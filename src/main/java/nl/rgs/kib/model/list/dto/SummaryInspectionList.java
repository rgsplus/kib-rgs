package nl.rgs.kib.model.list.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.model.list.InspectionListStatus;
import nl.rgs.kib.shared.models.AuditMetadata;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * SummaryInspectionList
 * <p>
 * DTO for summarizing an InspectionList. For example, for displaying in a list of inspection-list.
 * <p>
 *
 * @param id         the id of the InspectionList
 * @param name       the name of the InspectionList
 * @param status     the status of the InspectionList
 * @param totalItems count of items in the InspectionList
 * @param metadata   the metadata of the InspectionList
 */
public record SummaryInspectionList(
        @NotBlank
        @Schema(example = "5f622c23a8efb61a54365f33")
        String id,

        @NotBlank
        @Schema(example = "RGS+ NEN_2767")
        String name,

        @NotNull
        @Schema(example = "DEFINITIVE")
        InspectionListStatus status,

        @NotNull
        @Schema(example = "25")
        Integer totalItems,

        @NotNull
        @Field(name = "_metadata")
        AuditMetadata metadata
) {
}