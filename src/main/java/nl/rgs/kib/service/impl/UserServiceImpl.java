package nl.rgs.kib.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import nl.rgs.kib.model.user.User;
import nl.rgs.kib.model.user.dto.CreateUser;
import nl.rgs.kib.service.MailService;
import nl.rgs.kib.service.UserService;
import org.apache.commons.compress.utils.Sets;
import org.apache.commons.lang3.RandomStringUtils;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService, InitializingBean {
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailService mailService;

    private Keycloak keycloak;

    @Value("${keycloak.server}")
    private String server;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${app.url}")
    private String appUrl;

    @Override
    public void afterPropertiesSet() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(server)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    @Override
    public List<User> findAll() {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> users = realmResource.roles().get("kib_core").getUserMembers(0, 1000);

        return users.stream().map(userRepresentation -> {
            UserResource userResource = usersResource.get(userRepresentation.getId());
            return new User(userResource);
        }).toList();
    }

    @Override
    public Optional<User> findById(String id) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(id);

        if (userResource == null) {
            return Optional.empty();
        }

        return Optional.of(userResource).map(User::new);
    }

    @Override
    public User create(CreateUser createUser) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        RolesResource rolesResource = realmResource.roles();

        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final String tempPassword = RandomStringUtils.random(15, characters);

        UserRepresentation userRepresentation = User.getUserRepresentation(createUser);

        userRepresentation.setCredentials(new LinkedList<>());
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(tempPassword);
        credentialRepresentation.setTemporary(true);
        credentialRepresentation.setType("password");
        userRepresentation.getCredentials().add(credentialRepresentation);

        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() != 200 && response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user");
        }

        String userId = CreatedResponseUtil.getCreatedId(response);
        UserResource userResource = usersResource.get(userId);
        userRepresentation = userResource.toRepresentation();

        List<String> requiredActions = userRepresentation.getRequiredActions() != null ? userRepresentation.getRequiredActions() : new ArrayList<>();

        if (createUser.twoFactorAuthentication()) {
            requiredActions.add("CONFIGURE_TOTP");
        } else {
            Optional<CredentialRepresentation> otpCredentialRepresentation =
                    userResource.credentials().stream().filter(cred -> cred.getType().equals("otp")).findFirst();

            otpCredentialRepresentation.ifPresent(representation -> userResource.removeCredential(representation.getId()));
        }

        RoleRepresentation kibCoreRole = rolesResource.get("kib_core").toRepresentation();
        RoleRepresentation roleRepresentation = switch (createUser.role()) {
            case ADMIN -> rolesResource.get("kib_admin").toRepresentation();
            case USER -> rolesResource.get("kib_user").toRepresentation();
        };

        userResource.roles().realmLevel().add(List.of(kibCoreRole, roleRepresentation));

        userResource.update(userRepresentation);

        User user = new User(userResource);

        this.sendActivationMail(user, tempPassword);

        return user;
    }

    @Override
    public Optional<User> update(User user) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserResource userResource = usersResource.get(user.getId());
        UserRepresentation userRepresentation = usersResource.get(user.getId()).toRepresentation();
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());

        List<String> requiredActions = userRepresentation.getRequiredActions() != null ? userRepresentation.getRequiredActions() : new ArrayList<>();

        if (user.getTwoFactorAuthentication()) {
            requiredActions.add("CONFIGURE_TOTP");
        } else {
            requiredActions.remove("CONFIGURE_TOTP");

            Optional<CredentialRepresentation> credentialRepresentation =
                    userResource.credentials().stream().filter(cred -> cred.getType().equals("otp")).findFirst();

            credentialRepresentation.ifPresent(representation -> userResource.removeCredential(representation.getId()));
        }

        List<RoleRepresentation> kibRoles = new ArrayList<>();
        kibRoles.add(realmResource.roles().get("kib_admin").toRepresentation());
        kibRoles.add(realmResource.roles().get("kib_user").toRepresentation());
        RoleRepresentation roleRepresentation = switch (user.getRole()) {
            case ADMIN -> realmResource.roles().get("kib_admin").toRepresentation();
            case USER -> realmResource.roles().get("kib_user").toRepresentation();
        };
        userResource.roles().realmLevel().remove(kibRoles);
        userResource.roles().realmLevel().add(List.of(roleRepresentation));

        usersResource.get(user.getId()).update(userRepresentation);

        return Optional.of(userResource).map(User::new);
    }

    @Override
    public Optional<User> deleteById(String id) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserResource userResource = usersResource.get(id);

        if (userResource == null) {
            return Optional.empty();
        }

        Optional<User> user = Optional.of(userResource).map(User::new);
        usersResource.delete(id);
        return user;
    }

    @Override
    public Boolean emailExists(String email) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        List<UserRepresentation> usersRepresentation = usersResource.searchByEmail(email, true);
        return !usersRepresentation.isEmpty();
    }

    @Override
    public Boolean usernameExists(String username) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        List<UserRepresentation> usersRepresentation = usersResource.searchByUsername(username, true);
        return !usersRepresentation.isEmpty();
    }

    @Override
    public Long adminUsersCount() {
        RealmResource realmResource = keycloak.realm(realm);

        RolesResource rolesResource = realmResource.roles();

        List<UserRepresentation> adminUsers = rolesResource.get("kib_admin").getUserMembers(0, 1000);
        List<UserRepresentation> coreUsers = rolesResource.get("kib_core").getUserMembers(0, 1000);

        return adminUsers.stream().filter(adminUser -> coreUsers.stream().anyMatch(coreUser -> coreUser.getId().equals(adminUser.getId()))).count();
    }

    private void sendActivationMail(User user, String tempPassword) {
        String invitationText = "Beste " + user.getFirstName() + ",<br/><br/>";
        invitationText += "Gefeliciteerd met uw account bij RGS+<br/>";
        invitationText += "Uw registratie is bijna gereed.<br/><br/>";
        invitationText += "U kunt inloggen met de volgende gegevens:<br/><br/>";

        invitationText += "Gebruikersnaam: <b>" + user.getEmail() + "</b><br/>";
        invitationText += "Wachtwoord: <b>" + tempPassword + "</b><br/><br/>";

        final Context ctx = new Context();
        ctx.setVariable("invitationText", invitationText);
        ctx.setVariable("headerText", "Uw RGS+ account");
        ctx.setVariable("link", this.appUrl);
        ctx.setVariable("linkText", "Inloggen");

        final String htmlContent = this.templateEngine.process("email/new-user-mail.html", ctx);

        Set<InternetAddress> emailTos = Sets.newHashSet();
        try {
            emailTos.add(new InternetAddress(user.getEmail(), user.getFirstName() + " " + user.getLastName()));
        } catch (UnsupportedEncodingException e) {
            log.error("error sending email", e);
        }
        final String subject = "Uw RGS+ account";

        try {
            this.mailService.sendMail(htmlContent, subject, "noreply@rgsplus.nl", emailTos.toArray(new InternetAddress[0]), null, null);
        } catch (MessagingException e) {
            log.error("error sending email", e);
        }

    }
}
