package nl.rgs.kib.model.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rgs.kib.model.user.dto.CreateUser;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

@Data
@NoArgsConstructor
public class User {
    @NotBlank
    @Schema(example = "5f622c23a8efb61a54365f33")
    private String id;

    @NotBlank
    @Schema(example = "John")
    private String firstName;

    @Schema(example = "Doe")
    private String lastName;

    @NotBlank
    @Schema(example = "john.doe@gmail.com")
    private String email;

    @NotNull
    @Schema(example = "USER")
    private UserRole role;

    @NotNull
    @Schema(example = "true")
    private Boolean twoFactorAuthentication;

    @SuppressWarnings("deprecation")
    public User(UserResource userResource) {
        List<RoleRepresentation> roles = userResource.roles().realmLevel().listAll();
        UserRepresentation userRepresentation = userResource.toRepresentation();

        this.id = userRepresentation.getId();
        this.firstName = userRepresentation.getFirstName();
        this.lastName = userRepresentation.getLastName();
        this.email = userRepresentation.getEmail();
        this.twoFactorAuthentication = userRepresentation.isTotp() || userRepresentation.getRequiredActions().contains("CONFIGURE_TOTP");
        this.role = roles.stream().anyMatch(role -> role.getName().equals("kib_admin")) ? UserRole.ADMIN : UserRole.USER;
    }

    public static UserRepresentation getUserRepresentation(CreateUser createUser) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(createUser.firstName());
        userRepresentation.setLastName(createUser.lastName());
        userRepresentation.setEmail(createUser.email());
        userRepresentation.setEnabled(true);

        return userRepresentation;
    }
}
