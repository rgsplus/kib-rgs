package nl.rgs.kib.models.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.rgs.kib.model.user.dto.CreateUser;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateUserTest {
    @Nested
    public class CreateUserTestValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        @Test
        public void testUserUsernameNotNullValidator() {
            CreateUser user = new CreateUser(
                    null,
                    "John",
                    "Doe",
                    "john.doe@gmail.com",
                    new HashMap<>(),
                    new ArrayList<>(),
                    new HashMap<>()
            );

            assertEquals(1, validator.validate(user).size(), "Username should not be null.");
        }

        @Test
        public void testUserUsernameNotBlankValidator() {
            CreateUser user = new CreateUser(
                    "",
                    "John",
                    "Doe",
                    "john.doe@gmail.com",
                    new HashMap<>(),
                    new ArrayList<>(),
                    new HashMap<>()
            );

            assertEquals(1, validator.validate(user).size(), "Username should not be blank.");
        }

        @Test
        public void testUserFirstNameNotNullValidator() {
            CreateUser user = new CreateUser(
                    "john.doe",
                    null,
                    "Doe",
                    "john.doe@gmail.com",
                    new HashMap<>(),
                    new ArrayList<>(),
                    new HashMap<>()
            );

            assertEquals(1, validator.validate(user).size(), "Firstname should not be null.");
        }

        @Test
        public void testUserFirstNameNotBlankValidator() {
            CreateUser user = new CreateUser(
                    "john.doe",
                    "",
                    "Doe",
                    "john.doe@gmail.com",
                    new HashMap<>(),
                    new ArrayList<>(),
                    new HashMap<>()
            );

            assertEquals(1, validator.validate(user).size(), "Firstname should not be blank.");
        }

        @Test
        public void testUserEmailNotNullValidator() {
            CreateUser user = new CreateUser(
                    "john.doe",
                    "John",
                    "Doe",
                    null,
                    new HashMap<>(),
                    new ArrayList<>(),
                    new HashMap<>()
            );

            assertEquals(1, validator.validate(user).size(), "Email should not be null.");
        }

        @Test
        public void testUserEmailNotBlankValidator() {
            CreateUser user = new CreateUser(
                    "john.doe",
                    "John",
                    "Doe",
                    "",
                    new HashMap<>(),
                    new ArrayList<>(),
                    new HashMap<>()
            );

            assertEquals(1, validator.validate(user).size(), "Email should not be blank.");
        }
    }
}
