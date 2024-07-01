package nl.rgs.kib.model.item;

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
@Document(collection = "inspection_item_code")
public class InspectionItemCode {
    @Id()
    private String id;

    @NotBlank()
    private String name;

    private String group;

    @NotNull()
    private InspectionItemCodeCategory category;

    @NotNull()
    private ObjectId inspectionMethodCodeId;

    @NotNull()
    private List<InspectionItemCodeStage> stages;
}
