package nl.rgs.kib.models;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.rgs.kib.model.list.*;
import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.model.method.InspectionMethodCalculationMethod;
import nl.rgs.kib.model.method.InspectionMethodInput;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InspectionListTest {

    InspectionMethod inspectionMethod;

    @BeforeEach
    public void setUp() {
        inspectionMethod = new InspectionMethod();
        inspectionMethod.setId(new ObjectId().toHexString());
        inspectionMethod.setName("test");
        inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
        inspectionMethod.setInput(InspectionMethodInput.STAGE);
        inspectionMethod.setStages(List.of());
    }

    private InspectionListItem createInspectionListItem(String name) {
        InspectionListItemStage stage1 = new InspectionListItemStage();
        stage1.setStage(1);
        stage1.setName("Stage 1");
        stage1.setImages(List.of());

        InspectionListItemStage stage2 = new InspectionListItemStage();
        stage2.setStage(2);
        stage2.setName("Stage 2");
        stage2.setImages(List.of());

        return new InspectionListItem(UUID.randomUUID().toString(), 0, "Fundering", "Veiligheid", "Constructief",
                name, "1", "Visuele beoordeling fundering door gevel en vloeren", inspectionMethod, List.of(stage1, stage2));
    }

    private InspectionListItem createInspectionListItem(String name, String id) {
        InspectionListItem item = createInspectionListItem(name);
        item.setId(id);
        return item;
    }

    private InspectionListItem createInspectionListItem(String name, Integer index) {
        InspectionListItem item = createInspectionListItem(name);
        item.setIndex(index);
        return item;
    }

    private InspectionListItem createInspectionListItem(String name, String id, Integer index) {
        InspectionListItem item = createInspectionListItem(name);
        item.setId(id);
        item.setIndex(index);
        return item;
    }

    @Nested
    public class InspectionListValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        @Test
        public void testInspectionListIdNotNullValidator() {
            InspectionList inspectionList = new InspectionList();
            inspectionList.setName("test");
            inspectionList.setItems(List.of());
            inspectionList.setLabels(List.of());
            inspectionList.setStatus(InspectionListStatus.DEFINITIVE);
            assertEquals(1, validator.validate(inspectionList).size(), "Id should not be null.");
        }

        @Test
        public void testInspectionListNameNotNullValidator() {
            InspectionList inspectionList = new InspectionList();
            inspectionList.setId(new ObjectId().toHexString());
            inspectionList.setName(null);
            inspectionList.setItems(List.of());
            inspectionList.setLabels(List.of());
            inspectionList.setStatus(InspectionListStatus.DEFINITIVE);
            assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
        }

        @Test
        public void testInspectionListNameNotBlankValidator() {
            InspectionList inspectionList = new InspectionList();
            inspectionList.setId(new ObjectId().toHexString());
            inspectionList.setName(" ");
            inspectionList.setItems(List.of());
            inspectionList.setLabels(List.of());
            inspectionList.setStatus(InspectionListStatus.DEFINITIVE);
            assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
        }

        @Test
        public void testInspectionListStatusNotNullValidator() {
            InspectionList inspectionList = new InspectionList();
            inspectionList.setId(new ObjectId().toHexString());
            inspectionList.setName("test");
            inspectionList.setItems(List.of());
            inspectionList.setLabels(List.of());
            inspectionList.setStatus(null);
            assertEquals(1, validator.validate(inspectionList).size(), "Status should not be null.");
        }

        @Test
        public void testInspectionListItemsNotNullValidator() {
            InspectionList inspectionList = new InspectionList();
            inspectionList.setId(new ObjectId().toHexString());
            inspectionList.setName("test");
            inspectionList.setItems(null);
            inspectionList.setLabels(List.of());
            inspectionList.setStatus(InspectionListStatus.DEFINITIVE);
            assertEquals(1, validator.validate(inspectionList).size(), "Items should not be null.");
        }

        @Test
        public void testInspectionListItemsUniqueIdsValidator() {
            InspectionListItem item1 = createInspectionListItem("Item 1", "id1", 0);
            InspectionListItem item2 = createInspectionListItem("Item 2", "id1", 1);

            InspectionList inspectionList = new InspectionList();
            inspectionList.setId(new ObjectId().toHexString());
            inspectionList.setName("test");
            inspectionList.setItems(List.of(item1, item2));
            inspectionList.setLabels(List.of());
            inspectionList.setStatus(InspectionListStatus.DEFINITIVE);
            
            assertEquals(1, validator.validate(inspectionList).size(), "Items should have unique ids.");
        }

        @Test
        public void testInspectionListItemsValidIndexesValidator() {
            InspectionListItem item1 = createInspectionListItem("Item 1", 0);
            InspectionListItem item2 = createInspectionListItem("Item 2", 0);

            InspectionList inspectionList = new InspectionList();
            inspectionList.setId(new ObjectId().toHexString());
            inspectionList.setName("test");
            inspectionList.setItems(List.of(item1, item2));
            inspectionList.setLabels(List.of());
            inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

            assertEquals(1, validator.validate(inspectionList).size(), "Items should have unique indexes.");
        }

        @Test
        public void testInspectionListLabelsNotNullValidator() {
            InspectionList inspectionList = new InspectionList();
            inspectionList.setId(new ObjectId().toHexString());
            inspectionList.setName("test");
            inspectionList.setItems(List.of());
            inspectionList.setLabels(null);
            inspectionList.setStatus(InspectionListStatus.DEFINITIVE);
            assertEquals(1, validator.validate(inspectionList).size(), "Labels should not be null.");
        }

        @Test
        public void testInspectionListLabelsUniqueIdsValidator() {
            InspectionListLabel label1 = new InspectionListLabel("id1", 0, "Label 1", null, List.of());
            InspectionListLabel label2 = new InspectionListLabel("id1", 1, "Label 2", null, List.of());
            InspectionList inspectionList = new InspectionList();
            inspectionList.setId(new ObjectId().toHexString());
            inspectionList.setName("test");
            inspectionList.setItems(List.of());
            inspectionList.setLabels(List.of(label1, label2));
            inspectionList.setStatus(InspectionListStatus.DEFINITIVE);
            assertEquals(1, validator.validate(inspectionList).size(), "Labels should have unique ids.");
        }

        @Test
        public void testInspectionListLabelsValidIndexesValidator() {
            InspectionListLabel label1 = new InspectionListLabel("id1", 1, "Label 1", null, List.of());
            InspectionListLabel label2 = new InspectionListLabel("id2", 1, "Label 2", null, List.of());
            InspectionList inspectionList = new InspectionList();
            inspectionList.setId(new ObjectId().toHexString());
            inspectionList.setName("test");
            inspectionList.setItems(List.of());
            inspectionList.setLabels(List.of(label1, label2));
            inspectionList.setStatus(InspectionListStatus.DEFINITIVE);
            assertEquals(1, validator.validate(inspectionList).size(), "Labels should have unique indexes.");
        }

        @Nested
        public class InspectionListItemValidations {

            @Test
            public void testInspectionListItemIndexNotNullValidator() {
                InspectionListItem item = createInspectionListItem("Item 1");
                item.setIndex(null);

                InspectionList inspectionList = new InspectionList();
                inspectionList.setId(new ObjectId().toHexString());
                inspectionList.setName("test");
                inspectionList.setItems(List.of(item));
                inspectionList.setLabels(List.of());
                inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                assertEquals(2, validator.validate(inspectionList).size(), "Index should not be null.");
            }

            @Test
            public void testInspectionListItemIndexMinValidator() {
                InspectionListItem item = createInspectionListItem("Item 1");
                item.setIndex(-1);

                InspectionList inspectionList = new InspectionList();
                inspectionList.setId(new ObjectId().toHexString());
                inspectionList.setName("test");
                inspectionList.setItems(List.of(item));
                inspectionList.setLabels(List.of());
                inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                assertEquals(2, validator.validate(inspectionList).size(), "Index should be greater than or equal to 0.");
            }

            @Test
            public void testInspectionListItemIdNotNullValidator() {
                InspectionListItem item = createInspectionListItem("Item 1");
                item.setId(null);

                InspectionList inspectionList = new InspectionList();
                inspectionList.setId(new ObjectId().toHexString());
                inspectionList.setName("test");
                inspectionList.setItems(List.of(item));
                inspectionList.setLabels(List.of());
                inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                assertEquals(1, validator.validate(inspectionList).size(), "Id should not be null.");
            }

            @Test
            public void testInspectionListItemNameNotNullValidator() {
                InspectionListItem item = createInspectionListItem("Item 1");
                item.setName(null);

                InspectionList inspectionList = new InspectionList();
                inspectionList.setId(new ObjectId().toHexString());
                inspectionList.setName("test");
                inspectionList.setItems(List.of(item));
                inspectionList.setLabels(List.of());
                inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
            }

            @Test
            public void testInspectionListItemNameNotBlankValidator() {
                InspectionListItem item = createInspectionListItem("Item 1");
                item.setName(" ");

                InspectionList inspectionList = new InspectionList();
                inspectionList.setId(new ObjectId().toHexString());
                inspectionList.setName("test");
                inspectionList.setItems(List.of(item));
                inspectionList.setLabels(List.of());
                inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
            }

            @Test
            public void testInspectionListItemInspectionMethodNotNullValidator() {
                InspectionListItem item = createInspectionListItem("Item 1");
                item.setInspectionMethod(null);

                InspectionList inspectionList = new InspectionList();
                inspectionList.setId(new ObjectId().toHexString());
                inspectionList.setName("test");
                inspectionList.setItems(List.of(item));
                inspectionList.setLabels(List.of());
                inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                assertEquals(1, validator.validate(inspectionList).size(), "Inspection method should not be null.");
            }

            @Test
            public void testInspectionListItemStagesNotNullValidator() {
                InspectionListItem item = createInspectionListItem("Item 1");
                item.setStages(null);

                InspectionList inspectionList = new InspectionList();
                inspectionList.setId(new ObjectId().toHexString());
                inspectionList.setName("test");
                inspectionList.setItems(List.of(item));
                inspectionList.setLabels(List.of());
                inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                assertEquals(1, validator.validate(inspectionList).size(), "Stages should not be null.");
            }

            @Test
            public void testInspectionListItemStagesUniqueStagesValidator() {
                InspectionListItem item = createInspectionListItem("Item 1");
                item.getStages().get(0).setStage(1);
                item.getStages().get(1).setStage(1);


                InspectionList inspectionList = new InspectionList();
                inspectionList.setId(new ObjectId().toHexString());
                inspectionList.setName("test");
                inspectionList.setItems(List.of(item));
                inspectionList.setLabels(List.of());
                inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                assertEquals(1, validator.validate(inspectionList).size(), "Stages should have unique stages.");
            }

            @Test
            public void testInspectionListItemStagesMinLength2Validator() {
                InspectionListItem item = createInspectionListItem("Item 1");
                item.setStages(List.of());

                InspectionList inspectionList = new InspectionList();
                inspectionList.setId(new ObjectId().toHexString());
                inspectionList.setName("test");
                inspectionList.setItems(List.of(item));
                inspectionList.setLabels(List.of());
                inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                assertEquals(1, validator.validate(inspectionList).size(), "Stages should have at least 2 stages.");
            }

            @Nested
            public class InspectionListItemStageValidations {

                @Test
                public void testInspectionListItemStageStageNotNullValidator() {
                    InspectionListItem item = createInspectionListItem("Item 1");
                    item.getStages().getFirst().setStage(null);

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of(item));
                    inspectionList.setLabels(List.of());
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Stage should not be null.");
                }

                @Test
                public void testInspectionListItemStageStageMin1Validator() {
                    InspectionListItem item = createInspectionListItem("Item 1");
                    item.getStages().getFirst().setStage(0);

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of(item));
                    inspectionList.setLabels(List.of());
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Stage should be greater than or equal to 0.");
                }

                @Test
                public void testInspectionListItemStageStageMax100Validator() {
                    InspectionListItem item = createInspectionListItem("Item 1");
                    item.getStages().getFirst().setStage(101);

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of(item));
                    inspectionList.setLabels(List.of());
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Stage should be less than or equal to 10.");
                }

                @Test
                public void testInspectionListItemStageNameNotNullValidator() {
                    InspectionListItem item = createInspectionListItem("Item 1");
                    item.getStages().getFirst().setName(null);

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of(item));
                    inspectionList.setLabels(List.of());
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
                }

                @Test
                public void testInspectionListItemStageNameNotBlankValidator() {
                    InspectionListItem item = createInspectionListItem("Item 1");
                    item.getStages().getFirst().setName(" ");

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of(item));
                    inspectionList.setLabels(List.of());
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
                }

                @Test
                public void testInspectionListItemStageMaxMinValidator() {
                    InspectionListItem item = createInspectionListItem("Item 1");
                    item.getStages().getFirst().setMax(-1.0);

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of(item));
                    inspectionList.setLabels(List.of());
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Max should be greater than or equal to 0.");
                }

                @Test
                public void testInspectionListItemStageMaxMaxValidator() {
                    InspectionListItem item = createInspectionListItem("Item 1");
                    item.getStages().getFirst().setMax(101.0);

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of(item));
                    inspectionList.setLabels(List.of());
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Max should be less than or equal to 100.");
                }

                @Test
                public void testInspectionListItemStageImagesNotNullValidator() {
                    InspectionListItem item = createInspectionListItem("Item 1");
                    item.getStages().getFirst().setImages(null);

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of(item));
                    inspectionList.setLabels(List.of());
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Images should not be null.");
                }

                @Test
                public void testInspectionListItemStageImagesMainImageValidator() {
                    InspectionListItem item = createInspectionListItem("Item 1");
                    InspectionListItemStageImage image1 = new InspectionListItemStageImage(true, new ObjectId());
                    InspectionListItemStageImage image2 = new InspectionListItemStageImage(true, new ObjectId());
                    item.getStages().getFirst().setImages(List.of(image1, image2));

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of(item));
                    inspectionList.setLabels(List.of());
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Main image should be unique.");
                }

                @Test
                public void testInspectionListItemStageImagesUniqueFileIdsValidator() {
                    InspectionListItem item = createInspectionListItem("Item 1");
                    ObjectId fileId = new ObjectId();
                    InspectionListItemStageImage image1 = new InspectionListItemStageImage(true, fileId);
                    InspectionListItemStageImage image2 = new InspectionListItemStageImage(false, fileId);
                    item.getStages().getFirst().setImages(List.of(image1, image2));

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of(item));
                    inspectionList.setLabels(List.of());
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "File ids should be unique.");
                }

                @Nested
                public class InspectionListItemStageImageValidations {

                    @Test
                    public void testInspectionListItemStageImageFileIdNotNullValidator() {
                        InspectionListItem item = createInspectionListItem("Item 1");
                        InspectionListItemStageImage image = new InspectionListItemStageImage();
                        image.setMain(true);

                        InspectionListItemStage stage = item.getStages().getFirst();
                        stage.setImages(List.of(image));

                        InspectionList inspectionList = new InspectionList();
                        inspectionList.setId(new ObjectId().toHexString());
                        inspectionList.setName("test");
                        inspectionList.setItems(List.of(item));
                        inspectionList.setLabels(List.of());
                        inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                        assertEquals(1, validator.validate(inspectionList).size(), "File id should not be null.");
                    }
                }
            }

            @Nested
            public class InspectionListLabelValidations {

                @Test
                public void testInspectionListLabelIdNotNullValidator() {
                    InspectionListLabel label = new InspectionListLabel();
                    label.setIndex(0);
                    label.setName("test");
                    label.setGroup("group");
                    label.setFeatures(List.of());

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of());
                    inspectionList.setLabels(List.of(label));
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Id should not be null.");
                }

                @Test
                public void testInspectionListLabelIndexNotNullValidator() {
                    InspectionListLabel label = new InspectionListLabel();
                    label.setId("id");
                    label.setName("test");
                    label.setGroup("group");
                    label.setFeatures(List.of());

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of());
                    inspectionList.setLabels(List.of(label));
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(2, validator.validate(inspectionList).size(), "Index should not be null.");
                }

                @Test
                public void testInspectionListLabelIndexMinValidator() {
                    InspectionListLabel label = new InspectionListLabel();
                    label.setId("id");
                    label.setIndex(-1);
                    label.setName("test");
                    label.setGroup("group");
                    label.setFeatures(List.of());

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of());
                    inspectionList.setLabels(List.of(label));
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(2, validator.validate(inspectionList).size(), "Index should be greater than or equal to 0.");
                }

                @Test
                public void testInspectionListLabelNameNotNullValidator() {
                    InspectionListLabel label = new InspectionListLabel();
                    label.setId("id");
                    label.setIndex(0);
                    label.setGroup("group");
                    label.setFeatures(List.of());

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of());
                    inspectionList.setLabels(List.of(label));
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
                }

                @Test
                public void testInspectionListLabelNameNotBlankValidator() {
                    InspectionListLabel label = new InspectionListLabel();
                    label.setId("id");
                    label.setIndex(0);
                    label.setName(" ");
                    label.setGroup("group");
                    label.setFeatures(List.of());

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of());
                    inspectionList.setLabels(List.of(label));
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
                }

                @Test
                public void testInspectionListLabelFeaturesNotNullValidator() {
                    InspectionListLabel label = new InspectionListLabel();
                    label.setId("id");
                    label.setIndex(0);
                    label.setName("test");
                    label.setGroup("group");

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of());
                    inspectionList.setLabels(List.of(label));
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Features should not be null.");
                }

                @Test
                public void testInspectionListLabelFeaturesValidIndexesValidator() {
                    InspectionListLabelFeature feature1 = new InspectionListLabelFeature(0, "Feature 1");
                    InspectionListLabelFeature feature2 = new InspectionListLabelFeature(0, "Feature 2");
                    InspectionListLabel label = new InspectionListLabel("id", 0, "Label", null, List.of(feature1, feature2));

                    InspectionList inspectionList = new InspectionList();
                    inspectionList.setId(new ObjectId().toHexString());
                    inspectionList.setName("test");
                    inspectionList.setItems(List.of());
                    inspectionList.setLabels(List.of(label));
                    inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                    assertEquals(1, validator.validate(inspectionList).size(), "Features should have unique indexes.");
                }

                @Nested
                public class InspectionListLabelFeatureValidations {

                    @Test
                    public void testInspectionListLabelFeatureIndexNotNullValidator() {
                        InspectionListLabelFeature feature = new InspectionListLabelFeature();
                        feature.setName("test");

                        InspectionListLabel label = new InspectionListLabel("id", 0, "Label", null, List.of(feature));

                        InspectionList inspectionList = new InspectionList();
                        inspectionList.setId(new ObjectId().toHexString());
                        inspectionList.setName("test");
                        inspectionList.setItems(List.of());
                        inspectionList.setLabels(List.of(label));
                        inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                        assertEquals(2, validator.validate(inspectionList).size(), "Index should not be null.");
                    }

                    @Test
                    public void testInspectionListLabelFeatureIndexMinValidator() {
                        InspectionListLabelFeature feature = new InspectionListLabelFeature();
                        feature.setIndex(-1);
                        feature.setName("test");

                        InspectionListLabel label = new InspectionListLabel("id", 0, "Label", null, List.of(feature));

                        InspectionList inspectionList = new InspectionList();
                        inspectionList.setId(new ObjectId().toHexString());
                        inspectionList.setName("test");
                        inspectionList.setItems(List.of());
                        inspectionList.setLabels(List.of(label));
                        inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                        assertEquals(2, validator.validate(inspectionList).size(), "Index should be greater than or equal to 0.");
                    }

                    @Test
                    public void testInspectionListLabelFeatureNameNotNullValidator() {
                        InspectionListLabelFeature feature = new InspectionListLabelFeature();
                        feature.setIndex(0);

                        InspectionListLabel label = new InspectionListLabel("id", 0, "Label", null, List.of(feature));

                        InspectionList inspectionList = new InspectionList();
                        inspectionList.setId(new ObjectId().toHexString());
                        inspectionList.setName("test");
                        inspectionList.setItems(List.of());
                        inspectionList.setLabels(List.of(label));
                        inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                        assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
                    }

                    @Test
                    public void testInspectionListLabelFeatureNameNotBlankValidator() {
                        InspectionListLabelFeature feature = new InspectionListLabelFeature();
                        feature.setIndex(0);
                        feature.setName(" ");

                        InspectionListLabel label = new InspectionListLabel("id", 0, "Label", null, List.of(feature));

                        InspectionList inspectionList = new InspectionList();
                        inspectionList.setId(new ObjectId().toHexString());
                        inspectionList.setName("test");
                        inspectionList.setItems(List.of());
                        inspectionList.setLabels(List.of(label));
                        inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

                        assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
                    }
                }
            }
        }

        @Nested
        public class InspectionListSortItemsStagesAndImagesMethod {
            @Test
            public void testSortItemsStagesAndImagesWithEmptyList() {
                assertTrue(InspectionList.sortItemsStagesAndImages(List.of()).isEmpty(), "Sorted list should be empty.");
            }

            @Test
            public void testSortItemsStagesAndImagesWithMultipleItems() {
                InspectionListItem item1 = createInspectionListItem("Item 1", "id1", 1);
                InspectionListItem item2 = createInspectionListItem("Item 2", "id2", 0);

                List<InspectionListItem> sortedItems = InspectionList.sortItemsStagesAndImages(Arrays.asList(item1, item2));

                assertEquals("id2", sortedItems.get(0).getId(), "First item should be 'Item 2'.");
                assertEquals("id1", sortedItems.get(1).getId(), "Second item should be 'Item 1'.");
                assertEquals(Integer.valueOf(1), sortedItems.get(0).getStages().getFirst().getStage(), "First stage of first item should be in order.");
            }

            @Test
            public void testSortItemsStagesAndImagesWithSingleItem() {
                InspectionListItemStage stage1 = new InspectionListItemStage();
                stage1.setStage(1);
                stage1.setName("Stage 1");
                stage1.setImages(List.of());

                InspectionListItem item = createInspectionListItem("Item 1");
                item.setStages(List.of(stage1));

                List<InspectionListItem> sortedItems = InspectionList.sortItemsStagesAndImages(List.of(item));

                assertEquals(1, sortedItems.size(), "Sorted list should contain one element.");
                assertEquals(stage1, sortedItems.getFirst().getStages().getFirst(), "The single element should match the original.");
            }

            @Test
            public void testSortItemsStagesAndImagesWithUnorderedItems() {
                InspectionListItemStage stage1 = new InspectionListItemStage();
                stage1.setStage(5);
                stage1.setName("Stage 5");
                stage1.setImages(List.of());
                InspectionListItemStage stage2 = new InspectionListItemStage();
                stage2.setStage(1);
                stage2.setName("Stage 1");
                stage2.setImages(List.of());
                InspectionListItemStage stage3 = new InspectionListItemStage();
                stage3.setStage(3);
                stage3.setName("Stage 3");
                stage3.setImages(List.of());


                InspectionListItem item1 = createInspectionListItem("Item 5", 2);
                item1.setStages(List.of(stage1));
                InspectionListItem item2 = createInspectionListItem("Item 1", 0);
                item2.setStages(List.of(stage2));
                InspectionListItem item3 = createInspectionListItem("Item 3", 1);
                item3.setStages(List.of(stage3));
                List<InspectionListItem> unorderedItems = Arrays.asList(item1, item2, item3);
                List<InspectionListItem> sortedItems = InspectionList.sortItemsStagesAndImages(unorderedItems);

                assertEquals(3, sortedItems.size(), "Sorted list should contain three elements.");
                assertEquals(1, sortedItems.get(0).getStages().getFirst().getStage(), "First element should be Stage 1.");
                assertEquals(3, sortedItems.get(1).getStages().getFirst().getStage(), "Second element should be Stage 3.");
                assertEquals(5, sortedItems.get(2).getStages().getFirst().getStage(), "Third element should be Stage 5.");
            }

            @Test
            public void testSortItemsStagesAndImagesWithUnorderedImages() {
                InspectionListItemStageImage image1 = new InspectionListItemStageImage(false, new ObjectId());
                InspectionListItemStageImage image2 = new InspectionListItemStageImage(true, new ObjectId());
                InspectionListItemStageImage image3 = new InspectionListItemStageImage(false, new ObjectId());

                InspectionListItemStage stage1 = new InspectionListItemStage();
                stage1.setStage(1);
                stage1.setName("Stage 1");
                stage1.setImages(List.of(image1, image2, image3));

                InspectionListItem item1 = createInspectionListItem("Item 1");
                item1.setStages(List.of(stage1));

                List<InspectionListItem> unorderedItems = List.of(item1);
                List<InspectionListItem> sortedItems = InspectionList.sortItemsStagesAndImages(unorderedItems);

                assertEquals(1, sortedItems.size(), "Sorted list should contain one element.");
                assertEquals(3, sortedItems.getFirst().getStages().getFirst().getImages().size(), "Sorted list should contain three images.");
                assertEquals(image2, sortedItems.getFirst().getStages().getFirst().getImages().getFirst(), "First image should be the main image.");
                assertEquals(true, sortedItems.getFirst().getStages().getFirst().getImages().getFirst().getMain(), "First image should be the main image.");
            }
        }

        @Nested
        public class InspectionListSortLabelsAndFeaturesMethod {
            @Test
            public void testSortLabelsAndFeaturesWithEmptyList() {
                assertTrue(InspectionList.sortLabelsAndFeatures(List.of()).isEmpty(), "Sorted list should be empty.");
            }

            @Test
            public void testSortLabelsAndFeaturesWithMultipleLabels() {
                InspectionListLabel label1 = new InspectionListLabel("id1", 2, "Label 1", null, Arrays.asList(new InspectionListLabelFeature(2, "Feature 1"), new InspectionListLabelFeature(1, "Feature 2")));
                InspectionListLabel label2 = new InspectionListLabel("id2", 1, "Label 2", null, Arrays.asList(new InspectionListLabelFeature(1, "Feature 3"), new InspectionListLabelFeature(2, "Feature 4")));
                List<InspectionListLabel> sortedLabels = InspectionList.sortLabelsAndFeatures(Arrays.asList(label1, label2));

                assertEquals("id2", sortedLabels.get(0).getId(), "First label should be 'Label 2'.");
                assertEquals("id1", sortedLabels.get(1).getId(), "Second label should be 'Label 1'.");
                assertEquals(Integer.valueOf(1), sortedLabels.get(0).getFeatures().getFirst().getIndex(), "First feature of first label should be in order.");
            }

            @Test
            public void testSortLabelsAndFeaturesWithSingleLabel() {
                InspectionListLabel label = new InspectionListLabel("id1", 1, "Label 1", null, List.of(new InspectionListLabelFeature(1, "Feature 1")));
                List<InspectionListLabel> sortedLabels = InspectionList.sortLabelsAndFeatures(List.of(label));
                assertEquals(1, sortedLabels.size(), "Sorted list should contain one element.");
                assertEquals("id1", sortedLabels.getFirst().getId(), "The single element should match the original.");
            }

            @Test
            public void testSortLabelsAndFeaturesWithUnorderedLabels() {
                InspectionListLabelFeature feature1 = new InspectionListLabelFeature(2, "Feature 2");
                InspectionListLabelFeature feature2 = new InspectionListLabelFeature(1, "Feature 1");
                InspectionListLabelFeature feature3 = new InspectionListLabelFeature(3, "Feature 3");
                InspectionListLabel label1 = new InspectionListLabel("id1", 2, "Label 1", null, List.of(feature1));
                InspectionListLabel label2 = new InspectionListLabel("id2", 1, "Label 2", null, List.of(feature2));
                InspectionListLabel label3 = new InspectionListLabel("id3", 3, "Label 3", null, List.of(feature3));
                List<InspectionListLabel> unorderedLabels = Arrays.asList(label1, label2, label3);
                List<InspectionListLabel> sortedLabels = InspectionList.sortLabelsAndFeatures(unorderedLabels);
                assertEquals(3, sortedLabels.size(), "Sorted list should contain three elements.");
                assertEquals(1, sortedLabels.get(0).getFeatures().getFirst().getIndex(), "First element should be Feature 1.");
                assertEquals(2, sortedLabels.get(1).getFeatures().getFirst().getIndex(), "Second element should be Feature 2.");
                assertEquals(3, sortedLabels.get(2).getFeatures().getFirst().getIndex(), "Third element should be Feature 3.");
            }

            @Test
            public void testGetDeletedFileIdsWithEmptyLists() {
                // Existing list
                InspectionList existingList = new InspectionList();
                existingList.setItems(List.of());

                // Updated list
                InspectionList updatedList = new InspectionList();
                updatedList.setItems(List.of());

                assertTrue(InspectionList.getDeletedFileIds(existingList, updatedList).isEmpty(), "Deleted file ids should be empty.");
            }

            @Test
            public void testGetDeletedFileIdsWithSameImages() {
                ObjectId fileId1 = new ObjectId();
                ObjectId fileId2 = new ObjectId();

                // Existing list
                InspectionListItemStageImage existingInspectionListItemStageImage1 = new InspectionListItemStageImage(false, fileId1);
                InspectionListItemStageImage existingInspectionListItemStageImage2 = new InspectionListItemStageImage(true, fileId2);

                InspectionListItemStage existingInspectionListItemStage1 = new InspectionListItemStage();
                existingInspectionListItemStage1.setImages(List.of(existingInspectionListItemStageImage1, existingInspectionListItemStageImage2));

                InspectionListItem existingInspectionListItem = new InspectionListItem();
                existingInspectionListItem.setStages(List.of(existingInspectionListItemStage1));

                InspectionList existingList = new InspectionList();
                existingList.setItems(List.of(existingInspectionListItem));

                // Updated list
                InspectionListItemStageImage updatedInspectionListItemStageImage1 = new InspectionListItemStageImage(false, fileId1);
                InspectionListItemStageImage updatedInspectionListItemStageImage2 = new InspectionListItemStageImage(true, fileId2);

                InspectionListItemStage updatedInspectionListItemStage1 = new InspectionListItemStage();
                updatedInspectionListItemStage1.setImages(List.of(updatedInspectionListItemStageImage1, updatedInspectionListItemStageImage2));

                InspectionListItem updatedInspectionListItem = new InspectionListItem();
                updatedInspectionListItem.setStages(List.of(updatedInspectionListItemStage1));

                InspectionList updatedList = new InspectionList();
                updatedList.setItems(List.of(updatedInspectionListItem));

                assertTrue(InspectionList.getDeletedFileIds(existingList, updatedList).isEmpty(), "Deleted file ids should be empty.");
            }

            @Test
            public void testGetDeletedFileIdsWithDeletedImage() {
                ObjectId fileId1 = new ObjectId();
                ObjectId fileId2 = new ObjectId();

                // Existing list
                InspectionListItemStageImage existingInspectionListItemStageImage1 = new InspectionListItemStageImage(false, fileId1);
                InspectionListItemStageImage existingInspectionListItemStageImage2 = new InspectionListItemStageImage(true, fileId2);

                InspectionListItemStage existingInspectionListItemStage1 = new InspectionListItemStage();
                existingInspectionListItemStage1.setImages(List.of(existingInspectionListItemStageImage1, existingInspectionListItemStageImage2));

                InspectionListItem existingInspectionListItem = new InspectionListItem();
                existingInspectionListItem.setStages(List.of(existingInspectionListItemStage1));

                InspectionList existingList = new InspectionList();
                existingList.setItems(List.of(existingInspectionListItem));

                // Updated list
                InspectionListItemStageImage updatedInspectionListItemStageImage1 = new InspectionListItemStageImage(false, fileId1);

                InspectionListItemStage updatedInspectionListItemStage1 = new InspectionListItemStage();
                updatedInspectionListItemStage1.setImages(List.of(updatedInspectionListItemStageImage1));

                InspectionListItem updatedInspectionListItem = new InspectionListItem();
                updatedInspectionListItem.setStages(List.of(updatedInspectionListItemStage1));

                InspectionList updatedList = new InspectionList();
                updatedList.setItems(List.of(updatedInspectionListItem));

                assertEquals(List.of(fileId2), InspectionList.getDeletedFileIds(existingList, updatedList), "Deleted file ids should contain one element.");
            }

            @Test
            public void testGetDeletedFileIdsWithDeletedImageAndNullFileId() {
                ObjectId fileId1 = new ObjectId();

                // Existing list
                InspectionListItemStageImage existingInspectionListItemStageImage1 = new InspectionListItemStageImage(false, fileId1);
                InspectionListItemStageImage existingInspectionListItemStageImage2 = new InspectionListItemStageImage(true, null);

                InspectionListItemStage existingInspectionListItemStage1 = new InspectionListItemStage();
                existingInspectionListItemStage1.setImages(List.of(existingInspectionListItemStageImage1, existingInspectionListItemStageImage2));

                InspectionListItem existingInspectionListItem = new InspectionListItem();
                existingInspectionListItem.setStages(List.of(existingInspectionListItemStage1));

                InspectionList existingList = new InspectionList();
                existingList.setItems(List.of(existingInspectionListItem));

                // Updated list
                InspectionListItemStageImage updatedInspectionListItemStageImage1 = new InspectionListItemStageImage(false, fileId1);

                InspectionListItemStage updatedInspectionListItemStage1 = new InspectionListItemStage();
                updatedInspectionListItemStage1.setImages(List.of(updatedInspectionListItemStageImage1));

                InspectionListItem updatedInspectionListItem = new InspectionListItem();
                updatedInspectionListItem.setStages(List.of(updatedInspectionListItemStage1));

                InspectionList updatedList = new InspectionList();
                updatedList.setItems(List.of(updatedInspectionListItem));

                assertTrue(InspectionList.getDeletedFileIds(existingList, updatedList).isEmpty(), "Deleted file ids should be empty.");
            }


            @Test
            public void testGetDeletedFileIdsWithDeletedImageAndNullFileIdInUpdatedList() {
                ObjectId fileId1 = new ObjectId();

                // Existing list
                InspectionListItemStageImage existingInspectionListItemStageImage1 = new InspectionListItemStageImage(false, fileId1);
                InspectionListItemStageImage existingInspectionListItemStageImage2 = new InspectionListItemStageImage(true, null);

                InspectionListItemStage existingInspectionListItemStage1 = new InspectionListItemStage();
                existingInspectionListItemStage1.setImages(List.of(existingInspectionListItemStageImage1, existingInspectionListItemStageImage2));

                InspectionListItem existingInspectionListItem = new InspectionListItem();
                existingInspectionListItem.setStages(List.of(existingInspectionListItemStage1));

                InspectionList existingList = new InspectionList();
                existingList.setItems(List.of(existingInspectionListItem));

                // Updated list
                InspectionListItemStageImage updatedInspectionListItemStageImage1 = new InspectionListItemStageImage(false, fileId1);

                InspectionListItemStage updatedInspectionListItemStage1 = new InspectionListItemStage();
                updatedInspectionListItemStage1.setImages(List.of(updatedInspectionListItemStageImage1));

                InspectionListItem updatedInspectionListItem = new InspectionListItem();
                updatedInspectionListItem.setStages(List.of(updatedInspectionListItemStage1));

                InspectionList updatedList = new InspectionList();
                updatedList.setItems(List.of(updatedInspectionListItem));

                assertTrue(InspectionList.getDeletedFileIds(existingList, updatedList).isEmpty(), "Deleted file ids should be empty.");
            }

            @Test
            public void testGetAllFileIdsWithEmptyList() {
                InspectionList list = new InspectionList();
                list.setItems(List.of());

                assertTrue(InspectionList.getAllFileIds(list).isEmpty(), "File ids should be empty.");
            }

            @Test
            public void testGetAllFileIdsWithMultipleItems() {
                ObjectId fileId1 = new ObjectId();
                ObjectId fileId2 = new ObjectId();
                ObjectId fileId3 = new ObjectId();
                ObjectId fileId4 = new ObjectId();

                InspectionListItemStageImage stageImage1 = new InspectionListItemStageImage(false, fileId1);
                InspectionListItemStageImage stageImage2 = new InspectionListItemStageImage(true, fileId2);
                InspectionListItemStageImage stageImage3 = new InspectionListItemStageImage(false, fileId3);
                InspectionListItemStageImage stageImage4 = new InspectionListItemStageImage(true, fileId4);

                InspectionListItemStage stage1 = new InspectionListItemStage();
                stage1.setImages(List.of(stageImage1, stageImage2));
                InspectionListItemStage stage2 = new InspectionListItemStage();
                stage2.setImages(List.of(stageImage3, stageImage4));

                InspectionListItem item1 = new InspectionListItem();
                item1.setStages(List.of(stage1));
                InspectionListItem item2 = new InspectionListItem();
                item2.setStages(List.of(stage2));

                InspectionList list = new InspectionList();
                list.setItems(List.of(item1, item2));

                assertEquals(List.of(fileId1, fileId2, fileId3, fileId4), InspectionList.getAllFileIds(list), "File ids should contain all file ids.");
            }

            @Test
            public void testGetAllFileIdsWithNullFileId() {
                ObjectId fileId1 = new ObjectId();
                ObjectId fileId2 = new ObjectId();
                ObjectId fileId3 = new ObjectId();

                InspectionListItemStageImage stageImage1 = new InspectionListItemStageImage(false, fileId1);
                InspectionListItemStageImage stageImage2 = new InspectionListItemStageImage(true, fileId2);
                InspectionListItemStageImage stageImage3 = new InspectionListItemStageImage(false, fileId3);
                InspectionListItemStageImage stageImage4 = new InspectionListItemStageImage(true, null);

                InspectionListItemStage stage1 = new InspectionListItemStage();
                stage1.setImages(List.of(stageImage1, stageImage2));
                InspectionListItemStage stage2 = new InspectionListItemStage();
                stage2.setImages(List.of(stageImage3, stageImage4));

                InspectionListItem item1 = new InspectionListItem();
                item1.setStages(List.of(stage1));
                InspectionListItem item2 = new InspectionListItem();
                item2.setStages(List.of(stage2));

                InspectionList list = new InspectionList();
                list.setItems(List.of(item1, item2));

                assertEquals(List.of(fileId1, fileId2, fileId3), InspectionList.getAllFileIds(list), "File ids should contain all file ids except null.");
            }
        }
    }
}
