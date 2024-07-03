package nl.rgs.kib.validators;
import nl.rgs.kib.model.list.InspectionListItemValueStage;
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
        List<InspectionListItemValueStage> stages = List.of(
                new InspectionListItemValueStage(
                        0,
                        "Bad",
                        null
                ),
                new InspectionListItemValueStage(
                        1,
                        "Good",
                        null
                ),
                new InspectionListItemValueStage(
                        2,
                        "Excellent",
                        null
                ));
        assertTrue(validator.isValid(stages, null));
    }

    @Test
    void testIsValidWithNonUniqueStages() {
        List<InspectionListItemValueStage> stages = Arrays.asList(
                new InspectionListItemValueStage(
                        0,
                        "Bad",
                        null
                ),
                new InspectionListItemValueStage(
                        0,
                        "Good",
                        null
                ));
        assertFalse(validator.isValid(stages, null));
    }

    @Test
    void testIsValidWithNegativeStage() {
        List<InspectionListItemValueStage> stages = Collections.singletonList(
                new InspectionListItemValueStage(
                        -1,
                        "Bad",
                        null
                ));
        assertFalse(validator.isValid(stages, null));
    }

    @Test
    void testIsValidWithNullStage() {
        List<InspectionListItemValueStage> stages = Collections.singletonList(
                new InspectionListItemValueStage(null, "Bad", null));
        assertFalse(validator.isValid(stages, null));
    }
}