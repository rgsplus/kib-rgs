package nl.rgs.kib.shared.validators;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating that the file ids of a list of objects are unique for InspectionListItemStageImage.
 * <p>
 * Example usage:
 * <pre>
 * {@code @UniqueFileIds
 * private List<InspectionListItemStageImage> images;}
 * </pre>
 * <p>
 * The UniqueFileIdsValidator will check if the file ids of the InspectionListItemStageImage list are unique.
 * <p>
 * If the list is null, the validation will pass.
 * <p>
 * If the list is empty, the validation will pass.
 * <p>
 * If the list contains objects with null file ids, the validation will pass.
 * <p>
 * If the list contains objects with unique file ids, the validation will pass.
 * <p>
 * If the list contains objects with duplicate file ids, the validation will fail.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueFileIdsValidator.class)
public @interface UniqueFileIds {
    String message() default "File ids must be unique";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}