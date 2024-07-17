package nl.rgs.kib.models.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.rgs.kib.model.method.InspectionMethodCalculationMethod;
import nl.rgs.kib.model.method.InspectionMethodInput;
import nl.rgs.kib.model.method.InspectionMethodStage;
import nl.rgs.kib.model.method.dto.CreateInspectionMethod;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateInspectionMethodTest {
    @Nested
    public class CreateInspectionMethodValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        @Test
        public void testCreateInspectionMethodNameNotNullValidator() {
            CreateInspectionMethod inspectionMethod = new CreateInspectionMethod(
                    null,
                    InspectionMethodInput.PERCENTAGE,
                    InspectionMethodCalculationMethod.NEN2767,
                    new ArrayList<>()
            );
            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod name is required.");
        }

        @Test
        public void testCreateInspectionMethodNameNotBlankValidator() {
            CreateInspectionMethod inspectionMethod = new CreateInspectionMethod(
                    "",
                    InspectionMethodInput.PERCENTAGE,
                    InspectionMethodCalculationMethod.NEN2767,
                    new ArrayList<>()
            );

            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod name is required.");
        }

        @Test
        public void testCreateInspectionMethodInputNotNullValidator() {
            CreateInspectionMethod inspectionMethod = new CreateInspectionMethod(
                    "Test",
                    null,
                    InspectionMethodCalculationMethod.NEN2767,
                    new ArrayList<>()
            );

            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod input is required.");
        }

        @Test
        public void testCreateInspectionMethodCalculationMethodNotNullValidator() {
            CreateInspectionMethod inspectionMethod = new CreateInspectionMethod(
                    "Test",
                    InspectionMethodInput.PERCENTAGE,
                    null,
                    new ArrayList<>()
            );

            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod calculation method is required.");
        }

        @Test
        public void testCreateInspectionMethodStagesNotNullValidator() {
            CreateInspectionMethod inspectionMethod = new CreateInspectionMethod(
                    "Test",
                    InspectionMethodInput.PERCENTAGE,
                    InspectionMethodCalculationMethod.NEN2767,
                    null
            );

            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stages are required.");
        }

        @Test
        public void testCreateInspectionMethodUniqueStagesValidator() {
            InspectionMethodStage stage1 = new InspectionMethodStage(
                    1,
                    "Test 1"
            );
            InspectionMethodStage stage2 = new InspectionMethodStage(
                    1,
                    "Test 2"
            );

            CreateInspectionMethod inspectionMethod = new CreateInspectionMethod(
                    "Test",
                    InspectionMethodInput.PERCENTAGE,
                    InspectionMethodCalculationMethod.NEN2767,
                    List.of(stage1, stage2)
            );

            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stages must be unique.");
        }

        @Nested
        public class CreateInspectionMethodStageValidations {
            //TODO: Add tests for InspectionMethodStage validations

            @Test
            public void testInspectionMethodStageNotNullValidator() {
                InspectionMethodStage stage = new InspectionMethodStage();
                stage.setStage(null);
                stage.setName("Test");

                CreateInspectionMethod inspectionMethod = new CreateInspectionMethod(
                        "Test",
                        InspectionMethodInput.PERCENTAGE,
                        InspectionMethodCalculationMethod.NEN2767,
                        List.of(stage)
                );

                assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stages are required.");
            }

            @Test
            public void testInspectionMethodStageMin1Validator() {
                InspectionMethodStage stage = new InspectionMethodStage();
                stage.setStage(0);
                stage.setName("Test");

                CreateInspectionMethod inspectionMethod = new CreateInspectionMethod(
                        "Test",
                        InspectionMethodInput.PERCENTAGE,
                        InspectionMethodCalculationMethod.NEN2767,
                        List.of(stage)
                );

                assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stages must have a minimum value of 1.");
            }

            @Test
            public void testInspectionMethodStageMax10Validator() {
                InspectionMethodStage stage = new InspectionMethodStage();
                stage.setStage(11);
                stage.setName("Test");

                CreateInspectionMethod inspectionMethod = new CreateInspectionMethod(
                        "Test",
                        InspectionMethodInput.PERCENTAGE,
                        InspectionMethodCalculationMethod.NEN2767,
                        List.of(stage)
                );

                assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stages must have a maximum value of 10.");
            }

            @Test
            public void testInspectionMethodStageNameNotNullValidator() {
                InspectionMethodStage stage = new InspectionMethodStage();
                stage.setStage(1);
                stage.setName(null);

                CreateInspectionMethod inspectionMethod = new CreateInspectionMethod(
                        "Test",
                        InspectionMethodInput.PERCENTAGE,
                        InspectionMethodCalculationMethod.NEN2767,
                        List.of(stage)
                );

                assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stage name is required.");
            }

            @Test
            public void testInspectionMethodStageNameNotBlankValidator() {
                InspectionMethodStage stage = new InspectionMethodStage();
                stage.setStage(1);
                stage.setName("");

                CreateInspectionMethod inspectionMethod = new CreateInspectionMethod(
                        "Test",
                        InspectionMethodInput.PERCENTAGE,
                        InspectionMethodCalculationMethod.NEN2767,
                        List.of(stage)
                );

                assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stage name is required.");
            }
        }
    }
}
