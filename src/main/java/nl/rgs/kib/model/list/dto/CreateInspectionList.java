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
 * Data Transfer Object (DTO) for creating a new InspectionList.
 * Contains the necessary information to create an inspection list, including its name,
 * initial status, and the list of items it contains. Includes validation constraints
 * to ensure data integrity.
 *
 * @param name   The name of the inspection list (e.g., "RGS+ NEN_2767"). Cannot be blank.
 * @param status The initial status of the inspection list (e.g., DEFINITIVE). Cannot be null.
 * @param items  A list of {@link InspectionListItem} objects belonging to this inspection list.
 *               Cannot be null. The list itself and its elements are validated:
 *               - {@code @Valid}: Ensures nested validation of each {@link InspectionListItem}.
 *               - {@code @UniqueIds}: Ensures all item IDs within the list are unique.
 *               - {@code @ValidIndexes}: Ensures item indexes are sequential and start from 1.
 *               - {@code @UniqueStandardNos}: Ensures all item standard numbers within the list are unique.
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