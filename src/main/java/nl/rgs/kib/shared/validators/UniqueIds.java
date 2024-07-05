package nl.rgs.kib.shared.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating that the ids of a list of objects are unique.
 * The objects in the list must implement the Ideable interface.
 * <p>
 * Example usage:
 * <pre>
 * {@code @UniqueIds
 * private List<IdeableObject> someObjects;}
 * </pre>
 * <p>
 * The UniqueIdsValidator will check if the ids of the SomeObject objects in the list are unique.
 * <p>
 * If the list is null, the validation will pass.
 * <p>
 * If the list is empty, the validation will pass.
 * <p>
 * If the list contains objects with null ids, the validation will pass.
 * <p>
 * If the list contains objects with unique ids, the validation will pass.
 * <p>
 * If the list contains objects that do not implement the Ideable interface, the validation will fail.
 * <p>
 * If the list contains objects with duplicate ids, the validation will fail.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueIdsValidator.class)
public @interface UniqueIds {
    String message() default "Ids must be unique";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}