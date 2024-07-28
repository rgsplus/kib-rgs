package nl.rgs.kib.models.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Nested;

public class CreateUserTest {
    @Nested
    public class CreateUserTestValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        //TODO: Implement tests for Create User model
    }
}
