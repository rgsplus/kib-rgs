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
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        List<UserRepresentation> userRepresentations = usersResource.list(0, 10000);
        return userRepresentations.stream().map(User::new).toList();
    }

    @Override
    public Optional<User> findById(String id) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserRepresentation userRepresentation = usersResource.get(id).toRepresentation();
        return Optional.ofNullable(userRepresentation).map(User::new);
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

        return new User(usersResource.get(userId).toRepresentation());
    }

    @Override
    public Optional<User> update(User user) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserRepresentation userRepresentation = usersResource.get(user.getId()).toRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setAttributes(user.getAttributes());
        userRepresentation.setRealmRoles(user.getRealmRoles());
        userRepresentation.setClientRoles(user.getClientRoles());

        usersResource.get(user.getId()).update(userRepresentation);

        return Optional.of(userRepresentation).map(User::new);
    }

    @Override
    public Optional<User> deleteById(String id) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        UserRepresentation userRepresentation = usersResource.get(id).toRepresentation();
        usersResource.delete(id);
        return Optional.ofNullable(userRepresentation).map(User::new);
    }
}
