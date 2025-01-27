package nl.rgs.kib.models;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.model.method.InspectionMethodCalculationMethod;
import nl.rgs.kib.model.method.InspectionMethodInput;
import nl.rgs.kib.model.method.InspectionMethodStage;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InspectionMethodTest {
    @Nested
    public class InspectionMethodValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        @Test
        public void testInspectionMethodIdNotNullValidator() {
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setName("Test");
            inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
            inspectionMethod.setInput(InspectionMethodInput.STAGE);
            inspectionMethod.setStages(new ArrayList<>());
            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod ID is required.");
        }

        @Test
        public void testInspectionMethodNameNotNullValidator() {
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setId(new ObjectId().toHexString());
            inspectionMethod.setName(null);
            inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
            inspectionMethod.setInput(InspectionMethodInput.STAGE);
            inspectionMethod.setStages(new ArrayList<>());
            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod name is required.");
        }

        @Test
        public void testInspectionMethodNameNotBlankValidator() {
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setId(new ObjectId().toHexString());
            inspectionMethod.setName("");
            inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
            inspectionMethod.setInput(InspectionMethodInput.STAGE);
            inspectionMethod.setStages(new ArrayList<>());
            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod name is required.");
        }

        @Test
        public void testInspectionMethodInputNotNullValidator() {
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setId(new ObjectId().toHexString());
            inspectionMethod.setName("Test");
            inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
            inspectionMethod.setInput(null);
            inspectionMethod.setStages(new ArrayList<>());
            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod input is required.");
        }

        @Test
        public void testInspectionMethodStagesNotNullValidator() {
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setId(new ObjectId().toHexString());
            inspectionMethod.setName("Test");
            inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
            inspectionMethod.setInput(InspectionMethodInput.STAGE);
            inspectionMethod.setStages(null);
            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stages are required.");
        }

        @Test
        public void testInspectionMethodUniqueStagesValidator() {
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setId(new ObjectId().toHexString());
            inspectionMethod.setName("Test");
            inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
            inspectionMethod.setInput(InspectionMethodInput.STAGE);
            InspectionMethodStage stage1 = new InspectionMethodStage();
            stage1.setStage(1);
            stage1.setName("Test 1");
            InspectionMethodStage stage2 = new InspectionMethodStage();
            stage2.setStage(1);
            stage2.setName("Test 2");
            inspectionMethod.setStages(List.of(stage1, stage2));
            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stages must be unique.");
            stage2.setStage(2);
            assertEquals(0, validator.validate(inspectionMethod).size(), "InspectionMethod stages must be unique.");
        }

        @Nested
        public class InspectionMethodStageValidations {


            @Test
            public void testInspectionMethodStageNotNullValidator() {
                InspectionMethod inspectionMethod = new InspectionMethod();
                inspectionMethod.setId(new ObjectId().toHexString());
                inspectionMethod.setName("Test");
                inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
                inspectionMethod.setInput(InspectionMethodInput.STAGE);
                InspectionMethodStage stage = new InspectionMethodStage();
                stage.setStage(null);
                stage.setName("Test");
                inspectionMethod.setStages(List.of(stage));
                assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stages are required.");
            }

            @Test
            public void testInspectionMethodStageMin1Validator() {
                InspectionMethod inspectionMethod = new InspectionMethod();
                inspectionMethod.setId(new ObjectId().toHexString());
                inspectionMethod.setName("Test");
                inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
                inspectionMethod.setInput(InspectionMethodInput.STAGE);
                InspectionMethodStage stage = new InspectionMethodStage();
                stage.setStage(0);
                stage.setName("Test");
                inspectionMethod.setStages(List.of(stage));
                assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stages must have a minimum value of 1.");

                stage.setStage(1);
                assertEquals(0, validator.validate(inspectionMethod).size(), "InspectionMethod stages must have a minimum value of 1.");

                stage.setStage(-5);
                assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stages must have a minimum value of 1.");
            }

            @Test
            public void testInspectionMethodStageMax10Validator() {
                InspectionMethod inspectionMethod = new InspectionMethod();
                inspectionMethod.setId(new ObjectId().toHexString());
                inspectionMethod.setName("Test");
                inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
                inspectionMethod.setInput(InspectionMethodInput.STAGE);
                InspectionMethodStage stage = new InspectionMethodStage();
                stage.setStage(11);
                stage.setName("Test");
                inspectionMethod.setStages(List.of(stage));
                assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stages must have a maximum value of 10.");

                stage.setStage(10);
                assertEquals(0, validator.validate(inspectionMethod).size(), "InspectionMethod stages must have a maximum value of 10.");

                stage.setStage(15);
                assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stages must have a maximum value of 10.");
            }

            @Test
            public void testInspectionMethodStageNameNotNullValidator() {
                InspectionMethod inspectionMethod = new InspectionMethod();
                inspectionMethod.setId(new ObjectId().toHexString());
                inspectionMethod.setName("Test");
                inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
                inspectionMethod.setInput(InspectionMethodInput.STAGE);
                InspectionMethodStage stage = new InspectionMethodStage();
                stage.setStage(1);
                stage.setName(null);
                inspectionMethod.setStages(List.of(stage));
                assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stage name is required.");
            }

            @Test
            public void testInspectionMethodStageNameNotBlankValidator() {
                InspectionMethod inspectionMethod = new InspectionMethod();
                inspectionMethod.setId(new ObjectId().toHexString());
                inspectionMethod.setName("Test");
                inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
                inspectionMethod.setInput(InspectionMethodInput.STAGE);
                InspectionMethodStage stage = new InspectionMethodStage();
                stage.setStage(1);
                stage.setName("");
                inspectionMethod.setStages(List.of(stage));
                assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stage name is required.");
            }
        }
    }

    @Nested
    public class InspectionMethodApplySort {
        @Test
        public void testApplySortWithEmptyList() {
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setStages(new ArrayList<>());

            inspectionMethod.applySort();

            assertTrue(inspectionMethod.getStages().isEmpty(), "Sorted list should be empty.");
        }

        @Test
        public void testApplySortWithSingleElement() {
            InspectionMethodStage singleStage = new InspectionMethodStage();
            singleStage.setStage(1);
            singleStage.setName("Single Stage");
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setStages(List.of(singleStage));

            inspectionMethod.applySort();

            assertEquals(1, inspectionMethod.getStages().size(), "Sorted list should contain one element.");
            assertEquals(singleStage, inspectionMethod.getStages().getFirst(), "The single element should match the original.");
        }

        @Test
        public void testApplySortWithElementsInReverseOrder() {
            InspectionMethodStage stage1 = new InspectionMethodStage();
            stage1.setStage(3);
            stage1.setName("Stage 3");
            InspectionMethodStage stage2 = new InspectionMethodStage();
            stage2.setStage(2);
            stage2.setName("Stage 2");
            InspectionMethodStage stage3 = new InspectionMethodStage();
            stage3.setStage(1);
            stage3.setName("Stage 1");
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setStages(List.of(stage1, stage2, stage3));

            inspectionMethod.applySort();

            assertEquals(3, inspectionMethod.getStages().size(), "Sorted list should contain three elements.");
            assertTrue(inspectionMethod.getStages().get(0).getStage() < inspectionMethod.getStages().get(1).getStage() && inspectionMethod.getStages().get(1).getStage() < inspectionMethod.getStages().get(2).getStage(), "Elements should be in ascending order.");
        }

        @Test
        public void testApplySortWithUnorderedElements() {
            InspectionMethodStage stage1 = new InspectionMethodStage();
            stage1.setStage(5);
            stage1.setName("Stage 5");
            InspectionMethodStage stage2 = new InspectionMethodStage();
            stage2.setStage(1);
            stage2.setName("Stage 1");
            InspectionMethodStage stage3 = new InspectionMethodStage();
            stage3.setStage(3);
            stage3.setName("Stage 3");
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setStages(List.of(stage1, stage2, stage3));

            inspectionMethod.applySort();

            assertEquals(3, inspectionMethod.getStages().size(), "Sorted list should contain three elements.");
            assertEquals(1, inspectionMethod.getStages().get(0).getStage(), "First element should be Stage 1.");
            assertEquals(3, inspectionMethod.getStages().get(1).getStage(), "Second element should be Stage 3.");
            assertEquals(5, inspectionMethod.getStages().get(2).getStage(), "Third element should be Stage 5.");
        }

        @Test
        public void testApplySortWithOrderedElements() {
            InspectionMethodStage stage1 = new InspectionMethodStage();
            stage1.setStage(1);
            stage1.setName("Stage 1");
            InspectionMethodStage stage2 = new InspectionMethodStage();
            stage2.setStage(3);
            stage2.setName("Stage 3");
            InspectionMethodStage stage3 = new InspectionMethodStage();
            stage3.setStage(5);
            stage3.setName("Stage 5");
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setStages(List.of(stage1, stage2, stage3));

            inspectionMethod.applySort();

            assertEquals(3, inspectionMethod.getStages().size(), "Sorted list should contain three elements.");
            assertEquals(1, inspectionMethod.getStages().get(0).getStage(), "First element should be Stage 1.");
            assertEquals(3, inspectionMethod.getStages().get(1).getStage(), "Second element should be Stage 3.");
            assertEquals(5, inspectionMethod.getStages().get(2).getStage(), "Third element should be Stage 5.");
        }

        @Test
        public void testApplySortWithDuplicateElements() {
            InspectionMethodStage stage1 = new InspectionMethodStage();
            stage1.setStage(1);
            stage1.setName("Stage 1");
            InspectionMethodStage stage2 = new InspectionMethodStage();
            stage2.setStage(3);
            stage2.setName("Stage 3");
            InspectionMethodStage stage3 = new InspectionMethodStage();
            stage3.setStage(1);
            stage3.setName("Stage 1");
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setStages(List.of(stage1, stage2, stage3));

            inspectionMethod.applySort();

            assertEquals(3, inspectionMethod.getStages().size(), "Sorted list should contain three elements.");
            assertEquals(1, inspectionMethod.getStages().get(0).getStage(), "First element should be Stage 1.");
            assertEquals(1, inspectionMethod.getStages().get(1).getStage(), "Second element should be Stage 1.");
            assertEquals(3, inspectionMethod.getStages().get(2).getStage(), "Third element should be Stage 3.");
        }
    }
}
