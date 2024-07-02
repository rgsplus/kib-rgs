package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.rgs.kib.model.method.InspectionMethodCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data()
public class InspectionListCodeItemValue {
    @NotNull()
    @Schema(example = "5f622c23aeefb61a54365f33")
    @Field(name = "id")
    private String id;

    @NotBlank()
    @Schema(example = "Roof")
    private String name;

    @Schema(example = "Wooden")
    private String group;

    @NotNull()
    @Schema(example = "SERIOUSLY")
    private InspectionListCodeItemValueCategory category;

    @NotNull()
    @Schema(example = "66223agefb61auj4365f12")
    private ObjectId inspectionMethodCodeId;

    @NotNull()
    private List<InspectionListCodeItemValueStage> stages;

    public String getInspectionMethodCodeId() {
        return inspectionMethodCodeId.toHexString();
    }

    public void setInspectionMethodCodeId(String inspectionMethodCodeId) {
        this.inspectionMethodCodeId = new ObjectId(inspectionMethodCodeId);
    }
}
