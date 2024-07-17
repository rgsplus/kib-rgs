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
import java.util.Arrays;
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
            inspectionMethod.setInput(InspectionMethodInput.PERCENTAGE);
            inspectionMethod.setStages(new ArrayList<>());
            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod ID is required.");
        }

        @Test
        public void testInspectionMethodNameNotNullValidator() {
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setId(new ObjectId().toHexString());
            inspectionMethod.setName(null);
            inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
            inspectionMethod.setInput(InspectionMethodInput.PERCENTAGE);
            inspectionMethod.setStages(new ArrayList<>());
            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod name is required.");
        }

        @Test
        public void testInspectionMethodNameNotBlankValidator() {
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setId(new ObjectId().toHexString());
            inspectionMethod.setName("");
            inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
            inspectionMethod.setInput(InspectionMethodInput.PERCENTAGE);
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
            inspectionMethod.setInput(InspectionMethodInput.PERCENTAGE);
            inspectionMethod.setStages(null);
            assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stages are required.");
        }

        @Test
        public void testInspectionMethodUniqueStagesValidator() {
            InspectionMethod inspectionMethod = new InspectionMethod();
            inspectionMethod.setId(new ObjectId().toHexString());
            inspectionMethod.setName("Test");
            inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
            inspectionMethod.setInput(InspectionMethodInput.PERCENTAGE);
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
                inspectionMethod.setInput(InspectionMethodInput.PERCENTAGE);
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
                inspectionMethod.setInput(InspectionMethodInput.PERCENTAGE);
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
                inspectionMethod.setInput(InspectionMethodInput.PERCENTAGE);
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
                inspectionMethod.setInput(InspectionMethodInput.PERCENTAGE);
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
                inspectionMethod.setInput(InspectionMethodInput.PERCENTAGE);
                InspectionMethodStage stage = new InspectionMethodStage();
                stage.setStage(1);
                stage.setName("");
                inspectionMethod.setStages(List.of(stage));
                assertEquals(1, validator.validate(inspectionMethod).size(), "InspectionMethod stage name is required.");
            }
        }
    }

    @Nested
    public class InspectionMethodSortStagesMethod {
        @Test
        public void testSortStagesWithEmptyList() {
            List<InspectionMethodStage> emptyList = new ArrayList<>();
            List<InspectionMethodStage> sortedList = InspectionMethod.sortStages(emptyList);
            assertTrue(sortedList.isEmpty(), "Sorted list should be empty.");
        }

        @Test
        public void testSortStagesWithSingleElement() {
            InspectionMethodStage singleStage = new InspectionMethodStage(1, "Single Stage");
            List<InspectionMethodStage> singleList = List.of(singleStage);
            List<InspectionMethodStage> sortedList = InspectionMethod.sortStages(singleList);
            assertEquals(1, sortedList.size(), "Sorted list should contain one element.");
            assertEquals(singleStage, sortedList.getFirst(), "The single element should match the original.");
        }

        @Test
        public void testSortStagesWithElementsInReverseOrder() {
            InspectionMethodStage stage1 = new InspectionMethodStage(3, "Stage 3");
            InspectionMethodStage stage2 = new InspectionMethodStage(2, "Stage 2");
            InspectionMethodStage stage3 = new InspectionMethodStage(1, "Stage 1");
            List<InspectionMethodStage> reverseList = Arrays.asList(stage1, stage2, stage3);
            List<InspectionMethodStage> sortedList = InspectionMethod.sortStages(reverseList);
            assertEquals(3, sortedList.size(), "Sorted list should contain three elements.");
            assertTrue(sortedList.get(0).getStage() < sortedList.get(1).getStage() && sortedList.get(1).getStage() < sortedList.get(2).getStage(), "Elements should be in ascending order.");
        }

        @Test
        public void testSortStagesWithUnorderedElements() {
            InspectionMethodStage stage1 = new InspectionMethodStage(5, "Stage 5");
            InspectionMethodStage stage2 = new InspectionMethodStage(1, "Stage 1");
            InspectionMethodStage stage3 = new InspectionMethodStage(3, "Stage 3");
            List<InspectionMethodStage> unorderedList = Arrays.asList(stage1, stage2, stage3);
            List<InspectionMethodStage> sortedList = InspectionMethod.sortStages(unorderedList);
            assertEquals(3, sortedList.size(), "Sorted list should contain three elements.");
            assertEquals(1, sortedList.get(0).getStage(), "First element should be Stage 1.");
            assertEquals(3, sortedList.get(1).getStage(), "Second element should be Stage 3.");
            assertEquals(5, sortedList.get(2).getStage(), "Third element should be Stage 5.");
        }

        @Test
        public void testSortStagesWithOrderedElements() {
            InspectionMethodStage stage1 = new InspectionMethodStage(1, "Stage 1");
            InspectionMethodStage stage2 = new InspectionMethodStage(3, "Stage 3");
            InspectionMethodStage stage3 = new InspectionMethodStage(5, "Stage 5");
            List<InspectionMethodStage> orderedList = Arrays.asList(stage1, stage2, stage3);
            List<InspectionMethodStage> sortedList = InspectionMethod.sortStages(orderedList);
            assertEquals(3, sortedList.size(), "Sorted list should contain three elements.");
            assertEquals(1, sortedList.get(0).getStage(), "First element should be Stage 1.");
            assertEquals(3, sortedList.get(1).getStage(), "Second element should be Stage 3.");
            assertEquals(5, sortedList.get(2).getStage(), "Third element should be Stage 5.");
        }

        @Test
        public void testSortStagesWithDuplicateElements() {
            InspectionMethodStage stage1 = new InspectionMethodStage(1, "Stage 1");
            InspectionMethodStage stage2 = new InspectionMethodStage(3, "Stage 3");
            InspectionMethodStage stage3 = new InspectionMethodStage(1, "Stage 1");
            List<InspectionMethodStage> duplicateList = Arrays.asList(stage1, stage2, stage3);
            List<InspectionMethodStage> sortedList = InspectionMethod.sortStages(duplicateList);
            assertEquals(3, sortedList.size(), "Sorted list should contain three elements.");
            assertEquals(1, sortedList.get(0).getStage(), "First element should be Stage 1.");
            assertEquals(1, sortedList.get(1).getStage(), "Second element should be Stage 1.");
            assertEquals(3, sortedList.get(2).getStage(), "Third element should be Stage 3.");
        }
    }
}