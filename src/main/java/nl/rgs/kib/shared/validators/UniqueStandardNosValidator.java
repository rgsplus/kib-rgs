package nl.rgs.kib.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.rgs.kib.shared.models.StandarNoable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueStandardNosValidator implements ConstraintValidator<UniqueStandardNos, List<?>> {
    @Override
    public boolean isValid(List<?> items, ConstraintValidatorContext context) {
        if (items == null) {
            return true;
        }

        Set<String> standardNos = new HashSet<>();
        for (Object item : items) {
            if (!(item instanceof StandarNoable standarNoable)) {
                return false;
            }

            if (!standardNos.add(standarNoable.getStandardNo())) {
                return false;
            }
        }
        return true;
    }
}