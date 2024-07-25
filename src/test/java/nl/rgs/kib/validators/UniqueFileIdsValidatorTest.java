package nl.rgs.kib.validators;

import nl.rgs.kib.model.list.InspectionListItemStageImage;
import nl.rgs.kib.shared.validators.UniqueFileIdsValidator;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UniqueFileIdsValidatorTest {

    private UniqueFileIdsValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new UniqueFileIdsValidator();
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
    public void testIsValidWithUniqueFileIds() {
        List<InspectionListItemStageImage> items = List.of(
                new InspectionListItemStageImage(true, new ObjectId()),
                new InspectionListItemStageImage(false, new ObjectId())
        );

        assertTrue(validator.isValid(items, null));
    }

    @Test
    public void testIsInvalidWithNonUniqueIds() {
        ObjectId id = new ObjectId();
        List<InspectionListItemStageImage> items = List.of(
                new InspectionListItemStageImage(true, id),
                new InspectionListItemStageImage(false, id)
        );

        assertFalse(validator.isValid(items, null));
    }

    @Test
    public void testIsValidWithNullValue() {
        List<InspectionListItemStageImage> items = List.of(
                new InspectionListItemStageImage(true, null),
                new InspectionListItemStageImage(false, null)
        );

        assertTrue(validator.isValid(items, null));
    }
}
