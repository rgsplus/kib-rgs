package nl.rgs.kib.validators;

import nl.rgs.kib.shared.models.StandarNoable;
import nl.rgs.kib.shared.validators.UniqueStandardNosValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UniqueStandardNosValidatorTest {

    private UniqueStandardNosValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new UniqueStandardNosValidator();
    }

    @Test
    public void testIsValidWithNullList() {
        assertTrue(validator.isValid(null, null));
    }

    @Test
    public void testIsValidWithEmptyList() {
        assertTrue(validator.isValid(Collections.emptyList(), null));
    }


    @Test
    public void testIsValidWithUniqueStandardNos() {
        List<StandardNoObject> items = List.of(
                new StandardNoObject("id1"),
                new StandardNoObject("id2")
        );

        assertTrue(validator.isValid(items, null));
    }

    @Test
    public void testIsInvalidWithNonUniqueIds() {
        List<StandardNoObject> items = List.of(
                new StandardNoObject("id1"),
                new StandardNoObject("id1")
        );

        assertFalse(validator.isValid(items, null));
    }

    private static class StandardNoObject implements StandarNoable {
        private String standardNo;

        public StandardNoObject(String standardNo) {
            this.standardNo = standardNo;
        }

        @Override
        public String getStandardNo() {
            return standardNo;
        }

        @Override
        public void setStandardNo(String stage) {
            this.standardNo = stage;
        }
    }
}
