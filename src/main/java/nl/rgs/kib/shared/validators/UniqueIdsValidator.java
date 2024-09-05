package nl.rgs.kib.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.rgs.kib.shared.models.Ideable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueIdsValidator implements ConstraintValidator<UniqueIds, List<?>> {
    @Override
    public boolean isValid(List<?> items, ConstraintValidatorContext context) {
        if (items == null) {
            return true;
        }

        Set<String> uniqueIds = new HashSet<>();

        for (Object item : items) {
            if (!(item instanceof Ideable ideable)) {
                return false;
            }

            if (ideable.getId() == null) {
                continue;
            }

            if (!uniqueIds.add(ideable.getId())) {
                return false;
            }
        }
        return true;
    }
}