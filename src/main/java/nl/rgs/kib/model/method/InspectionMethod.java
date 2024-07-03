package nl.rgs.kib.model.method;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.rgs.kib.shared.models.AuditMetadata;
import nl.rgs.kib.shared.models.BaseObject;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data()
@EqualsAndHashCode(callSuper = true)
@Document(collection = "inspection_method_code")
public class InspectionMethod extends BaseObject  {
    @Id()
    @NotNull()
    @Schema(example = "5f622c23aeefb61a54365f33")
    private ObjectId id;

    @NotBlank()
    @Schema(example = "QuickScan")
    private String name;

    @NotNull()
    @Schema(example = "PERCENTAGE")
    private InspectionMethodInput input;

    @NotNull()
    @Schema(example = "NEN2767")
    private InspectionMethodCalculationMethod calculationMethod;

    public String getId() {
        return id.toHexString();
    }

    public void setId(String id) {
        this.id = new ObjectId(id);
    }
}
