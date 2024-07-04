package nl.rgs.kib.shared.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueIndexesValidator.class)
public @interface UniqueIndexes {
    String message() default "Indexes are not unique";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}