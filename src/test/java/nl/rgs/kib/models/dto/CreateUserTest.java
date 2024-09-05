package nl.rgs.kib.models.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.rgs.kib.model.user.UserRole;
import nl.rgs.kib.model.user.dto.CreateUser;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateUserTest {
    @Nested
    public class CreateUserTestValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        @Test
        public void testUserFirstNameNotNullValidator() {
            CreateUser user = new CreateUser(
                    null,
                    "Doe",
                    "john.doe@gmail.com",
                    UserRole.USER, false
            );

            assertEquals(1, validator.validate(user).size(), "Firstname should not be null.");
        }

        @Test
        public void testUserFirstNameNotBlankValidator() {
            CreateUser user = new CreateUser(
                    "",
                    "Doe",
                    "john.doe@gmail.com",
                    UserRole.USER, false
            );

            assertEquals(1, validator.validate(user).size(), "Firstname should not be blank.");
        }

        @Test
        public void testUserEmailNotNullValidator() {
            CreateUser user = new CreateUser(
                    "John",
                    "Doe",
                    null,
                    UserRole.USER, false
            );

            assertEquals(1, validator.validate(user).size(), "Email should not be null.");
        }

        @Test
        public void testUserEmailNotBlankValidator() {
            CreateUser user = new CreateUser(
                    "John",
                    "Doe",
                    "",
                    UserRole.USER,
                    false
            );

            assertEquals(1, validator.validate(user).size(), "Email should not be blank.");
        }
    }
}
