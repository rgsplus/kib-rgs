package nl.rgs.kib.model.method.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.model.method.InspectionMethodCalculationMethod;
import nl.rgs.kib.model.method.InspectionMethodInput;
import nl.rgs.kib.model.method.InspectionMethodStage;

import java.util.List;

public record CreateInspectionMethod (
    @NotBlank()
    @Schema(example = "QuickScan")
    String name,

    @NotNull()
    @Schema(example = "PERCENTAGE")
    InspectionMethodInput input,

    @NotNull()
    @Schema(example = "NEN2767")
    InspectionMethodCalculationMethod calculationMethod,

    @NotNull()
    List<InspectionMethodStage> stages
) {
}