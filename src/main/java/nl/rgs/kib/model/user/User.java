package nl.rgs.kib.model.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rgs.kib.model.user.dto.CreateUser;
import org.keycloak.json.StringListMapDeserializer;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Map;

@Data()
@NoArgsConstructor()
public class User {

    @NotBlank()
    @Schema(example = "5f622c23a8efb61a54365f33")
    private String id;

    @NotBlank()
    @Schema(example = "john.doe")
    private String username;

    @NotBlank()
    @Schema(example = "John")
    private String firstName;

    @Schema(example = "Doe")
    private String lastName;

    @NotBlank()
    @Schema(example = "john.doe@gmail.com")
    private String email;

    @JsonDeserialize(using = StringListMapDeserializer.class)
    @Schema(example = "{\"locale\": [\"en\"]}")
    private Map<String, List<String>> attributes;

    private List<String> realmRoles;

    private Map<String, List<String>> clientRoles;

    public User(UserRepresentation userRepresentation) {
        this.id = userRepresentation.getId();
        this.username = userRepresentation.getUsername();
        this.firstName = userRepresentation.getFirstName();
        this.lastName = userRepresentation.getLastName();
        this.email = userRepresentation.getEmail();
        this.attributes = userRepresentation.getAttributes();
        this.realmRoles = userRepresentation.getRealmRoles();
        this.clientRoles = userRepresentation.getClientRoles();
    }

    public static UserRepresentation getUserRepresentation(CreateUser createUser) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(createUser.username());
        userRepresentation.setFirstName(createUser.firstName());
        userRepresentation.setLastName(createUser.lastName());
        userRepresentation.setEmail(createUser.email());
        userRepresentation.setAttributes(createUser.attributes());
        userRepresentation.setRealmRoles(createUser.realmRoles());
        userRepresentation.setClientRoles(createUser.clientRoles());
        userRepresentation.setEnabled(true);

        return userRepresentation;
    }
}
