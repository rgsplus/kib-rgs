package nl.rgs.kib.model.method;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data()
@Document(collection = "inspection_method_code")
public class InspectionMethodCode {
    @Id()
    @NotNull()
    @Schema(example = "5f622c23aeefb61a54365f33")
    private ObjectId id;

    @NotBlank()
    @Schema(example = "QuickScan")
    private String name;

    @NotNull()
    @Schema(example = "PERCENTAGE")
    private InspectionMethodCodeInput input;

    @NotNull()
    @Schema(example = "DEFINITIVE")
    private InspectionMethodCodeCalculationMethod calculationMethod;

    public String getId() {
        return id.toHexString();
    }

    public void setId(String id) {
        this.id = new ObjectId(id);
    }
}
