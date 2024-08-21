package nl.rgs.kib.model.method;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.rgs.kib.shared.models.BaseObject;
import nl.rgs.kib.shared.validators.UniqueStages;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Data()
@EqualsAndHashCode(callSuper = true)
@Document(collection = "inspection_method")
public class InspectionMethod extends BaseObject {

    @Id()
    @NotNull()
    @Schema(example = "5f622c23a8efb61a54365f33")
    private ObjectId id;

    @NotBlank()
    @Schema(example = "QuickScan")
    private String name;

    @NotNull()
    @Schema(example = "STAGE")
    private InspectionMethodInput input;

    @Schema(example = "NEN2767")
    private InspectionMethodCalculationMethod calculationMethod;

    @Valid()
    @NotNull()
    @UniqueStages()
    private List<InspectionMethodStage> stages;

    public static List<InspectionMethodStage> sortStages(List<InspectionMethodStage> stages) {
        return stages.stream()
                .sorted(Comparator.comparing(InspectionMethodStage::getStage))
                .toList();
    }

    public String getId() {
        return Optional.ofNullable(id).map(ObjectId::toHexString).orElse(null);
    }

    public void setId(String id) {
        this.id = Optional.ofNullable(id).map(ObjectId::new).orElse(null);
    }
}
