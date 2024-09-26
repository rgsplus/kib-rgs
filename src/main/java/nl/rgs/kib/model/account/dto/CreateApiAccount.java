package nl.rgs.kib.model.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.shared.validators.EndDateAfterStartDate;

import java.util.Date;

/**
 * CreateApiAccount
 * <p>
 * DTO for creating an ApiAccount
 * <p>
 *
 * @param name
 * @param businessName
 * @param startDate
 * @param endDate
 * @param active
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
