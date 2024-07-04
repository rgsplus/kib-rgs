package nl.rgs.kib.validators;
import nl.rgs.kib.shared.models.Stageable;
import nl.rgs.kib.shared.validators.ValidStagesValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ValidStagesValidatorTest {

    private ValidStagesValidator validator;

    @BeforeEach
    void setUp() {
        validator = new ValidStagesValidator();
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
    void testIsValidWithUniqueValidStages() {
        List<StageableObject> stages = List.of(
                new StageableObject(0),
                new StageableObject(1),
                new StageableObject(2)
                );
        assertTrue(validator.isValid(stages, null));
    }

    @Test
    void testIsValidWithNonUniqueStages() {
        List<StageableObject> stages = Arrays.asList(
                new StageableObject(0),
                new StageableObject(0)
        );
        assertFalse(validator.isValid(stages, null));
    }

    @Test
    void testIsValidWithNegativeStage() {
        List<StageableObject> stages = Collections.singletonList(new StageableObject(-1));
        assertFalse(validator.isValid(stages, null));
    }

    @Test
    void testIsValidWithNullStage() {
        List<StageableObject> stages = Collections.singletonList(
                new StageableObject(null)
        );
        assertFalse(validator.isValid(stages, null));
    }

    public static class StageableObject implements Stageable {
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