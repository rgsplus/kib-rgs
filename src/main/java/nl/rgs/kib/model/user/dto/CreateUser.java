package nl.rgs.kib.model.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
        String username,
        String firstName,
        String lastName,
        String email,
        @JsonDeserialize(using = StringListMapDeserializer.class)
        Map<String, List<String>> attributes,
        List<String> realmRoles,
        Map<String, List<String>> clientRoles
) {
}
