package nl.rgs.kib.shared.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating that the stages of a list of objects are unique.
 * The objects in the list must implement the Stageable interface.
 * <p>
 * Example usage:
 * <pre>
 * {@code @UniqueStages
 * private List<StageableObject> someObjects;}
 * </pre>
 * The UniqueStagesValidator will check if the stages of the SomeObject objects in the list are unique.
 * <p>
 * If the list is null, the validation will pass.
 * <p>
 * If the list is empty, the validation will pass.
 * <p>
 * If the list contains objects with null stages, the validation will pass.
 * <p>
 * If the list contains objects with unique stages, the validation will pass.
 * <p>
 * If the list contains objects that do not implement the Stageable interface, the validation will fail.
 * <p>
 * If the list contains objects with duplicate stages, the validation will fail.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueStagesValidator.class)
public @interface UniqueStages {
    String message() default "Stages must be unique";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}