package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rgs.kib.model.method.InspectionMethodStage;
import nl.rgs.kib.shared.validators.MainImage;
import nl.rgs.kib.shared.validators.UniqueFileIds;

import java.util.List;

@Data()
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor()
@AllArgsConstructor()
public class InspectionListItemStage extends InspectionMethodStage {
    @Min(0)
    @Max(100)
    @Schema(example = "25", minimum = "0", maximum = "100")
    private Double max;

    @Valid()
    @NotNull()
    @MainImage()
    @UniqueFileIds()
    private List<InspectionListItemStageImage> images = List.of();
}
