package nl.rgs.kib.shared.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating that the main image of a list of InspectionListItemStageImage objects is unique.
 * <p>
 * Example usage:
 * <pre>
 * {@code @MainImage
 * private List<InspectionListItemStageImage> images;}
 * </pre>
 * <p>
 * This validator checks that there is exactly one main image in a list of InspectionListItemStageImage objects.
 * <p>
 * If the list is null, the validation will pass.
 * <p>
 * If the list is empty, the validation will pass.
 * <p>
 * If the list contains exactly one object with a true main flag, the validation will pass.
 * <p>
 * If the list contains more than one object with a true main flag, the validation will fail.
 * <p>
 * If the list contains no objects with a true main flag, the validation will fail.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MainImageValidator.class)
public @interface MainImage {
    String message() default "Main image must be unique and not null";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}