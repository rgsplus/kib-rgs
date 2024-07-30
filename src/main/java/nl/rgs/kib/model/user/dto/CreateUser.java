package nl.rgs.kib.model.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.keycloak.json.StringListMapDeserializer;

import java.util.List;
import java.util.Map;

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
 * @param attributes
 * @param realmRoles
 * @param clientRoles
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

        @JsonDeserialize(using = StringListMapDeserializer.class)
        @Schema(example = "{\"locale\": [\"en\"]}")
        Map<String, List<String>> attributes,

        List<String> realmRoles,

        Map<String, List<String>> clientRoles
) {
}
