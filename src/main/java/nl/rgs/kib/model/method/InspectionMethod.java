package nl.rgs.kib.model.method;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.rgs.kib.shared.models.BaseObject;
import nl.rgs.kib.shared.validators.UniqueStages;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "inspection_method")
public class InspectionMethod extends BaseObject {
    @Id
    @NotBlank
    @Schema(example = "5f622c23a8efb61a54365f33")
    private String id;

    @NotBlank
    @Schema(example = "QuickScan")
    private String name;

    @NotNull
    @Schema(example = "STAGE")
    private InspectionMethodInput input;

    @Schema(example = "NEN2767")
    private InspectionMethodCalculationMethod calculationMethod;

    @Valid
    @NotNull
    @UniqueStages
    private List<InspectionMethodStage> stages = List.of();

    /**
     * Sorts the stages by <b>stage</b>.
     *
     * @param stages the list of stages to sort
     * @return the sorted list of stages
     * @see InspectionMethodStage
     */
    public static List<InspectionMethodStage> sortStages(List<InspectionMethodStage> stages) {
        return stages.stream()
                .sorted(Comparator.comparing(InspectionMethodStage::getStage))
                .toList();
    }
}
