package nl.rgs.kib.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.rgs.kib.model.list.InspectionListItemValueStage;
import nl.rgs.kib.shared.models.Indexable;
import nl.rgs.kib.shared.models.Stageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidStagesValidator implements ConstraintValidator<ValidStages, List<?>> {
    @Override
    public boolean isValid(List<?> stages, ConstraintValidatorContext context) {
        if (stages == null) {
            return true;
        }

        Set<Integer> uniqueStages = new HashSet<>();

        for (Object item : stages) {
            if (!(item instanceof Stageable stage)) {
                return false;
            }

            if (stage.getStage() == null || stage.getStage() < 0 || !uniqueStages.add(stage.getStage())) {
                return false;
            }
        }

        return true;
    }
}