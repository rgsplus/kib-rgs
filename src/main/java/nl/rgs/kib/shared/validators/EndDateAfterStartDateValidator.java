package nl.rgs.kib.shared.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.Date;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, Object> {
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            Field startDateField = obj.getClass().getDeclaredField("startDate");
            Field endDateField = obj.getClass().getDeclaredField("endDate");

            startDateField.setAccessible(true);
            endDateField.setAccessible(true);

            Date startDate = (Date) startDateField.get(obj);
            Date endDate = (Date) endDateField.get(obj);

            if (startDate == null || endDate == null) {
                return true;
            }

            return endDate.after(startDate);
        } catch (Exception e) {
            return false;
        }
    }
}