package nl.rgs.kib.models;

import nl.rgs.kib.model.list.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InspectionListTest {

    @Test
    public void testSortItemsAndStagesWithEmptyList() {
        assertTrue(InspectionList.sortItemsAndStages(List.of()).isEmpty(), "Sorted list should be empty.");
    }

    @Test
    public void testSortItemsAndStagesWithMultipleItems() {
        InspectionListItemStage stage1 = new InspectionListItemStage();
        stage1.setStage(1);
        stage1.setName("Stage 1");
        InspectionListItemStage stage2 = new InspectionListItemStage();
        stage2.setStage(2);
        stage2.setName("Stage 2");

        InspectionListItem item1 = new InspectionListItem(2, "id1", "Item 1", null, null, null, Arrays.asList(stage2, stage1));
        InspectionListItem item2 = new InspectionListItem(1, "id2", "Item 2", null, null, null, Arrays.asList(stage1, stage2));
        List<InspectionListItem> sortedItems = InspectionList.sortItemsAndStages(Arrays.asList(item1, item2));

        assertEquals("id2", sortedItems.get(0).getId(), "First item should be 'Item 2'.");
        assertEquals("id1", sortedItems.get(1).getId(), "Second item should be 'Item 1'.");
        assertEquals(Integer.valueOf(1), sortedItems.get(0).getStages().getFirst().getStage(), "First stage of first item should be in order.");
    }

    @Test
    public void testSortItemsAndStagesWithSingleItem() {
        InspectionListItemStage stage1 = new InspectionListItemStage();
        stage1.setStage(1);
        stage1.setName("Stage 1");
        InspectionListItem item = new InspectionListItem(1, "id1", "Item 1", null, null, null, Arrays.asList(stage1));
        List<InspectionListItem> sortedItems = InspectionList.sortItemsAndStages(List.of(item));
        assertEquals(1, sortedItems.size(), "Sorted list should contain one element.");
        assertEquals(stage1, sortedItems.getFirst().getStages().getFirst(), "The single element should match the original.");
    }

    @Test
    public void testSortItemsAndStagesWithUnorderedItems() {
        InspectionListItemStage stage1 = new InspectionListItemStage();
        stage1.setStage(5);
        stage1.setName("Stage 5");
        InspectionListItemStage stage2 = new InspectionListItemStage();
        stage2.setStage(1);
        stage2.setName("Stage 1");
        InspectionListItemStage stage3 = new InspectionListItemStage();
        stage3.setStage(3);
        stage3.setName("Stage 3");
        InspectionListItem item1 = new InspectionListItem(5, "id1", "Item 5", null, null, null, List.of(stage1));
        InspectionListItem item2 = new InspectionListItem(1, "id2", "Item 1", null, null, null, List.of(stage2));
        InspectionListItem item3 = new InspectionListItem(3, "id3", "Item 3", null, null, null, List.of(stage3));
        List<InspectionListItem> unorderedItems = Arrays.asList(item1, item2, item3);
        List<InspectionListItem> sortedItems = InspectionList.sortItemsAndStages(unorderedItems);
        assertEquals(3, sortedItems.size(), "Sorted list should contain three elements.");
        assertEquals(1, sortedItems.get(0).getStages().getFirst().getStage(), "First element should be Stage 1.");
        assertEquals(3, sortedItems.get(1).getStages().getFirst().getStage(), "Second element should be Stage 3.");
        assertEquals(5, sortedItems.get(2).getStages().getFirst().getStage(), "Third element should be Stage 5.");
    }

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
}