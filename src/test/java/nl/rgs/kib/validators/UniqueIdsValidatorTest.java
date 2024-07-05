package nl.rgs.kib.validators;

import nl.rgs.kib.shared.models.Ideable;
import nl.rgs.kib.shared.validators.UniqueIdsValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UniqueIdsValidatorTest {
    private UniqueIdsValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UniqueIdsValidator();
    }

    @Test
    void testIsValidWithNullList() {
        assertTrue(validator.isValid(null, null));
    }

    @Test
    void testIsValidWithEmptyList() {
        assertTrue(validator.isValid(Collections.emptyList(), null));
    }

    @Test
    void testIsValidWithUniqueIds() {
        List<IdeableObject> items = List.of(
                new IdeableObject("id1"),
                new IdeableObject("id2")
        );

        assertTrue(validator.isValid(items, null));
    }

    @Test
    void testIsInvalidWithNonUniqueIds() {
        List<IdeableObject> items = List.of(
                new IdeableObject("id1"),
                new IdeableObject("id1")
        );

        assertFalse(validator.isValid(items, null));
    }

    @Test
    void testIsValidWithNullValue() {
        List<IdeableObject> items = List.of(
                new IdeableObject(null),
                new IdeableObject(null)
        );

        assertTrue(validator.isValid(items, null));
    }

    @Test
    void testIsValidWithNonIdeableObject() {
        List<?> items = List.of(new Object());
        assertFalse(validator.isValid(items, null));
    }

    public static class IdeableObject implements Ideable {
        private String id;

        public IdeableObject(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void setId(String id) {
            this.id = id;
        }
    }
}
