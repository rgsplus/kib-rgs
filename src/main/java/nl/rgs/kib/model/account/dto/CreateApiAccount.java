package nl.rgs.kib.model.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.shared.validators.EndDateAfterStartDate;

import java.util.Date;

/**
 * Data Transfer Object (DTO) for creating a new API Account.
 * Contains validation constraints and schema descriptions for API documentation.
 * Ensures the end date is after the start date using a custom validator.
 *
 * @param name         The name of the account holder (e.g., "John Doe"). Cannot be blank.
 * @param description  A description for the API account (e.g., "API account for Facebook integration"). Optional.
 * @param businessName The name of the associated business (e.g., "Facebook"). Cannot be blank.
 * @param startDate    The date when the account becomes active. Cannot be null.
 * @param endDate      The date when the account expires. Optional. Must be after startDate if provided.
 * @param active       Indicates whether the account is currently active. Cannot be null.
 */
@EndDateAfterStartDate
public record CreateApiAccount(
        @NotBlank
        @Schema(example = "John Doe", description = "Name of the account holder")
        String name,

        @Schema(example = "Api account for Facebook", description = "Description of the account")
        String description,

        @NotBlank
        @Schema(example = "Facebook", description = "Name of the business")
        String businessName,

        @NotNull
        @Schema(example = "2021-09-15T00:00:00.000Z", description = "Start date of the account")
        Date startDate,

        @Schema(example = "2021-09-15T00:00:00.000Z", description = "End date of the account")
        Date endDate,

        @NotNull
        @Schema(example = "true")
        Boolean active
) {
}
