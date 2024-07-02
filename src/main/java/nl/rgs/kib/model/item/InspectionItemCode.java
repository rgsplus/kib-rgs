package nl.rgs.kib.model.item;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @NotNull()
    @Schema(example = "5f622c23aeefb61a54365f33")
    private ObjectId id;

    @NotBlank()
    @Schema(example = "Roof")
    private String name;

    @Schema(example = "Wooden")
    private String group;

    @NotNull()
    @Schema(example = "SERIOUSLY")
    private InspectionItemCodeCategory category;

    @NotNull()
    @Schema(example = "66223agefb61auj4365f12")
    private ObjectId inspectionMethodCodeId;

    @NotNull()
    private List<InspectionItemCodeStage> stages;

    public String getId() {
        return id.toHexString();
    }

    public void setId(String id) {
        this.id = new ObjectId(id);
    }

    public String getInspectionMethodCodeId() {
        return inspectionMethodCodeId.toHexString();
    }

    public void setInspectionMethodCodeId(String inspectionMethodCodeId) {
        this.inspectionMethodCodeId = new ObjectId(inspectionMethodCodeId);
    }
}
