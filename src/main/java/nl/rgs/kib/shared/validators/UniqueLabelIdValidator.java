package nl.rgs.kib.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.rgs.kib.model.list.InspectionListLabel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueLabelIdValidator implements ConstraintValidator<UniqueLabelIds, List<InspectionListLabel>> {
    @Override
    public boolean isValid(List<InspectionListLabel> labels, ConstraintValidatorContext context) {
        if (labels == null) {
            return true;
        }
        Set<String> uniqueIds = new HashSet<>();
        for (InspectionListLabel label : labels) {
            if (!uniqueIds.add(label.getId())) {
                return false;
            }
        }
        return true;
    }
}