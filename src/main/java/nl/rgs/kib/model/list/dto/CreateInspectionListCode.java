package nl.rgs.kib.model.list.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.model.list.InspectionListCodeItem;
import nl.rgs.kib.model.list.InspectionListCodeStatus;
import nl.rgs.kib.model.list.InspectionListCodeLabel;

import java.util.List;

public record CreateInspectionListCode(
        @NotBlank()
        @Schema(example = "RGS+ NEN_2767")
        String name,

        @NotNull()
        @Schema(example = "ACTIVE")
        InspectionListCodeStatus status,

        @NotNull()
        List<InspectionListCodeItem> items,

        @NotNull()
        List<InspectionListCodeLabel> labels
) {
}