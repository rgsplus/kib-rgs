package nl.rgs.kib.service.impl;

import jakarta.ws.rs.core.Response;
import nl.rgs.kib.model.user.User;
import nl.rgs.kib.model.user.dto.CreateUser;
import nl.rgs.kib.service.UserService;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, InitializingBean {
    private Keycloak keycloak;

    @Value("${keycloak.server}")
    private String server;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

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

        return users.stream().map(u -> new User(u, usersResource)).toList();
    }

    @Override
    public Optional<User> findById(String id) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserRepresentation userRepresentation = usersResource.get(id).toRepresentation();
        return Optional.ofNullable(userRepresentation).map(u -> new User(u, usersResource));
    }

    @Override
    public User create(CreateUser createUser) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        Response response = usersResource.create(User.getUserRepresentation(createUser));

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user");
        }

        String userId = CreatedResponseUtil.getCreatedId(response);

        UserRepresentation userRepresentation = usersResource.get(userId).toRepresentation();

        RoleRepresentation kibCoreRole = realmResource.roles().get("kib_core").toRepresentation();
        RoleRepresentation roleRepresentation = switch (createUser.role()) {
            case ADMIN -> realmResource.roles().get("kib_admin").toRepresentation();
            case USER -> realmResource.roles().get("kib_user").toRepresentation();
        };

        usersResource.get(userId).roles().realmLevel().add(List.of(kibCoreRole, roleRepresentation));

        return new User(userRepresentation, usersResource);
    }

    @Override
    public Optional<User> update(User user) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation userRepresentation = usersResource.get(user.getId()).toRepresentation();
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        usersResource.get(user.getId()).update(userRepresentation);

        List<RoleRepresentation> kibRoles = new ArrayList<>();
        kibRoles.add(realmResource.roles().get("kib_admin").toRepresentation());
        kibRoles.add(realmResource.roles().get("kib_user").toRepresentation());
        RoleRepresentation roleRepresentation = switch (user.getRole()) {
            case ADMIN -> realmResource.roles().get("kib_admin").toRepresentation();
            case USER -> realmResource.roles().get("kib_user").toRepresentation();
        };
        usersResource.get(user.getId()).roles().realmLevel().remove(kibRoles);
        usersResource.get(user.getId()).roles().realmLevel().add(List.of(roleRepresentation));

        return Optional.of(userRepresentation).map(u -> new User(u, usersResource));
    }

    @Override
    public Optional<User> deleteById(String id) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserRepresentation userRepresentation = usersResource.get(id).toRepresentation();
        Optional<User> user = Optional.ofNullable(userRepresentation).map(u -> new User(u, usersResource));
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

        List<UserRepresentation> adminUsers = realmResource.roles().get("kib_admin").getUserMembers(0, 1000);
        List<UserRepresentation> coreUsers = realmResource.roles().get("kib_core").getUserMembers(0, 1000);

        return adminUsers.stream().filter(adminUser -> coreUsers.stream().anyMatch(coreUser -> coreUser.getId().equals(adminUser.getId()))).count();
    }
}
