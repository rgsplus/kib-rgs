package nl.rgs.kib.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.rgs.kib.model.list.InspectionListItemValueStage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidStagesValidator implements ConstraintValidator<ValidStages, List<InspectionListItemValueStage>> {
    @Override
    public boolean isValid(List<InspectionListItemValueStage> stages, ConstraintValidatorContext context) {
        if (stages == null) {
            return true;
        }

        Set<Integer> uniqueStages = new HashSet<>();

        for (InspectionListItemValueStage stage : stages) {
            if (stage.getStage() == null || stage.getStage() < 0 || !uniqueStages.add(stage.getStage())) {
                return false;
            }
        }

        return true;
    }
}