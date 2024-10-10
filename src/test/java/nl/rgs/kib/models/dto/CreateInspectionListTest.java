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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateInspectionListTest {

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
                name, new ObjectId().toHexString(), "Visuele beoordeling fundering door gevel en vloeren", "Deze inspectie is bedoeld om de constructieve staat van de fundering en gevelmetselwerk te beoordelen.", inspectionMethod, List.of(stage1, stage2));
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
    public class CreateInspectionListValidations {

        private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        @Test
        public void testCreateInspectionListNameNotNullValidator() {
            CreateInspectionList inspectionList = new CreateInspectionList(
                    null,
                    InspectionListStatus.DEFINITIVE,
                    List.of()
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
        }

        @Test
        public void testCreateInspectionListNameNotBlankValidator() {
            CreateInspectionList inspectionList = new CreateInspectionList(
                    " ",
                    InspectionListStatus.DEFINITIVE,
                    List.of()
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
        }

        @Test
        public void testCreateInspectionListStatusNotNullValidator() {
            CreateInspectionList inspectionList = new CreateInspectionList(
                    "test",
                    null,
                    List.of()
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Status should not be null.");
        }

        @Test
        public void testCreateInspectionListItemsNotNullValidator() {
            CreateInspectionList inspectionList = new CreateInspectionList(
                    "test",
                    InspectionListStatus.DEFINITIVE,
                    null
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Items should not be null.");
        }

        @Test
        public void testCreateInspectionListItemsUniqueIdsValidator() {
            InspectionListItem item1 = createInspectionListItem("Constructieve staat fundering en gevelmetselwerk", "id1", 0);
            InspectionListItem item2 = createInspectionListItem("Hout(rot)schade in elementen", "id1", 1);

            CreateInspectionList inspectionList = new CreateInspectionList(
                    "test",
                    InspectionListStatus.DEFINITIVE,
                    List.of(item1, item2)
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Items should have unique ids.");
        }

        @Test
        public void testCreateInspectionListItemsValidIndexesValidator() {
            InspectionListItem item1 = createInspectionListItem("Constructieve staat fundering en gevelmetselwerk", "id1", 0);
            InspectionListItem item2 = createInspectionListItem("Hout(rot)schade in elementen", "id2", 0);

            CreateInspectionList inspectionList = new CreateInspectionList(
                    "test",
                    InspectionListStatus.DEFINITIVE,
                    List.of(item1, item2)
            );

            assertEquals(1, validator.validate(inspectionList).size(), "Items should have unique indexes.");
        }

        @Test
        public void testInspectionListItemsUniqueStandardNosValidator() {
            InspectionListItem item1 = createInspectionListItem("Item 1", 0);
            InspectionListItem item2 = createInspectionListItem("Item 2", 1);

            InspectionList inspectionList = new InspectionList();
            inspectionList.setId(new ObjectId().toHexString());
            inspectionList.setName("test");
            inspectionList.setItems(List.of(item1, item2));
            inspectionList.setStatus(InspectionListStatus.DEFINITIVE);

            inspectionList.getItems().get(0).setStandardNo("standardNo");
            inspectionList.getItems().get(1).setStandardNo("standardNo");

            assertEquals(1, validator.validate(inspectionList).size(), "Items should have unique standardNos.");
        }

        @Nested
        public class CreateInspectionListItemValidations {
            @Test
            public void testCreateInspectionListItemIndexNotNullValidator() {
                InspectionListItemStage stage1 = new InspectionListItemStage();
                stage1.setStage(1);
                stage1.setName("Stage 1");
                stage1.setImages(List.of());

                InspectionListItemStage stage2 = new InspectionListItemStage();
                stage2.setStage(2);
                stage2.setName("Stage 2");
                stage2.setImages(List.of());

                InspectionListItem item = new InspectionListItem();
                item.setId("id");
                item.setName("test");
                item.setGroup("group");
                item.setInspectionMethod(inspectionMethod);
                item.setStages(List.of(stage1, stage2));

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item)
                );

                assertEquals(2, validator.validate(inspectionList).size(), "Index should not be null.");
            }

            @Test
            public void testCreateInspectionListItemIndexMinValidator() {
                InspectionListItemStage stage1 = new InspectionListItemStage();
                stage1.setStage(1);
                stage1.setName("Stage 1");
                stage1.setImages(List.of());

                InspectionListItemStage stage2 = new InspectionListItemStage();
                stage2.setStage(2);
                stage2.setName("Stage 2");
                stage2.setImages(List.of());

                InspectionListItem item = new InspectionListItem();
                item.setIndex(-1);
                item.setId("id");
                item.setName("test");
                item.setGroup("group");
                item.setInspectionMethod(inspectionMethod);
                item.setStages(List.of(stage1, stage2));

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item)
                );

                assertEquals(2, validator.validate(inspectionList).size(), "Index should be greater than or equal to 0.");
            }

            @Test
            public void testCreateInspectionListItemIdNotNullValidator() {
                InspectionListItemStage stage1 = new InspectionListItemStage();
                stage1.setStage(1);
                stage1.setName("Stage 1");
                stage1.setImages(List.of());

                InspectionListItemStage stage2 = new InspectionListItemStage();
                stage2.setStage(2);
                stage2.setName("Stage 2");
                stage2.setImages(List.of());

                InspectionListItem item = new InspectionListItem();
                item.setIndex(0);
                item.setName("test");
                item.setGroup("group");
                item.setInspectionMethod(inspectionMethod);
                item.setStages(List.of(stage1, stage2));

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Id should not be null.");
            }

            @Test
            public void testCreateInspectionListItemNameNotNullValidator() {
                InspectionListItem item = createInspectionListItem("test");
                item.setName(null);

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
            }

            @Test
            public void testCreateInspectionListItemNameNotBlankValidator() {
                InspectionListItem item = createInspectionListItem("test");
                item.setName(" ");

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
            }

            @Test
            public void testCreateInspectionListItemInspectionMethodNotNullValidator() {
                InspectionListItem item = createInspectionListItem("test");
                item.setInspectionMethod(null);

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Inspection method should not be null.");
            }

            @Test
            public void testCreateInspectionListItemStagesNotNullValidator() {
                InspectionListItem item = createInspectionListItem("test");
                item.setStages(null);

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Stages should not be null.");
            }

            @Test
            public void testCreateInspectionListItemStagesUniqueStagesValidator() {
                InspectionListItem item = createInspectionListItem("test");
                item.getStages().get(0).setStage(1);
                item.getStages().get(1).setStage(1);

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Stages should have unique stages.");
            }

            @Test
            public void testCreateInspectionListItemStagesMinLength2Validator() {
                InspectionListItem item = createInspectionListItem("test");
                item.setStages(List.of());

                CreateInspectionList inspectionList = new CreateInspectionList(
                        "test",
                        InspectionListStatus.DEFINITIVE,
                        List.of(item)
                );

                assertEquals(1, validator.validate(inspectionList).size(), "Stages should have at least 2 stages.");
            }

            @Nested
            public class CreateInspectionListItemStageValidations {
                @Test
                public void testCreateInspectionListItemStageStageNotNullValidator() {
                    InspectionListItem item = createInspectionListItem("test");
                    item.getStages().getFirst().setStage(null);

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item)
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Stage should not be null.");
                }

                @Test
                public void testCreateInspectionListItemStageStageMin1Validator() {
                    InspectionListItem item = createInspectionListItem("test");
                    item.getStages().getFirst().setStage(0);

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item)
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Stage should be greater than or equal to 0.");
                }

                @Test
                public void testCreateInspectionListItemStageStageMax100Validator() {
                    InspectionListItem item = createInspectionListItem("test");
                    item.getStages().getFirst().setStage(101);

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item)
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Stage should be less than or equal to 9.");
                }

                @Test
                public void testCreateInspectionListItemStageNameNotNullValidator() {
                    InspectionListItem item = createInspectionListItem("test");
                    item.getStages().getFirst().setName(null);

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item)
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
                }

                @Test
                public void testCreateInspectionListItemStageNameNotBlankValidator() {
                    InspectionListItem item = createInspectionListItem("test");
                    item.getStages().getFirst().setName(" ");

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item)
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Name should not be blank.");
                }

                @Test
                public void testCreateInspectionListItemStageImagesNotNullValidator() {
                    InspectionListItem item = createInspectionListItem("test");
                    item.getStages().getFirst().setImages(null);

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item)
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Images should not be null.");
                }

                @Test
                public void testCreateInspectionListItemStageImagesMainImageValidator() {
                    InspectionListItem item = createInspectionListItem("test");
                    InspectionListItemStageImage image1 = new InspectionListItemStageImage();
                    image1.setMain(false);
                    image1.setFileId(new ObjectId().toHexString());

                    InspectionListItemStageImage image2 = new InspectionListItemStageImage();
                    image2.setMain(false);
                    image2.setFileId(new ObjectId().toHexString());

                    item.getStages().getFirst().setImages(List.of(image1, image2));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item)
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "Main image should be unique.");
                }

                @Test
                public void testCreateInspectionListItemStageImagesUniqueFileIdsValidator() {
                    InspectionListItem item = createInspectionListItem("test");

                    InspectionListItemStageImage image1 = new InspectionListItemStageImage();
                    image1.setMain(true);
                    image1.setFileId(new ObjectId().toHexString());

                    InspectionListItemStageImage image2 = new InspectionListItemStageImage();
                    image2.setMain(false);
                    image2.setFileId(image1.getFileId());

                    item.getStages().getFirst().setImages(List.of(image1, image2));

                    CreateInspectionList inspectionList = new CreateInspectionList(
                            "test",
                            InspectionListStatus.DEFINITIVE,
                            List.of(item)
                    );

                    assertEquals(1, validator.validate(inspectionList).size(), "File ids should be unique.");
                }

                @Nested
                public class CreateInspectionListItemStageImageValidations {

                    @Test
                    public void testCreateInspectionListItemStageImageFileIdNotNullValidator() {
                        InspectionListItemStageImage image = new InspectionListItemStageImage();
                        image.setMain(true);
                        image.setFileId(null);

                        InspectionListItemStage stage1 = new InspectionListItemStage();
                        stage1.setStage(1);
                        stage1.setName("Stage 1");
                        stage1.setImages(List.of(image));

                        InspectionListItemStage stage2 = new InspectionListItemStage();
                        stage2.setStage(2);
                        stage2.setName("Stage 2");
                        stage2.setImages(List.of());

                        InspectionListItem item = createInspectionListItem("test");
                        item.setStages(List.of(stage1, stage2));

                        CreateInspectionList inspectionList = new CreateInspectionList(
                                "test",
                                InspectionListStatus.DEFINITIVE,
                                List.of(item)
                        );

                        assertEquals(1, validator.validate(inspectionList).size(), "File id should not be null.");
                    }
                }
            }
        }
    }
}
