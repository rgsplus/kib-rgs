package nl.rgs.kib.shared.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for validating that the standardNos of a list of InspectionListItems are unique.
 * <p>
 * Example usage:
 * <pre>
 * {@code @UniqueStandardNos
 * private List<InspectionListItem> items;}
 * </pre>
 * <p>
 * The UniqueStandardNoValidator will check if the standardNos of the InspectionListItem list are unique.
 * <p>
 * If the list is null, the validation will pass.
 * <p>
 * If the list is empty, the validation will pass.
 * <p>
 * If the list contains objects with unique standardNos, the validation will pass.
 * <p>
 * If the list contains objects with duplicate standardNos, the validation will fail.
 * <p>
 */
@Constraint(validatedBy = UniqueStandardNosValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueStandardNos {
    String message() default "Duplicate standardNo found in InspectionList";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}