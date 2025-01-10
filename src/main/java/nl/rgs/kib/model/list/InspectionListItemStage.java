package nl.rgs.kib.model.list;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.rgs.kib.model.method.InspectionMethodStage;
import nl.rgs.kib.shared.validators.MainImage;
import nl.rgs.kib.shared.validators.UniqueFileIds;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class InspectionListItemStage extends InspectionMethodStage {
    @Valid
    @NotNull
    @MainImage
    @UniqueFileIds
    private List<InspectionListItemStageImage> images = new ArrayList<>();
}
