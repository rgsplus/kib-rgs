package nl.rgs.kib.models;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.rgs.kib.model.account.ApiAccount;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiAccountTest {

    @Nested
    public class ApiAccountValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        @Test
        public void testApiAccountIdNotNullValidator() {
            ApiAccount apiAccount = new ApiAccount();
            apiAccount.setId(null);
            apiAccount.setApiKey("e0e4fe66-5114-4bd8-83f7-28c4bd3461a6");
            apiAccount.setName("test");
            apiAccount.setBusinessName("Facebook");
            apiAccount.setActive(true);
            apiAccount.setStartDate(new Date());
            apiAccount.setEndDate(null);

            assertEquals(1, validator.validate(apiAccount).size(), "Id should not be null.");
        }

        @Test
        public void testApiAccountApiKeyNotNullValidator() {
            ApiAccount apiAccount = new ApiAccount();
            apiAccount.setId("5f622c23a8efb61a54365f33");
            apiAccount.setApiKey(null);
            apiAccount.setName("test");
            apiAccount.setBusinessName("Facebook");
            apiAccount.setActive(true);
            apiAccount.setStartDate(new Date());
            apiAccount.setEndDate(null);

            assertEquals(1, validator.validate(apiAccount).size(), "ApiKey should not be null.");
        }

        @Test
        public void testApiAccountApiKeyNotBlankValidator() {
            ApiAccount apiAccount = new ApiAccount();
            apiAccount.setId("5f622c23a8efb61a54365f33");
            apiAccount.setApiKey("");
            apiAccount.setName("test");
            apiAccount.setBusinessName("Facebook");
            apiAccount.setActive(true);
            apiAccount.setStartDate(new Date());
            apiAccount.setEndDate(null);

            assertEquals(1, validator.validate(apiAccount).size(), "ApiKey should not be blank.");
        }

        @Test
        public void testApiAccountNameNotNullValidator() {
            ApiAccount apiAccount = new ApiAccount();
            apiAccount.setId("5f622c23a8efb61a54365f33");
            apiAccount.setApiKey("e0e4fe66-5114-4bd8-83f7-28c4bd3461a6");
            apiAccount.setName(null);
            apiAccount.setBusinessName("Facebook");
            apiAccount.setActive(true);
            apiAccount.setStartDate(new Date());
            apiAccount.setEndDate(null);

            assertEquals(1, validator.validate(apiAccount).size(), "Name should not be null.");
        }

        @Test
        public void testApiAccountNameNotBlankValidator() {
            ApiAccount apiAccount = new ApiAccount();
            apiAccount.setId("5f622c23a8efb61a54365f33");
            apiAccount.setApiKey("e0e4fe66-5114-4bd8-83f7-28c4bd3461a6");
            apiAccount.setName("");
            apiAccount.setBusinessName("Facebook");
            apiAccount.setActive(true);
            apiAccount.setStartDate(new Date());
            apiAccount.setEndDate(null);

            assertEquals(1, validator.validate(apiAccount).size(), "Name should not be blank.");
        }

        @Test
        public void testApiAccountBusinessNameNotNullValidator() {
            ApiAccount apiAccount = new ApiAccount();
            apiAccount.setId("5f622c23a8efb61a54365f33");
            apiAccount.setApiKey("e0e4fe66-5114-4bd8-83f7-28c4bd3461a6");
            apiAccount.setName("test");
            apiAccount.setBusinessName(null);
            apiAccount.setActive(true);
            apiAccount.setStartDate(new Date());
            apiAccount.setEndDate(null);

            assertEquals(1, validator.validate(apiAccount).size(), "BusinessName should not be null.");
        }

        @Test
        public void testApiAccountBusinessNameNotBlankValidator() {
            ApiAccount apiAccount = new ApiAccount();
            apiAccount.setId("5f622c23a8efb61a54365f33");
            apiAccount.setApiKey("e0e4fe66-5114-4bd8-83f7-28c4bd3461a6");
            apiAccount.setName("test");
            apiAccount.setBusinessName("");
            apiAccount.setActive(true);
            apiAccount.setStartDate(new Date());
            apiAccount.setEndDate(null);

            assertEquals(1, validator.validate(apiAccount).size(), "BusinessName should not be blank.");
        }

        @Test
        public void testApiAccountStartDateNotNullValidator() {
            ApiAccount apiAccount = new ApiAccount();
            apiAccount.setId("5f622c23a8efb61a54365f33");
            apiAccount.setApiKey("e0e4fe66-5114-4bd8-83f7-28c4bd3461a6");
            apiAccount.setName("test");
            apiAccount.setBusinessName("Facebook");
            apiAccount.setActive(true);
            apiAccount.setStartDate(null);
            apiAccount.setEndDate(null);

            assertEquals(1, validator.validate(apiAccount).size(), "StartDate should not be null.");
        }

        @Test
        public void testApiAccountActiveNotNullValidator() {
            ApiAccount apiAccount = new ApiAccount();
            apiAccount.setId("5f622c23a8efb61a54365f33");
            apiAccount.setApiKey("e0e4fe66-5114-4bd8-83f7-28c4bd3461a6");
            apiAccount.setName("test");
            apiAccount.setBusinessName("Facebook");
            apiAccount.setActive(null);
            apiAccount.setStartDate(new Date());
            apiAccount.setEndDate(null);

            assertEquals(1, validator.validate(apiAccount).size(), "Active should not be null.");
        }

        @Test
        public void testApiAccountStartDateBeforeEndDateValidator() {
            ApiAccount apiAccount = new ApiAccount();
            apiAccount.setId("5f622c23a8efb61a54365f33");
            apiAccount.setApiKey("e0e4fe66-5114-4bd8-83f7-28c4bd3461a6");
            apiAccount.setName("test");
            apiAccount.setBusinessName("Facebook");
            apiAccount.setActive(true);
            apiAccount.setStartDate(new Date());
            apiAccount.setEndDate(new Date(0));

            assertEquals(1, validator.validate(apiAccount).size(), "StartDate should be before EndDate.");
        }
    }
}
