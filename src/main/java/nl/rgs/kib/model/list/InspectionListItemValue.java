package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data()
public class InspectionListItemValue {
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
    private InspectionListItemValueCategory category;

    @NotNull()
    @Schema(example = "66223agefb61auj4365f12")
    private ObjectId inspectionMethodId;

    @NotNull()
    private List<InspectionListItemValueStage> stages;

    public String getInspectionMethodId() {
        return inspectionMethodId.toHexString();
    }

    public void setInspectionMethodId(String inspectionMethodId) {
        this.inspectionMethodId = new ObjectId(inspectionMethodId);
    }
}
