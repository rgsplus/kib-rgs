package nl.rgsnl.kib.model.list;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data()
@AllArgsConstructor()
@Document(collection = "inspection_list_code")
public class InspectionListCode {
    @Id()
    private ObjectId id;

    @NotBlank()
    private String name;

    @NotNull()
    private InspectionListCodeStatus status;

    @NotNull()
    private List<InspectionListCodeItem> items;
}