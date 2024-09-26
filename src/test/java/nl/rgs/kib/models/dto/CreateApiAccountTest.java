package nl.rgs.kib.models.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.rgs.kib.model.account.dto.CreateApiAccount;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateApiAccountTest {
    @Nested
    public class CreateApiAccountValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        @Test
        public void testCreateApiAccountNameNotNullValidator() {
            CreateApiAccount createApiAccount = new CreateApiAccount(
                    null,
                    "Api account for testing",
                    "Facebook",
                    new Date(),
                    null,
                    true
            );

            assertEquals(1, validator.validate(createApiAccount).size(), "Name should not be null.");
        }

        @Test
        public void testCreateApiAccountNameNotBlankValidator() {
            CreateApiAccount createApiAccount = new CreateApiAccount(
                    "",
                    "Api account for testing",
                    "Facebook",
                    new Date(),
                    null,
                    true
            );

            assertEquals(1, validator.validate(createApiAccount).size(), "Name should not be blank.");
        }

        @Test
        public void testCreateApiAccountBusinessNameNotNullValidator() {
            CreateApiAccount createApiAccount = new CreateApiAccount(
                    "test",
                    "Api account for testing",
                    null,
                    new Date(),
                    null,
                    true
            );

            assertEquals(1, validator.validate(createApiAccount).size(), "Business name should not be null.");
        }

        @Test
        public void testCreateApiAccountBusinessNameNotBlankValidator() {
            CreateApiAccount createApiAccount = new CreateApiAccount(
                    "test",
                    "Api account for testing",
                    "",
                    new Date(),
                    null,
                    true
            );

            assertEquals(1, validator.validate(createApiAccount).size(), "Business name should not be blank.");
        }

        @Test
        public void testCreateApiAccountStartDateNotNullValidator() {
            CreateApiAccount createApiAccount = new CreateApiAccount(
                    "test",
                    "Api account for testing",
                    "Facebook",
                    null,
                    null,
                    true
            );

            assertEquals(1, validator.validate(createApiAccount).size(), "Start date should not be null.");
        }

        @Test
        public void testCreateApiAccountActiveNotNullValidator() {
            CreateApiAccount createApiAccount = new CreateApiAccount(
                    "test",
                    "Api account for testing",
                    "Facebook",
                    new Date(),
                    null,
                    null
            );

            assertEquals(1, validator.validate(createApiAccount).size(), "Active should not be null.");
        }

        @Test
        public void testCreateApiAccountStartDateBeforeEndDateValidator() {
            CreateApiAccount createApiAccount = new CreateApiAccount(
                    "test",
                    "Api account for testing",
                    "Facebook",
                    new Date(),
                    new Date(0),
                    true
            );

            assertEquals(1, validator.validate(createApiAccount).size(), "Start date should be before end date.");
        }
    }
}
