package nl.rgsnl.kib.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data()
@Document(collection = "inspection_method_code")
public class InspectionMethodCode {
    @Id()
    private ObjectId id;
}
