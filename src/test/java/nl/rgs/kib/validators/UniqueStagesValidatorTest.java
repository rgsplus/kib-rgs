package nl.rgs.kib.validators;

import nl.rgs.kib.shared.models.Stageable;
import nl.rgs.kib.shared.validators.UniqueStagesValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UniqueStagesValidatorTest {

    private UniqueStagesValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new UniqueStagesValidator();
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
    public void testIsValidWithUniqueValidStages() {
        List<StageableObject> stages = List.of(
                new StageableObject(0),
                new StageableObject(1),
                new StageableObject(2)
        );
        assertTrue(validator.isValid(stages, null));
    }

    @Test
    public void testIsInvalidWithNonUniqueStages() {
        List<StageableObject> stages = Arrays.asList(
                new StageableObject(0),
                new StageableObject(0)
        );
        assertFalse(validator.isValid(stages, null));
    }

    @Test
    public void testIsValidWithNullValue() {
        List<StageableObject> items = List.of(
                new StageableObject(null),
                new StageableObject(null)
        );

        assertTrue(validator.isValid(items, null));
    }

    @Test
    public void testIsValidWithNonStageableObject() {
        List<?> items = List.of(new Object());
        assertFalse(validator.isValid(items, null));
    }

    private static class StageableObject implements Stageable {
        private Integer stage;

        public StageableObject(Integer stage) {
            this.stage = stage;
        }

        @Override
        public Integer getStage() {
            return stage;
        }

        @Override
        public void setStage(Integer stage) {
            this.stage = stage;
        }
    }
}