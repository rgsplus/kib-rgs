package nl.rgsnl.kib.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data()
@AllArgsConstructor()
@Document(collection = "inspection_list_code")
public class InspectionListCode {
    @Id()
    private ObjectId id;
}