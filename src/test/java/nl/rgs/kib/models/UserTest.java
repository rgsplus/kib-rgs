package nl.rgs.kib.models;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Nested;

public class UserTest {
    @Nested
    public class UserTestValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        //TODO: Implement tests for User model
    }
}
