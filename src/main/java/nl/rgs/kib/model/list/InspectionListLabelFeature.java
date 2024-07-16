package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rgs.kib.shared.models.Indexable;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
public class InspectionListLabelFeature implements Indexable {
    @NotNull()
    @Min(0)
    @Schema(example = "1", minimum = "0")
    private Integer index;

    @NotBlank()
    @Schema(example = "1990")
    private String name;
}
