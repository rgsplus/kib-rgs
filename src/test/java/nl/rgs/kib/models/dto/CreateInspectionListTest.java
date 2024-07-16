package nl.rgs.kib.models.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Nested;

public class CreateInspectionListTest {
    @Nested
    public class CreateInspectionListValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        //TODO: Implement tests for InspectionList validations

        @Nested
        public class CreateInspectionListItemValidations {
            //TODO: Add tests for InspectionListItem validations

            @Nested
            public class CreateInspectionListItemStageValidations {
                //TODO: Add tests for InspectionListItemStage validations
            }
        }

        @Nested
        public class CreateInspectionListLabelValidations {
            //TODO: Add tests for InspectionListLabel validations

            @Nested
            public class CreateInspectionListLabelFeatureValidations {
                //TODO: Add tests for InspectionListLabelFeature validations
            }
        }
    }
}
