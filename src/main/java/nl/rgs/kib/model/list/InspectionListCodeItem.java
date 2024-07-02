package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data()
public class InspectionListCodeItem {
    @NotNull()
    @Min(0)
    @Schema(example = "1", minimum = "0")
    private Integer index;

    @NotNull()
    private InspectionListCodeItemValue value;
}
