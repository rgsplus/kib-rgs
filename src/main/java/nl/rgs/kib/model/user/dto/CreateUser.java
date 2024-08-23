package nl.rgs.kib.model.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.model.user.UserRole;

//TODO: Define Create User constraints and validations for the properties

/**
 * CreateUser
 * <p>
 * DTO for creating an User
 * <p>
 *
 * @param username
 * @param firstName
 * @param lastName
 * @param email
 * @param role
 */
public record CreateUser(

        @NotBlank()
        @Schema(example = "john.doe")
        String username,

        @NotBlank()
        @Schema(example = "John")
        String firstName,

        @Schema(example = "Doe")
        String lastName,

        @NotBlank()
        @Schema(example = "john.doe@gmail.com")
        String email,

        @NotNull()
        @Schema(example = "USER")
        UserRole role
) {
}
