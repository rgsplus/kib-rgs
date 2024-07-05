package nl.rgs.kib.shared.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating that the indexes of a list of objects are unique.
 * The objects in the list must implement the Indexable interface.
 * <p>
 * Example usage:
 * <pre>
 * {@code @UniqueIndexes
 * private List<IndexableObject> someObjects;}
 * </pre>
 * <p>
 * The UniqueIndexesValidator will check if the indexes of the SomeObject objects in the list are unique.
 * <p>
 * If the list is null, the validation will pass.
 * <p>
 * If the list is empty, the validation will pass.
 * <p>
 * If the list contains objects with null indexes, the validation will pass.
 * <p>
 * If the list contains objects with unique indexes, the validation will pass.
 * <p>
 * If the list contains objects that do not implement the Indexable interface, the validation will fail.
 * <p>
 * If the list contains objects with duplicate indexes, the validation will fail.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueIndexesValidator.class)
public @interface UniqueIndexes {
    String message() default "Indexes must be unique";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}