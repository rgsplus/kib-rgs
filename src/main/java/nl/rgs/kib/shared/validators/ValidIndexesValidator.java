package nl.rgs.kib.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Data;
import nl.rgs.kib.model.list.InspectionListItem;
import nl.rgs.kib.model.list.InspectionListItemValue;
import nl.rgs.kib.shared.models.Indexable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidIndexesValidator implements ConstraintValidator<ValidIndexes, List<?>> {
    @Override
    public boolean isValid(List<?> items, ConstraintValidatorContext context) {
        if (items == null) {
            return true;
        }

        Set<Integer> uniqueIndexes = new HashSet<>();

        for (Object item : items) {
            if (!(item instanceof Indexable indexableItem)) {
                return false;
            }

            Integer index = indexableItem.getIndex();

            if (index == null || index < 0 || !uniqueIndexes.add(index)) {
                return false;
            }
        }

        return true;
    }
}