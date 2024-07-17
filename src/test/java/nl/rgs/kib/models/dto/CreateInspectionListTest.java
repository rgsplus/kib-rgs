package nl.rgs.kib.models.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.rgs.kib.model.list.*;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.model.method.InspectionMethodCalculationMethod;
import nl.rgs.kib.model.method.InspectionMethodInput;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateInspectionListTest {

    InspectionMethod inspectionMethod;

    @BeforeEach
    public void setUp() {
        inspectionMethod = new InspectionMethod();
        inspectionMethod.setId(new ObjectId().toHexString());
        inspectionMethod.setName("test");
        inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
        inspectionMethod.setInput(InspectionMethodInput.PERCENTAGE);
        inspectionMethod.setStages(List.of());
    }

    @Nested
    public class CreateInspectionListValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        @Test
        public void testCreateInspectionListNameNotNullValidator() {
            CreateInspectionList inspectionList = new CreateInspectionList(
                    null,
                    InspectionListStatus.DEFINITIVE,
                    List.of(),
                    List.of()
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
        }

        @Test
        public void testCreateInspectionListNameNotBlankValidator() {
            CreateInspectionList inspectionList = new CreateInspectionList(
                    " ",
                    InspectionListStatus.DEFINITIVE,
                    List.of(),
                    List.of()
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
        }

        @Test
        public void testCreateInspectionListStatusNotNullValidator() {
            CreateInspectionList inspectionList = new CreateInspectionList(
                    "test",
                    null,
                    List.of(),
                    List.of()
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Status should not be null.");
        }

        @Test
        public void testCreateInspectionListItemsNotNullValidator() {
            CreateInspectionList inspectionList = new CreateInspectionList(
                    "test",
                    InspectionListStatus.DEFINITIVE,
                    null,
                    List.of()
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Items should not be null.");
        }

        @Test
        public void testCreateInspectionListItemsUniqueIdsValidator() {
            InspectionListItem item1 = new InspectionListItem(1, "id1", "Item 1", null, InspectionListItemCategory.SERIOUS, inspectionMethod, List.of());
            InspectionListItem item2 = new InspectionListItem(2, "id1", "Item 2", null, InspectionListItemCategory.SERIOUS, inspectionMethod, List.of());

            CreateInspectionList inspectionList = new CreateInspectionList(
                    "test",
                    InspectionListStatus.DEFINITIVE,
                    List.of(item1, item2),
                    List.of()
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Items should have unique ids.");
        }

        @Test
        public void testCreateInspectionListItemsUniqueIndexesValidator() {
            InspectionListItem item1 = new InspectionListItem(1, "id1", "Item 1", null, InspectionListItemCategory.SERIOUS, inspectionMethod, List.of());
            InspectionListItem item2 = new InspectionListItem(1, "id2", "Item 2", null, InspectionListItemCategory.SERIOUS, inspectionMethod, List.of());

            CreateInspectionList inspectionList = new CreateInspectionList(
                    "test",
                    InspectionListStatus.DEFINITIVE,
                    List.of(item1, item2),
                    List.of()
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Items should have unique indexes.");
        }

        @Test
        public void testCreateInspectionListLabelsNotNullValidator() {
            CreateInspectionList inspectionList = new CreateInspectionList(
                    "test",
                    InspectionListStatus.DEFINITIVE,
                    List.of(),
                    null
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Labels should not be null.");
        }

        @Test
        public void testCreateInspectionListLabelsUniqueIdsValidator() {
            InspectionListLabel label1 = new InspectionListLabel("id1", 1, "Label 1", null, List.of());
            InspectionListLabel label2 = new InspectionListLabel("id1", 2, "Label 2", null, List.of());

            CreateInspectionList inspectionList = new CreateInspectionList(
                    "test",
                    InspectionListStatus.DEFINITIVE,
                    List.of(),
                    List.of(label1, label2)
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Labels should have unique ids.");
        }

        @Test
        public void testCreateInspectionListLabelsUniqueIndexesValidator() {
            InspectionListLabel label1 = new InspectionListLabel("id1", 1, "Label 1", null, List.of());
            InspectionListLabel label2 = new InspectionListLabel("id2", 1, "Label 2", null, List.of());

            CreateInspectionList inspectionList = new CreateInspectionList(
                    "test",
                    InspectionListStatus.DEFINITIVE,
                    List.of(),
                    List.of(label1, label2)
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Labels should have unique indexes.");
        }

        @Nested
        public class CreateInspectionListItemValidations {
            @Test
            public void testCreateInspectionListItemIndexNotNullValidator() {
                InspectionListItem item = new InspectionListItem();
                item.setId("id");
                item.setName("test");
                item.setGroup("group");
                item.setCategory(InspectionListItemCategory.SERIOUS);
                item.setInspectionMethod(inspectionMethod);
                item.setStages(List.of());

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item),
                        List.of()
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Index should not be null.");
            }

            @Test
            public void testCreateInspectionListItemIndexMinValidator() {
                InspectionListItem item = new InspectionListItem();
                item.setIndex(-1);
                item.setId("id");
                item.setName("test");
                item.setGroup("group");
                item.setCategory(InspectionListItemCategory.SERIOUS);
                item.setInspectionMethod(inspectionMethod);
                item.setStages(List.of());

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item),
                        List.of()
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Index should be greater than or equal to 0.");
            }

            @Test
            public void testCreateInspectionListItemIdNotNullValidator() {
                InspectionListItem item = new InspectionListItem();
                item.setIndex(1);
                item.setName("test");
                item.setGroup("group");
                item.setCategory(InspectionListItemCategory.SERIOUS);
                item.setInspectionMethod(inspectionMethod);
                item.setStages(List.of());

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item),
                        List.of()
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Id should not be null.");
            }

            @Test
            public void testCreateInspectionListItemNameNotNullValidator() {
                InspectionListItem item = new InspectionListItem();
                item.setIndex(1);
                item.setId("id");
                item.setGroup("group");
                item.setCategory(InspectionListItemCategory.SERIOUS);
                item.setInspectionMethod(inspectionMethod);
                item.setStages(List.of());

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item),
                        List.of()
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
            }

            @Test
            public void testCreateInspectionListItemNameNotBlankValidator() {
                InspectionListItem item = new InspectionListItem();
                item.setIndex(1);
                item.setId("id");
                item.setName(" ");
                item.setGroup("group");
                item.setCategory(InspectionListItemCategory.SERIOUS);
                item.setInspectionMethod(inspectionMethod);
                item.setStages(List.of());

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item),
                        List.of()
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
            }

            @Test
            public void testCreateInspectionListItemCategoryNotNullValidator() {
                InspectionListItem item = new InspectionListItem();
                item.setIndex(1);
                item.setId("id");
                item.setName("test");
                item.setGroup("group");
                item.setInspectionMethod(inspectionMethod);
                item.setStages(List.of());

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item),
                        List.of()
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Category should not be null.");
            }

            @Test
            public void testCreateInspectionListItemInspectionMethodNotNullValidator() {
                InspectionListItem item = new InspectionListItem();
                item.setIndex(1);
                item.setId("id");
                item.setName("test");
                item.setGroup("group");
                item.setCategory(InspectionListItemCategory.SERIOUS);
                item.setStages(List.of());

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item),
                        List.of()
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Inspection method should not be null.");
            }

            @Test
            public void testCreateInspectionListItemStagesNotNullValidator() {
                InspectionListItem item = new InspectionListItem();
                item.setIndex(1);
                item.setId("id");
                item.setName("test");
                item.setGroup("group");
                item.setCategory(InspectionListItemCategory.SERIOUS);
                item.setInspectionMethod(inspectionMethod);
                item.setStages(null);

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item),
                        List.of()
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Stages should not be null.");
            }

            @Test
            public void testCreateInspectionListItemStagesUniqueStagesValidator() {
                InspectionListItemStage stage1 = new InspectionListItemStage();
                stage1.setStage(1);
                stage1.setName("Stage 1");
                InspectionListItemStage stage2 = new InspectionListItemStage();
                stage2.setStage(1);
                stage2.setName("Stage 2");
                InspectionListItem item = new InspectionListItem(1, "id", "Item", null, InspectionListItemCategory.SERIOUS, inspectionMethod, Arrays.asList(stage1, stage2));

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item),
                        List.of()
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Stages should have unique stages.");
            }

            @Nested
            public class CreateInspectionListItemStageValidations {
                @Test
                public void testCreateInspectionListItemStageStageNotNullValidator() {
                    InspectionListItemStage stage = new InspectionListItemStage();
                    stage.setName("Stage 1");
                    stage.setMax(25.0);
                    stage.setImage("https://example.com/image.jpg");

                    InspectionListItem item = new InspectionListItem(1, "id", "Item", null, InspectionListItemCategory.SERIOUS, inspectionMethod, List.of(stage));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item),
                            List.of()
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Stage should not be null.");
                }

                @Test
                public void testCreateInspectionListItemStageStageMin1Validator() {
                    InspectionListItemStage stage = new InspectionListItemStage();
                    stage.setStage(-1);
                    stage.setName("Stage 1");
                    stage.setMax(25.0);
                    stage.setImage("https://example.com/image.jpg");

                    InspectionListItem item = new InspectionListItem(1, "id", "Item", null, InspectionListItemCategory.SERIOUS, inspectionMethod, List.of(stage));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item),
                            List.of()
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Stage should be greater than or equal to 0.");
                }

                @Test
                public void testCreateInspectionListItemStageStageMax100Validator() {
                    InspectionListItemStage stage = new InspectionListItemStage();
                    stage.setStage(10);
                    stage.setName("Stage 1");
                    stage.setMax(250.0);
                    stage.setImage("https://example.com/image.jpg");

                    InspectionListItem item = new InspectionListItem(1, "id", "Item", null, InspectionListItemCategory.SERIOUS, inspectionMethod, List.of(stage));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item),
                            List.of()
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Stage should be less than or equal to 9.");
                }

                @Test
                public void testCreateInspectionListItemStageNameNotNullValidator() {
                    InspectionListItemStage stage = new InspectionListItemStage();
                    stage.setStage(1);
                    stage.setMax(25.0);
                    stage.setImage("https://example.com/image.jpg");

                    InspectionListItem item = new InspectionListItem(1, "id", "Item", null, InspectionListItemCategory.SERIOUS, inspectionMethod, List.of(stage));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item),
                            List.of()
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
                }

                @Test
                public void testCreateInspectionListItemStageNameNotBlankValidator() {
                    InspectionListItemStage stage = new InspectionListItemStage();
                    stage.setStage(1);
                    stage.setName(" ");
                    stage.setMax(25.0);
                    stage.setImage("https://example.com/image.jpg");

                    InspectionListItem item = new InspectionListItem(1, "id", "Item", null, InspectionListItemCategory.SERIOUS, inspectionMethod, List.of(stage));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item),
                            List.of()
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
                }

                @Test
                public void testCreateInspectionListItemStageMaxMinValidator() {
                    InspectionListItemStage stage = new InspectionListItemStage();
                    stage.setStage(1);
                    stage.setName("Stage 1");
                    stage.setMax(-1.0);
                    stage.setImage("https://example.com/image.jpg");

                    InspectionListItem item = new InspectionListItem(1, "id", "Item", null, InspectionListItemCategory.SERIOUS, inspectionMethod, List.of(stage));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item),
                            List.of()
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Max should be greater than or equal to 0.");
                }

                @Test
                public void testCreateInspectionListItemStageMaxMaxValidator() {
                    InspectionListItemStage stage = new InspectionListItemStage();
                    stage.setStage(1);
                    stage.setName("Stage 1");
                    stage.setMax(101.0);
                    stage.setImage("https://example.com/image.jpg");

                    InspectionListItem item = new InspectionListItem(1, "id", "Item", null, InspectionListItemCategory.SERIOUS, inspectionMethod, List.of(stage));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item),
                            List.of()
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Max should be less than or equal to 100.");
                }

                @Test
                public void testCreateInspectionListItemStageImageURLValidator() {
                    InspectionListItemStage stage = new InspectionListItemStage();
                    stage.setStage(1);
                    stage.setName("Stage 1");
                    stage.setMax(25.0);
                    stage.setImage("example.com/image.jpg");

                    InspectionListItem item = new InspectionListItem(1, "id", "Item", null, InspectionListItemCategory.SERIOUS, inspectionMethod, List.of(stage));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item),
                            List.of()
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Image should be a valid URL.");
                }
            }
        }

        @Nested
        public class CreateInspectionListLabelValidations {
            @Test
            public void testCreateInspectionListLabelIdNotNullValidator() {
                InspectionListLabel label = new InspectionListLabel();
                label.setIndex(1);
                label.setName("test");
                label.setGroup("group");
                label.setFeatures(List.of());

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(),
                        List.of(label)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Id should not be null.");
            }

            @Test
            public void testCreateInspectionListLabelIndexNotNullValidator() {
                InspectionListLabel label = new InspectionListLabel();
                label.setId("id");
                label.setName("test");
                label.setGroup("group");
                label.setFeatures(List.of());

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(),
                        List.of(label)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Index should not be null.");
            }

            @Test
            public void testCreateInspectionListLabelIndexMinValidator() {
                InspectionListLabel label = new InspectionListLabel();
                label.setId("id");
                label.setIndex(-1);
                label.setName("test");
                label.setGroup("group");
                label.setFeatures(List.of());

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(),
                        List.of(label)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Index should be greater than or equal to 0.");
            }

            @Test
            public void testCreateInspectionListLabelNameNotNullValidator() {
                InspectionListLabel label = new InspectionListLabel();
                label.setId("id");
                label.setIndex(1);
                label.setGroup("group");
                label.setFeatures(List.of());

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(),
                        List.of(label)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
            }

            @Test
            public void testCreateInspectionListLabelNameNotBlankValidator() {
                InspectionListLabel label = new InspectionListLabel();
                label.setId("id");
                label.setIndex(1);
                label.setName(" ");
                label.setGroup("group");
                label.setFeatures(List.of());

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(),
                        List.of(label)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
            }

            @Test
            public void testCreateInspectionListLabelFeaturesNotNullValidator() {
                InspectionListLabel label = new InspectionListLabel();
                label.setId("id");
                label.setIndex(1);
                label.setName("test");
                label.setGroup("group");

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(),
                        List.of(label)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Features should not be null.");
            }

            @Test
            public void testCreateInspectionListLabelFeaturesUniqueIndexesValidator() {
                InspectionListLabelFeature feature1 = new InspectionListLabelFeature(1, "Feature 1");
                InspectionListLabelFeature feature2 = new InspectionListLabelFeature(1, "Feature 2");
                InspectionListLabel label = new InspectionListLabel("id", 1, "Label", null, List.of(feature1, feature2));

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(),
                        List.of(label)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Features should have unique indexes.");
            }

            @Nested
            public class CreateInspectionListLabelFeatureValidations {
                @Test
                public void testCreateInspectionListLabelFeatureIndexNotNullValidator() {
                    InspectionListLabelFeature feature = new InspectionListLabelFeature();
                    feature.setName("test");

                    InspectionListLabel label = new InspectionListLabel("id", 1, "Label", null, List.of(feature));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(),
                            List.of(label)
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Index should not be null.");
                }

                @Test
                public void testCreateInspectionListLabelFeatureIndexMinValidator() {
                    InspectionListLabelFeature feature = new InspectionListLabelFeature();
                    feature.setIndex(-1);
                    feature.setName("test");

                    InspectionListLabel label = new InspectionListLabel("id", 1, "Label", null, List.of(feature));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(),
                            List.of(label)
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Index should be greater than or equal to 0.");
                }

                @Test
                public void testCreateInspectionListLabelFeatureNameNotNullValidator() {
                    InspectionListLabelFeature feature = new InspectionListLabelFeature();
                    feature.setIndex(1);

                    InspectionListLabel label = new InspectionListLabel("id", 1, "Label", null, List.of(feature));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(),
                            List.of(label)
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
                }

                @Test
                public void testCreateInspectionListLabelFeatureNameNotBlankValidator() {
                    InspectionListLabelFeature feature = new InspectionListLabelFeature();
                    feature.setIndex(1);
                    feature.setName(" ");

                    InspectionListLabel label = new InspectionListLabel("id", 1, "Label", null, List.of(feature));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(),
                            List.of(label)
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
                }
            }
        }
    }
}
