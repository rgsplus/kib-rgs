package nl.rgs.kib.model.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.model.user.UserRole;

/**
 * Data Transfer Object (DTO) for creating a new User.
 * Contains the necessary information required to register a new user in the system,
 * including personal details, email, assigned role, and two-factor authentication preference.
 * Includes validation constraints to ensure data integrity.
 *
 * @param firstName               The first name of the user (e.g., "John"). Cannot be blank.
 * @param lastName                The last name of the user (e.g., "Doe"). Optional.
 * @param email                   The email address of the user (e.g., "john.doe@gmail.com"). Must be a valid email format and cannot be blank.
 * @param role                    The role assigned to the user (e.g., USER). Cannot be null. Determines user permissions.
 * @param twoFactorAuthentication Flag indicating whether two-factor authentication should be enabled for the user. Cannot be null.
 */
public record CreateUser(
        @NotBlank
        @Schema(example = "John")
        String firstName,

        @Schema(example = "Doe")
        String lastName,

        @Email
        @NotBlank
        @Schema(example = "john.doe@gmail.com")
        String email,

        @NotNull
        @Schema(example = "USER")
        UserRole role,

        @NotNull
        @Schema(example = "true")
        Boolean twoFactorAuthentication
) {
}
