package nl.rgs.kib.shared.validators;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidStagesValidator.class)
public @interface ValidStages {
    String message() default "Invalid stages";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}