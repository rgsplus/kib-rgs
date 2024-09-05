package nl.rgs.kib.validators;

import nl.rgs.kib.model.list.InspectionListItemStageImage;
import nl.rgs.kib.shared.validators.MainImageValidator;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainImageValidatorTest {

    private MainImageValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new MainImageValidator();
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
    public void testIsValidWithOneMainImage() {
        List<InspectionListItemStageImage> items = List.of(
                new InspectionListItemStageImage(false, new ObjectId().toHexString()),
                new InspectionListItemStageImage(true, new ObjectId().toHexString())
        );

        assertTrue(validator.isValid(items, null));
    }


    @Test
    public void testIsInvalidWithMultipleMainImages() {
        List<InspectionListItemStageImage> items = List.of(
                new InspectionListItemStageImage(true, new ObjectId().toHexString()),
                new InspectionListItemStageImage(true, new ObjectId().toHexString())
        );

        assertFalse(validator.isValid(items, null));
    }

    @Test
    public void testIsInvalidWithNoMainImages() {
        List<InspectionListItemStageImage> items = List.of(
                new InspectionListItemStageImage(false, new ObjectId().toHexString()),
                new InspectionListItemStageImage(false, new ObjectId().toHexString())
        );

        assertFalse(validator.isValid(items, null));
    }
}
