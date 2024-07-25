package nl.rgs.kib.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.rgs.kib.model.list.InspectionListItemStageImage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueFileIdsValidator implements ConstraintValidator<UniqueFileIds, List<InspectionListItemStageImage>> {
    @Override
    public boolean isValid(List<InspectionListItemStageImage> items, ConstraintValidatorContext context) {
        if (items == null) {
            return true;
        }

        Set<String> uniqueIds = new HashSet<>();

        for (InspectionListItemStageImage item : items) {

            if (item.getFileId() == null) {
                continue;
            }

            if (!uniqueIds.add(item.getFileId())) {
                return false;
            }
        }
        return true;
    }
}