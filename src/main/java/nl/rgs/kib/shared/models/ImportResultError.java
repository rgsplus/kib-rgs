package nl.rgs.kib.shared.models;

import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
public class ImportResultError {
    private String entity;
    private String field;
    private String message;
    private String value;

    public static <T> ImportResultError constraintViolationImplToImportResultError(ConstraintViolationImpl<T> violation) {
        ImportResultError importResultError = new ImportResultError();
        importResultError.setEntity(violation.getRootBeanClass() == null ? null : violation.getRootBeanClass().getName());
        importResultError.setField(violation.getPropertyPath() == null ? null : violation.getPropertyPath().toString());
        importResultError.setMessage(violation.getMessage());
        importResultError.setValue(violation.getInvalidValue() == null ? null : violation.getInvalidValue().toString());
        return importResultError;
    }

    public static <T> List<ImportResultError> constraintViolationsImplToImportResultsError(Set<ConstraintViolationImpl<T>> violations) {
        return violations.stream().map(ImportResultError::constraintViolationImplToImportResultError).toList();
    }

    public static <T> Set<ConstraintViolationImpl<T>> convertToImplSet(Set<ConstraintViolation<T>> violations) {
        Set<ConstraintViolationImpl<T>> implSet = new HashSet<>();
        for (ConstraintViolation<T> violation : violations) {
            if (violation instanceof ConstraintViolationImpl) {
                implSet.add((ConstraintViolationImpl<T>) violation);
            } else {
                throw new IllegalArgumentException("Violation is not of type ConstraintViolationImpl");
            }
        }
        return implSet;
    }
}
