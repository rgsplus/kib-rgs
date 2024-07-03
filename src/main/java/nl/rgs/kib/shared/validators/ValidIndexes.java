package nl.rgs.kib.shared.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidIndexesValidator.class)
public @interface ValidIndexes {
    String message() default "Indexes are not valid";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}