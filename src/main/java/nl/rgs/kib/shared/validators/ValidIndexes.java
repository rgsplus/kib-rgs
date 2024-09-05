package nl.rgs.kib.shared.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating that the indexes of a list of objects are unique, starting from 0 and incrementing by 1.
 * The objects in the list must implement the Indexable interface.
 * <p>
 * Example usage:
 * <pre>
 * {@code @ValidIndexes
 * private List<IndexableObject> someObjects;}
 * </pre>
 * <p>
 * The ValidIndexesValidator will check if the indexes of the SomeObject objects in the list are unique.
 * <p>
 * If the list is null, the validation will pass.
 * <p>
 * If the list is empty, the validation will pass.
 * <p>
 * If the list contains objects with null indexes, the validation will fail.
 * <p>
 * If the list contains objects with duplicate indexes, the validation will fail.
 * <p>
 * If the list contains objects with indexes that start from 0 and increment by 1, the validation will pass.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidIndexesValidator.class)
public @interface ValidIndexes {
    String message() default "Indexes must be unique, starting from 0 and incrementing by 1";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}