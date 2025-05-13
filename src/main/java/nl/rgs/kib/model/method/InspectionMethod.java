package nl.rgs.kib.model.method;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.rgs.kib.shared.models.BaseObject;
import nl.rgs.kib.shared.models.Sortable;
import nl.rgs.kib.shared.validators.UniqueStages;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "inspection_method")
public class InspectionMethod extends BaseObject implements Sortable {
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
    private List<InspectionMethodStage> stages = new ArrayList<>();


    /**
     * Sorts the {@code stages} list of this inspection method in place based on their natural order.
     * This method ensures that the stages within the inspection method are consistently ordered,
     * typically relying on the {@link Comparable} implementation of {@link InspectionMethodStage}.
     * <p>
     *
     * @see InspectionMethodStage#compareTo(InspectionMethodStage)
     * @see Sortable#applySort()
     */
    @Override
    public void applySort() {
        List<InspectionMethodStage> mutableStages = new ArrayList<>(this.getStages());
        mutableStages.sort(null);
        this.setStages(mutableStages);
    }
}
