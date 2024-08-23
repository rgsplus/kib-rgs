package nl.rgs.kib.models;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.rgs.kib.model.user.User;
import nl.rgs.kib.model.user.UserRole;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    @Nested
    public class UserTestValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        @Test
        public void testUserIdNotNullValidator() {
            User user = new User();
            user.setId(null);
            user.setEmail("john.doe@gmail.com");
            user.setUsername("john.doe");
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setRole(UserRole.USER);

            assertEquals(1, validator.validate(user).size(), "Id should not be null.");
        }

        @Test
        public void testUserIdNotBlankValidator() {
            User user = new User();
            user.setId("");
            user.setEmail("john.doe@gmail.com");
            user.setUsername("john.doe");
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setRole(UserRole.USER);

            assertEquals(1, validator.validate(user).size(), "Id should not be blank.");
        }

        @Test
        public void testUserUsernameNotNullValidator() {
            User user = new User();
            user.setId(new ObjectId().toHexString());
            user.setEmail("john.doe@gmail.com");
            user.setUsername(null);
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setRole(UserRole.USER);

            assertEquals(1, validator.validate(user).size(), "Username should not be null.");
        }

        @Test
        public void testUserUsernameNotBlankValidator() {
            User user = new User();
            user.setId(new ObjectId().toHexString());
            user.setEmail("john.doe@gmail.com");
            user.setUsername("");
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setRole(UserRole.USER);

            assertEquals(1, validator.validate(user).size(), "Username should not be blank.");
        }

        @Test
        public void testUserFirstNameNotNullValidator() {
            User user = new User();
            user.setId(new ObjectId().toHexString());
            user.setEmail("john.doe@gmail.com");
            user.setUsername("john.doe");
            user.setFirstName(null);
            user.setLastName("Doe");
            user.setRole(UserRole.USER);

            assertEquals(1, validator.validate(user).size(), "Firstname should not be null.");
        }

        @Test
        public void testUserFirstNameNotBlankValidator() {
            User user = new User();
            user.setId(new ObjectId().toHexString());
            user.setEmail("john.doe@gmail.com");
            user.setUsername("john.doe");
            user.setFirstName("");
            user.setLastName("Doe");
            user.setRole(UserRole.USER);

            assertEquals(1, validator.validate(user).size(), "Firstname should not be blank.");
        }

        @Test
        public void testUserEmailNotNullValidator() {
            User user = new User();
            user.setId(new ObjectId().toHexString());
            user.setEmail(null);
            user.setUsername("john.doe");
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setRole(UserRole.USER);

            assertEquals(1, validator.validate(user).size(), "Email should not be null.");
        }

        @Test
        public void testUserEmailNotBlankValidator() {
            User user = new User();
            user.setId(new ObjectId().toHexString());
            user.setEmail("");
            user.setUsername("john.doe");
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setRole(UserRole.USER);

            assertEquals(1, validator.validate(user).size(), "Email should not be blank.");
        }
    }
}
