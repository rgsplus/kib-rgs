package nl.rgs.kib.model.list.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.model.list.InspectionListItem;
import nl.rgs.kib.model.list.InspectionListStatus;
import nl.rgs.kib.shared.validators.UniqueIds;
import nl.rgs.kib.shared.validators.UniqueStandardNos;
import nl.rgs.kib.shared.validators.ValidIndexes;

import java.util.List;

/**
 * CreateInspectionList
 * <p>
 * DTO for creating an InspectionList
 * <p>
 *
 * @param name
 * @param status
 * @param items
 */
public record CreateInspectionList(
        @NotBlank
        @Schema(example = "RGS+ NEN_2767")
        String name,

        @NotNull
        @Schema(example = "DEFINITIVE")
        InspectionListStatus status,

        @Valid
        @NotNull
        @UniqueIds
        @ValidIndexes
        @UniqueStandardNos
        List<InspectionListItem> items
) {
}