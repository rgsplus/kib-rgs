package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data()
public class InspectionListItemValueStage {
    @NotNull()
    @Min(1)
    @Max(10)
    @Schema(example = "1", allowableValues = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"})
    private Integer stage;

    @NotBlank()
    @Schema(example = "Good")
    private String name;

    @Min(0)
    @Max(100)
    @Schema(example = "25", minimum = "0", maximum = "100")
    private Double max;
}
