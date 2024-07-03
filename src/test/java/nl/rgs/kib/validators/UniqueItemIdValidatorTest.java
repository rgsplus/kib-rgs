package nl.rgs.kib.validators;

import nl.rgs.kib.model.list.InspectionListItem;
import nl.rgs.kib.model.list.InspectionListItemValue;
import nl.rgs.kib.model.list.InspectionListItemValueCategory;
import nl.rgs.kib.shared.validators.UniqueItemIdValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UniqueItemIdValidatorTest {
    private UniqueItemIdValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UniqueItemIdValidator();
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
        List<InspectionListItem> items = List.of(
                new InspectionListItem(1, new InspectionListItemValue(
                        "id1",
                        "Roof",
                        "Wooden",
                        InspectionListItemValueCategory.SERIOUSLY,
                        null,
                        List.of()
                )),
                new InspectionListItem(2, new InspectionListItemValue(
                        "id2",
                        "Roof",
                        "Wooden",
                        InspectionListItemValueCategory.SERIOUSLY,
                        null,
                        List.of()
                ))
        );

        assertTrue(validator.isValid(items, null));
    }

    @Test
    void testIsValidWithNonUniqueIds() {
        List<InspectionListItem> items = List.of(
                new InspectionListItem(1, new InspectionListItemValue(
                        "id1",
                        "Roof",
                        "Wooden",
                        InspectionListItemValueCategory.SERIOUSLY,
                        null,
                        List.of()
                )),
                new InspectionListItem(2, new InspectionListItemValue(
                        "id1",
                        "Roof",
                        "Wooden",
                        InspectionListItemValueCategory.SERIOUSLY,
                        null,
                        List.of()
                ))
        );

        assertFalse(validator.isValid(items, null));
    }

    @Test
    void testIsValidWithNullValue() {
        List<InspectionListItem> items = List.of(
                new InspectionListItem(1, null),
                new InspectionListItem(2, new InspectionListItemValue(
                        "id1",
                        "Roof",
                        "Wooden",
                        InspectionListItemValueCategory.SERIOUSLY,
                        null,
                        List.of()
                ))
        );
        
        assertTrue(validator.isValid(items, null));
    }
}
