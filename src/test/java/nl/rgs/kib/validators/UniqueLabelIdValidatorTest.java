package nl.rgs.kib.validators;

import nl.rgs.kib.model.list.InspectionListItem;
import nl.rgs.kib.model.list.InspectionListItemValue;
import nl.rgs.kib.model.list.InspectionListItemValueCategory;
import nl.rgs.kib.model.list.InspectionListLabel;
import nl.rgs.kib.shared.validators.UniqueItemIdValidator;
import nl.rgs.kib.shared.validators.UniqueLabelIdValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UniqueLabelIdValidatorTest {
    private UniqueLabelIdValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UniqueLabelIdValidator();
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
    void testIsValidWithUniqueIds() {
        List<InspectionListLabel> items = List.of(
                new InspectionListLabel(
                        "id1",
                        1,
                        "Wooden",
                        null,
                        List.of()
                ),
                new InspectionListLabel(
                        "id2",
                        2,
                        "Wooden",
                        null,
                        List.of()
                )
        );

        assertTrue(validator.isValid(items, null));
    }

    @Test
    void testIsValidWithNonUniqueIds() {
        List<InspectionListLabel> items = List.of(
                new InspectionListLabel(
                        "id1",
                        1,
                        "Wooden",
                        null,
                        List.of()
                ),
                new InspectionListLabel(
                        "id1",
                        2,
                        "Wooden",
                        null,
                        List.of()
                )
        );

        assertFalse(validator.isValid(items, null));
    }

    @Test
    void testIsValidWithNullValue() {
        List<InspectionListLabel> items = List.of(
                new InspectionListLabel(
                        "id1",
                        1,
                        "Wooden",
                        null,
                        List.of()
                ),
                new InspectionListLabel(
                        null,
                        2,
                        "Wooden",
                        null,
                        List.of()
                )
        );
        
        assertTrue(validator.isValid(items, null));
    }
}
