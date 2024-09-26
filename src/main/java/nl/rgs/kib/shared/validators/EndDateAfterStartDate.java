package nl.rgs.kib.shared.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating that the end date is after the start date of objects with a startDate and endDate fields as Date objects.
 * <p>
 * This validator checks that the end date of an object is after the start date.
 * <p>
 * If the endDate or startDate is null, the validation will pass.
 * <p>
 * If the endDate is after the startDate, the validation will pass.
 * <p>
 * If the endDate is before the startDate, the validation will fail.
 * <p>
 * If the endDate is equal to the startDate, the validation will fail.
 */
@Constraint(validatedBy = EndDateAfterStartDateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EndDateAfterStartDate {
    String message() default "End date must be after start date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}