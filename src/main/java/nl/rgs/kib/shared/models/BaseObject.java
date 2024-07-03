package nl.rgs.kib.shared.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
public class BaseObject {

    @JsonIgnore()
    @Field(name = "_metadata")
    private AuditMetadata metadata;
}