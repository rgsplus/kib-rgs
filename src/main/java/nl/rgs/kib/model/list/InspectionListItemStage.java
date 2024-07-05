package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rgs.kib.model.method.InspectionMethodStage;
import org.hibernate.validator.constraints.URL;

@Data()
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor()
@AllArgsConstructor()
public class InspectionListItemStage extends InspectionMethodStage {
    @Min(0)
    @Max(100)
    @Schema(example = "25", minimum = "0", maximum = "100")
    private Double max;

    @URL()
    @Schema(example = "https://example.com/image.jpg")
    private String image;
}
