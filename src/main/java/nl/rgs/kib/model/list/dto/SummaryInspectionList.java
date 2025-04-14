package nl.rgs.kib.model.list.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.model.list.InspectionListStatus;
import nl.rgs.kib.shared.models.AuditMetadata;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Data Transfer Object (DTO) representing a summarized view of an {@link nl.rgs.kib.model.list.InspectionList}.
 * This DTO is typically used for displaying lists of inspection lists where full detail is not required.
 *
 * @param id         The unique identifier of the InspectionList (e.g., "5f622c23a8efb61a54365f33"). Cannot be blank.
 * @param name       The name of the InspectionList (e.g., "RGS+ NEN_2767"). Cannot be blank.
 * @param status     The current status of the InspectionList (e.g., DEFINITIVE). Cannot be null.
 * @param totalItems The total count of items within the InspectionList (e.g., 25). Cannot be null.
 * @param metadata   The audit metadata associated with the InspectionList (creation/update timestamps and users). Cannot be null.
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