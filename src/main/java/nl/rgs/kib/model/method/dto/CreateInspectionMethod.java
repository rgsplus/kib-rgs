package nl.rgs.kib.model.method.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.model.method.InspectionMethodCalculationMethod;
import nl.rgs.kib.model.method.InspectionMethodInput;
import nl.rgs.kib.model.method.InspectionMethodStage;
import nl.rgs.kib.shared.validators.UniqueStages;

import java.util.List;

/**
 * CreateInspectionMethod
 * <p>
 * DTO for creating an InspectionMethod
 * <p>
 *
 * @param name
 * @param input
 * @param calculationMethod
 * @param stages
 */
public record CreateInspectionMethod(
        @NotBlank()
        @Schema(example = "QuickScan")
        String name,

        @NotNull()
        @Schema(example = "PERCENTAGE")
        InspectionMethodInput input,

        @Schema(example = "NEN2767")
        InspectionMethodCalculationMethod calculationMethod,

        @Valid()
        @NotNull()
        @UniqueStages()
        List<InspectionMethodStage> stages
) {
}