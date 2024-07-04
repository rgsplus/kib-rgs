package nl.rgs.kib.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.rgs.kib.shared.models.Indexable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueIndexesValidator implements ConstraintValidator<UniqueIndexes, List<?>> {
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

            if (!uniqueIndexes.add(indexableItem.getIndex())) {
                return false;
            }
        }

        return true;
    }
}