package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data()
public class InspectionListLabelFeature {
    @NotNull()
    @Min(0)
    @Schema(example = "1", minimum = "0")
    private Integer index;

    @NotBlank()
    @Schema(example = "1990")
    private String name;
}
