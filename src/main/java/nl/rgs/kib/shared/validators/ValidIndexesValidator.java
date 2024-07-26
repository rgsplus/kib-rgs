package nl.rgs.kib.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
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

            if (indexableItem.getIndex() == null) {
                return false;
            }

            if (!uniqueIndexes.add(indexableItem.getIndex())) {
                return false;
            }
        }

        for (int i = 0; i < uniqueIndexes.size(); i++) {
            if (!uniqueIndexes.contains(i)) {
                return false;
            }
        }

        return true;
    }
}