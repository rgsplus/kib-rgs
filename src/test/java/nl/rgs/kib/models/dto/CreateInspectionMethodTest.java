package nl.rgs.kib.models.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Nested;

public class CreateInspectionMethodTest {
    @Nested
    public class CreateInspectionMethodValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        //TODO: Implement tests for InspectionMethod validations

        @Nested
        public class CreateInspectionMethodStageValidations {
            //TODO: Add tests for InspectionMethodStage validations
        }
    }
}
