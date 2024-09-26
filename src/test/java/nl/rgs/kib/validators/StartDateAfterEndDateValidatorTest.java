package nl.rgs.kib.validators;

import nl.rgs.kib.model.account.ApiAccount;
import nl.rgs.kib.shared.validators.EndDateAfterStartDateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StartDateAfterEndDateValidatorTest {

    private EndDateAfterStartDateValidator validator;
    private ZoneId defaultZoneId;

    @BeforeEach
    public void setUp() {
        validator = new EndDateAfterStartDateValidator();
        defaultZoneId = ZoneId.systemDefault();
    }

    @Test
    public void testIsValidWithNullStartDate() {
        ApiAccount account = new ApiAccount();
        account.setStartDate(null);
        account.setEndDate(new Date());

        assertTrue(validator.isValid(account, null));
    }

    @Test
    public void testIsValidWithNullEndDate() {
        ApiAccount account = new ApiAccount();
        account.setStartDate(new Date());
        account.setEndDate(null);

        assertTrue(validator.isValid(account, null));
    }

    @Test
    public void testIsValidWithStartDateAfterEndDate() {
        ApiAccount account = new ApiAccount();

        account.setStartDate(Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(defaultZoneId).toInstant()));
        account.setEndDate(Date.from(LocalDate.of(2001, 1, 1).atStartOfDay(defaultZoneId).toInstant()));

        assertTrue(validator.isValid(account, null));
    }

    @Test
    public void testIsInvalidWithStartDateBeforeEndDate() {
        ApiAccount account = new ApiAccount();

        account.setStartDate(Date.from(LocalDate.of(2001, 1, 1).atStartOfDay(defaultZoneId).toInstant()));
        account.setEndDate(Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(defaultZoneId).toInstant()));

        assertFalse(validator.isValid(account, null));
    }

    @Test
    public void testIsInValidWithStartDateEqualToEndDate() {
        ApiAccount account = new ApiAccount();

        account.setStartDate(Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(defaultZoneId).toInstant()));
        account.setEndDate(Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(defaultZoneId).toInstant()));

        assertFalse(validator.isValid(account, null));
    }

    @Test
    public void testIsInvalidWithNoStartDateOrEndDateFields() {
        Object object = new Object();

        assertFalse(validator.isValid(object, null));
    }
}
