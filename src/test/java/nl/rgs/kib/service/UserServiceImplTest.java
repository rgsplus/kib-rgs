package nl.rgs.kib.service;

import nl.rgs.kib.model.user.User;
import nl.rgs.kib.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

        UserResource userResource = mock(UserResource.class);
        when(userResource.toRepresentation()).thenReturn(null);

        UsersResource usersResource = mock(UsersResource.class);
        when(usersResource.get(userId)).thenReturn(userResource);

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
}