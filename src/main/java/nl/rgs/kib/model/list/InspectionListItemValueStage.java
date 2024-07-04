package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rgs.kib.model.method.InspectionMethodStage;

@Data()
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor()
@AllArgsConstructor()
public class InspectionListItemValueStage extends InspectionMethodStage {
    @Min(0)
    @Max(100)
    @Schema(example = "25", minimum = "0", maximum = "100")
    private Double max;
}
