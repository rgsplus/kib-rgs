package nl.rgs.kib.model.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.json.StringListMapDeserializer;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Map;

//TODO: Define User constraints and validations for the properties

@Data()
@NoArgsConstructor()
public class User {

    @Schema(example = "5f622c23a8efb61a54365f33")
    private String id;

    @Schema(example = "john.doe")
    private String username;

    @Schema(example = "John")
    private String firstName;

    @Schema(example = "Doe")
    private String lastName;

    @Schema(example = "john.doe@gmail.com")
    private String email;

    @Schema(example = "{\"locale\": [\"en\"]}")
    @JsonDeserialize(using = StringListMapDeserializer.class)
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
}
