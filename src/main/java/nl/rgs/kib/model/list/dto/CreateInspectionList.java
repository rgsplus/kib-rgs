package nl.rgs.kib.model.list.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.model.list.InspectionListItem;
import nl.rgs.kib.model.list.InspectionListStatus;
import nl.rgs.kib.model.list.InspectionListLabel;

import java.util.List;

public record CreateInspectionList(
        @NotBlank()
        @Schema(example = "RGS+ NEN_2767")
        String name,

        @NotNull()
        @Schema(example = "DEFINITIVE")
        InspectionListStatus status,

        @NotNull()
        @Valid()
        List<InspectionListItem> items,

        @NotNull()
        @Valid()
        List<InspectionListLabel> labels
) {
}