package nl.rgs.kib.model.method.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.model.method.InspectionMethodCodeCalculationMethod;
import nl.rgs.kib.model.method.InspectionMethodCodeInput;

public record CreateInspectionMethodCode (
    @NotBlank()
    @Schema(example = "QuickScan")
    String name,

    @NotNull()
    @Schema(example = "PERCENTAGE")
    InspectionMethodCodeInput input,

    @NotNull()
    @Schema(example = "NEN2767")
    InspectionMethodCodeCalculationMethod calculationMethod
) {
}