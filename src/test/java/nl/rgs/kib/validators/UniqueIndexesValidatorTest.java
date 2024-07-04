package nl.rgs.kib.validators;
import nl.rgs.kib.shared.models.Indexable;
import nl.rgs.kib.shared.validators.UniqueIndexesValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UniqueIndexesValidatorTest {

    private UniqueIndexesValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UniqueIndexesValidator();
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
    void testIsValidWithUniqueValidIndexes() {
        List<Indexable> items = List.of(new IndexableObject(0), new IndexableObject(1), new IndexableObject(2));
        assertTrue(validator.isValid(items, null));
    }

    @Test
    void testIsValidWithNonUniqueIndexes() {
        List<Indexable> items = List.of(new IndexableObject(0), new IndexableObject(0));
        assertFalse(validator.isValid(items, null));
    }

    @Test
    void testIsValidWithNonIndexableObject() {
        List<?> items = List.of(new Object());
        assertFalse(validator.isValid(items, null));
    }

    public static class IndexableObject implements Indexable {
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