package nl.rgs.kib.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.rgs.kib.model.list.InspectionListItemStageImage;

import java.util.ArrayList;
import java.util.List;

public class MainImageValidator implements ConstraintValidator<MainImage, List<InspectionListItemStageImage>> {
    @Override
    public boolean isValid(List<InspectionListItemStageImage> items, ConstraintValidatorContext context) {
        if (items == null || items.isEmpty()) {
            return true;
        }

        List<InspectionListItemStageImage> mainImages = new ArrayList<>();

        for (InspectionListItemStageImage inspectionListItemStageImage : items) {
            if (inspectionListItemStageImage.getMain() == null) {
                continue;
            }
            if (inspectionListItemStageImage.getMain()) {
                mainImages.add(inspectionListItemStageImage);
            }
        }

        return mainImages.size() == 1;
    }
}