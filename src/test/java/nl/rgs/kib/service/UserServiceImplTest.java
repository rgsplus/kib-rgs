package nl.rgs.kib.service;

import jakarta.mail.MessagingException;
import jakarta.ws.rs.core.Response;
import nl.rgs.kib.model.user.User;
import nl.rgs.kib.model.user.UserRole;
import nl.rgs.kib.model.user.dto.CreateUser;
import nl.rgs.kib.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;

import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MailService mailService;

    @Mock
    private Keycloak keycloak;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testFindAll() {
        // Arrange
        UserRepresentation userRepresentation1 = new UserRepresentation();
        userRepresentation1.setId(UUID.randomUUID().toString());
        userRepresentation1.setEmail("email1@test.com");
        userRepresentation1.setUsername("username1");
        userRepresentation1.setTotp(true);
        userRepresentation1.setRealmRoles(List.of("kib_core", "kib_admin"));
        userRepresentation1.setClientRoles(Map.of());

        RoleMappingResource roleMappingResource1 = mock(RoleMappingResource.class);
        when(roleMappingResource1.realmLevel()).thenReturn(mock(RoleScopeResource.class));

        UserResource userResource1 = mock(UserResource.class);
        when(userResource1.toRepresentation()).thenReturn(userRepresentation1);
        when(userResource1.roles()).thenReturn(roleMappingResource1);

        UserRepresentation userRepresentation2 = new UserRepresentation();
        userRepresentation2.setId(UUID.randomUUID().toString());
        userRepresentation2.setEmail("email2@test.com");
        userRepresentation2.setUsername("username2");
        userRepresentation2.setTotp(true);
        userRepresentation2.setRealmRoles(List.of("kib_core", "kib_user"));
        userRepresentation2.setClientRoles(Map.of());

        RoleMappingResource roleMappingResource2 = mock(RoleMappingResource.class);
        when(roleMappingResource2.realmLevel()).thenReturn(mock(RoleScopeResource.class));

        UserResource userResource2 = mock(UserResource.class);
        when(userResource2.toRepresentation()).thenReturn(userRepresentation2);
        when(userResource2.roles()).thenReturn(roleMappingResource2);

        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(userRepresentation1.getId())).thenReturn(userResource1);
        when(usersResource.get(userRepresentation2.getId())).thenReturn(userResource2);

        RoleResource roleResource = mock(RoleResource.class);
        when(roleResource.getUserMembers(0, 1000)).thenReturn(List.of(userRepresentation1, userRepresentation2));

        RolesResource rolesResource = mock(RolesResource.class);
        when(rolesResource.get("kib_core")).thenReturn(roleResource);

        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.roles()).thenReturn(rolesResource);

        when(realmResource.users()).thenReturn(usersResource);

        when(keycloak.realm(any())).thenReturn(realmResource);

        // Act
        List<User> users = userService.findAll();

        // Assert
        assertEquals(2, users.size());
        assertEquals(userRepresentation1.getId(), users.getFirst().getId());
        assertEquals(userRepresentation1.getEmail(), users.getFirst().getEmail());
        assertEquals(userRepresentation1.getFirstName(), users.getFirst().getFirstName());
        assertEquals(userRepresentation1.getLastName(), users.getFirst().getLastName());
        verify(keycloak, times(1)).realm(any());
        verify(realmResource, times(1)).roles();
        verify(rolesResource, times(1)).get("kib_core");
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        String userId = UUID.randomUUID().toString();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(userId);
        userRepresentation.setEmail("email@test.com");
        userRepresentation.setUsername("username");
        userRepresentation.setTotp(true);
        userRepresentation.setRealmRoles(List.of("kib_core", "kib_admin"));
        userRepresentation.setClientRoles(Map.of());

        UserResource userResource = mock(UserResource.class);
        when(userResource.toRepresentation()).thenReturn(userRepresentation);

        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(userId)).thenReturn(userResource);

        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);

        RoleScopeResource roleScopeResource = mock(RoleScopeResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);

        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);

        when(keycloak.realm(any())).thenReturn(realmResource);

        // Act
        Optional<User> user = userService.findById(userId);

        // Assert
        assertTrue(user.isPresent());
        assertEquals(userId, user.get().getId());
        assertEquals(userRepresentation.getEmail(), user.get().getEmail());
        assertEquals(userRepresentation.getFirstName(), user.get().getFirstName());
        assertEquals(userRepresentation.getLastName(), user.get().getLastName());
        verify(keycloak, times(1)).realm(any());
        verify(realmResource, times(1)).users();
        verify(usersResource, times(1)).get(userId);
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange
        String userId = UUID.randomUUID().toString();

        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(userId)).thenReturn(null);

        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);

        when(keycloak.realm(any())).thenReturn(realmResource);

        // Act
        Optional<User> user = userService.findById(userId);

        // Assert
        assertFalse(user.isPresent());
        verify(keycloak, times(1)).realm(any());
        verify(realmResource, times(1)).users();
        verify(usersResource, times(1)).get(userId);
    }

    @Test
    public void testDeleteById_Success() {
        // Arrange
        String userId = UUID.randomUUID().toString();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(userId);
        userRepresentation.setEmail("test@email.com");
        userRepresentation.setUsername("username");
        userRepresentation.setTotp(true);
        userRepresentation.setRealmRoles(List.of("kib_core", "kib_admin"));
        userRepresentation.setClientRoles(Map.of());

        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(mock(RoleScopeResource.class));

        UserResource userResource = mock(UserResource.class);
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(userResource.toRepresentation()).thenReturn(userRepresentation);

        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(userId)).thenReturn(userResource);

        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);

        when(keycloak.realm(any())).thenReturn(realmResource);

        // Act
        Optional<User> user = userService.deleteById(userId);

        // Assert
        assertTrue(user.isPresent());
        assertEquals(userId, user.get().getId());
        assertEquals(userRepresentation.getEmail(), user.get().getEmail());
        assertEquals(userRepresentation.getFirstName(), user.get().getFirstName());
        verify(keycloak, times(1)).realm(any());
        verify(realmResource, times(1)).users();
        verify(usersResource, times(1)).get(userId);
        verify(usersResource, times(1)).delete(userId);
    }

    @Test
    public void testDeleteById_NotFound() {
        // Arrange
        String userId = UUID.randomUUID().toString();

        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(userId)).thenReturn(null);

        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);

        when(keycloak.realm(any())).thenReturn(realmResource);

        // Act
        Optional<User> user = userService.deleteById(userId);

        // Assert
        assertFalse(user.isPresent());
        verify(keycloak, times(1)).realm(any());
        verify(realmResource, times(1)).users();
        verify(usersResource, times(1)).get(userId);
    }

    @Test
    public void testEmailExists_True() {
        // Arrange
        String email = "test@email.com";

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(UUID.randomUUID().toString());
        userRepresentation.setEmail(email);
        userRepresentation.setUsername("username");
        userRepresentation.setTotp(true);
        userRepresentation.setRealmRoles(List.of("kib_core", "kib_admin"));
        userRepresentation.setClientRoles(Map.of());

        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.searchByEmail(email, true)).thenReturn(List.of(userRepresentation));

        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);

        when(keycloak.realm(any())).thenReturn(realmResource);

        // Act
        Boolean emailExists = userService.emailExists(email);

        // Assert
        assertTrue(emailExists);
        verify(keycloak, times(1)).realm(any());
        verify(realmResource, times(1)).users();
        verify(usersResource, times(1)).searchByEmail(email, true);
    }


    @Test
    public void testEmailExists_False() {
        // Arrange
        String email = "test@email.com";

        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.searchByEmail(email, true)).thenReturn(List.of());

        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);

        when(keycloak.realm(any())).thenReturn(realmResource);

        // Act
        Boolean emailExists = userService.emailExists(email);

        // Assert
        assertFalse(emailExists);
        verify(keycloak, times(1)).realm(any());
        verify(realmResource, times(1)).users();
        verify(usersResource, times(1)).searchByEmail(email, true);
    }

    @Test
    public void testUsernameExists_True() {
        // Arrange
        String username = "username";

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(UUID.randomUUID().toString());
        userRepresentation.setEmail("test@email.com");
        userRepresentation.setUsername(username);
        userRepresentation.setTotp(true);
        userRepresentation.setRealmRoles(List.of("kib_core", "kib_admin"));
        userRepresentation.setClientRoles(Map.of());

        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.searchByUsername(username, true)).thenReturn(List.of(userRepresentation));

        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);

        when(keycloak.realm(any())).thenReturn(realmResource);

        // Act
        Boolean usernameExists = userService.usernameExists(username);

        // Assert
        assertTrue(usernameExists);
        verify(keycloak, times(1)).realm(any());
        verify(realmResource, times(1)).users();
        verify(usersResource, times(1)).searchByUsername(username, true);
    }

    @Test
    public void testUsernameExists_False() {
        // Arrange
        String username = "username";

        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.searchByUsername(username, true)).thenReturn(List.of());

        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);

        when(keycloak.realm(any())).thenReturn(realmResource);

        // Act
        Boolean usernameExists = userService.usernameExists(username);

        // Assert
        assertFalse(usernameExists);
        verify(keycloak, times(1)).realm(any());
        verify(realmResource, times(1)).users();
        verify(usersResource, times(1)).searchByUsername(username, true);
    }

    @Test
    public void testAdminUsersCount() {
        // Arrange
        UserRepresentation userRepresentation1 = new UserRepresentation();
        userRepresentation1.setId(UUID.randomUUID().toString());
        userRepresentation1.setEmail("test@email.com");
        userRepresentation1.setUsername("username1");
        userRepresentation1.setTotp(true);
        userRepresentation1.setRealmRoles(List.of("kib_core", "kib_admin"));
        userRepresentation1.setClientRoles(Map.of());

        RoleResource roleResource = mock(RoleResource.class);
        when(roleResource.getUserMembers(0, 1000)).thenReturn(List.of(userRepresentation1));

        RolesResource rolesResource = mock(RolesResource.class);
        when(rolesResource.get(anyString())).thenReturn(roleResource);

        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.roles()).thenReturn(rolesResource);

        when(keycloak.realm(any())).thenReturn(realmResource);

        // Act
        Long adminUsersCount = userService.adminUsersCount();

        // Assert
        assertEquals(1, adminUsersCount);
        verify(keycloak, times(1)).realm(any());
        verify(realmResource, times(1)).roles();
        verify(rolesResource, times(1)).get("kib_admin");
        verify(rolesResource, times(1)).get("kib_core");
    }

    @Test
    public void testCreateUser_Success() throws MessagingException {
        // Arrange
        CreateUser createUser = new CreateUser(
                "firstName",
                "lastName",
                "test@email.com",
                UserRole.USER,
                true
        );

        UserRepresentation expectedUserRepresentation = new UserRepresentation();
        expectedUserRepresentation.setId(UUID.randomUUID().toString());
        expectedUserRepresentation.setEmail(createUser.email());
        expectedUserRepresentation.setUsername(createUser.email());
        expectedUserRepresentation.setFirstName(createUser.firstName());
        expectedUserRepresentation.setLastName(createUser.lastName());
        expectedUserRepresentation.setTotp(true);
        expectedUserRepresentation.setRealmRoles(List.of("kib_core", "kib_user"));

        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(mock(RoleScopeResource.class));

        UserResource expectedUserResource = mock(UserResource.class);
        when(expectedUserResource.roles()).thenReturn(roleMappingResource);
        when(expectedUserResource.toRepresentation()).thenReturn(expectedUserRepresentation);

        User expectedUser = new User();
        expectedUser.setId(expectedUserRepresentation.getId());
        expectedUser.setEmail(expectedUserRepresentation.getEmail());
        expectedUser.setFirstName(expectedUserRepresentation.getFirstName());
        expectedUser.setLastName(expectedUserRepresentation.getLastName());
        expectedUser.setRole(createUser.role());
        expectedUser.setTwoFactorAuthentication(createUser.twoFactorAuthentication());

        RealmResource realmResource = mock(RealmResource.class);
        UsersResource usersResource = mock(UsersResource.class);
        when(realmResource.users()).thenReturn(usersResource);

        when(keycloak.realm(any())).thenReturn(realmResource);

        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(201);

        URI uri = mock(URI.class);
        when(uri.getPath()).thenReturn("/realms/kib/users/" + expectedUser.getId());
        when(response.getLocation()).thenReturn(uri);
        when(response.getStatusInfo()).thenReturn(Response.Status.CREATED);

        when(usersResource.create(any())).thenReturn(response);

        when(usersResource.get(expectedUser.getId())).thenReturn(expectedUserResource);

        RolesResource rolesResource = mock(RolesResource.class);
        when(realmResource.roles()).thenReturn(rolesResource);

        RoleResource kibCoreRoleResource = mock(RoleResource.class);
        when(rolesResource.get("kib_core")).thenReturn(kibCoreRoleResource);

        RoleResource kibUserRoleResource = mock(RoleResource.class);
        when(rolesResource.get("kib_user")).thenReturn(kibUserRoleResource);

        RoleRepresentation kibCoreRole = new RoleRepresentation();
        kibCoreRole.setId(UUID.randomUUID().toString());
        kibCoreRole.setName("kib_core");
        kibCoreRole.setDescription("KIB Core Role");

        RoleRepresentation kibUserRole = new RoleRepresentation();
        kibUserRole.setId(UUID.randomUUID().toString());
        kibUserRole.setName("kib_user");
        kibUserRole.setDescription("KIB User Role");

        when(kibCoreRoleResource.toRepresentation()).thenReturn(kibCoreRole);
        when(kibUserRoleResource.toRepresentation()).thenReturn(kibUserRole);

        // Act
        User user = userService.create(createUser);

        // Assert
        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getFirstName(), user.getFirstName());
        assertEquals(expectedUser.getLastName(), user.getLastName());
        assertEquals(expectedUser.getRole(), user.getRole());
        assertEquals(expectedUser.getTwoFactorAuthentication(), user.getTwoFactorAuthentication());
        verify(keycloak, times(1)).realm(any());
        verify(realmResource, times(1)).users();
        verify(usersResource, times(1)).create(any());
        verify(usersResource, times(1)).get(expectedUser.getId());
        verify(expectedUserResource, times(2)).roles();
        verify(expectedUserResource, times(2)).toRepresentation();
        verify(rolesResource, times(1)).get("kib_core");
        verify(rolesResource, times(1)).get("kib_user");
        verify(rolesResource, never()).get("kib_admin");
        verify(kibCoreRoleResource, times(1)).toRepresentation();
        verify(kibUserRoleResource, times(1)).toRepresentation();
        verify(mailService, times(1)).sendMail(any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testUpdate_Success() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("email@email.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setRole(UserRole.USER);
        user.setTwoFactorAuthentication(true);

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(user.getId());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setUsername(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setTotp(true);
        userRepresentation.setRealmRoles(List.of("kib_core", "kib_user"));
        userRepresentation.setClientRoles(Map.of());
        userRepresentation.setRequiredActions(new ArrayList<>());

        RoleMappingResource roleMappingResource = mock(RoleMappingResource.class);
        when(roleMappingResource.realmLevel()).thenReturn(mock(RoleScopeResource.class));

        UserResource userResource = mock(UserResource.class);
        when(userResource.toRepresentation()).thenReturn(userRepresentation);
        when(userResource.roles()).thenReturn(roleMappingResource);

        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(user.getId())).thenReturn(userResource);

        RoleRepresentation kibCoreRole = new RoleRepresentation();
        kibCoreRole.setId(UUID.randomUUID().toString());
        kibCoreRole.setName("kib_core");
        kibCoreRole.setDescription("KIB Core Role");

        RoleRepresentation kibUserRole = new RoleRepresentation();
        kibUserRole.setId(UUID.randomUUID().toString());
        kibUserRole.setName("kib_user");
        kibUserRole.setDescription("KIB User Role");

        RoleResource kibUserRoleResource = mock(RoleResource.class);
        when(kibUserRoleResource.toRepresentation()).thenReturn(kibUserRole);

        RoleRepresentation kibAdminRole = new RoleRepresentation();
        kibAdminRole.setId(UUID.randomUUID().toString());
        kibAdminRole.setName("kib_admin");
        kibAdminRole.setDescription("KIB Admin Role");

        RoleResource kibAdminRoleResource = mock(RoleResource.class);
        when(kibAdminRoleResource.toRepresentation()).thenReturn(kibAdminRole);

        RolesResource rolesResource = mock(RolesResource.class);
        when(rolesResource.get("kib_user")).thenReturn(kibUserRoleResource);
        when(rolesResource.get("kib_admin")).thenReturn(kibAdminRoleResource);

        RealmResource realmResource = mock(RealmResource.class);
        when(realmResource.users()).thenReturn(usersResource);
        when(realmResource.roles()).thenReturn(rolesResource);

        when(keycloak.realm(any())).thenReturn(realmResource);

        // Act
        Optional<User> updatedUser = userService.update(user);

        // Assert
        assertTrue(updatedUser.isPresent());
        assertEquals(user.getId(), updatedUser.get().getId());
        assertEquals(user.getEmail(), updatedUser.get().getEmail());
        assertEquals(user.getFirstName(), updatedUser.get().getFirstName());
        assertEquals(user.getLastName(), updatedUser.get().getLastName());
        assertEquals(user.getRole(), updatedUser.get().getRole());
        assertEquals(user.getTwoFactorAuthentication(), updatedUser.get().getTwoFactorAuthentication());
        verify(keycloak, times(1)).realm(any());
        verify(realmResource, times(1)).users();
        verify(usersResource, times(3)).get(user.getId());
        verify(userResource, times(2)).toRepresentation();
        verify(userResource, times(3)).roles();
        verify(userResource, times(1)).update(userRepresentation);
    }

}