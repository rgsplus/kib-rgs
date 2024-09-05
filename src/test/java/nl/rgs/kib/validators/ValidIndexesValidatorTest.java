package nl.rgs.kib.validators;

import nl.rgs.kib.shared.models.Indexable;
import nl.rgs.kib.shared.validators.ValidIndexesValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidIndexesValidatorTest {

    private ValidIndexesValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new ValidIndexesValidator();
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
    public void testIsValidWithUniqueValidIndexes() {
        List<Indexable> items = List.of(new IndexableObject(0), new IndexableObject(1), new IndexableObject(2));
        assertTrue(validator.isValid(items, null));
    }

    @Test
    public void testIsInvalidWithNonValidIndexes() {
        List<Indexable> items = List.of(new IndexableObject(0), new IndexableObject(0));
        assertFalse(validator.isValid(items, null));
    }

    @Test
    public void testIsInvalidWithNullValue() {
        List<Indexable> items = List.of(
                new IndexableObject(null),
                new IndexableObject(null)
        );

        assertFalse(validator.isValid(items, null));
    }

    @Test
    public void testIsInvalidWithNonSequentialIndexes() {
        List<Indexable> items = List.of(
                new IndexableObject(0),
                new IndexableObject(2)
        );

        assertFalse(validator.isValid(items, null));
    }

    @Test
    public void testIsInvalidWithStartIndexGreaterThanZero() {
        List<Indexable> items = List.of(
                new IndexableObject(1),
                new IndexableObject(2)
        );

        assertFalse(validator.isValid(items, null));
    }

    @Test
    public void testIsValidWithNonIndexableObject() {
        List<?> items = List.of(new Object());
        assertFalse(validator.isValid(items, null));
    }

    private static class IndexableObject implements Indexable {
        private Integer index;

        public IndexableObject(Integer index) {
            this.index = index;
        }

        @Override
        public Integer getIndex() {
            return index;
        }

        @Override
        public void setIndex(Integer index) {
            this.index = index;
        }
    }
}