package nl.rgs.kib.models;

import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.model.method.InspectionMethodStage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InspectionMethodTest {

    @Test
    public void testSortStagesWithEmptyList() {
        List<InspectionMethodStage> emptyList = new ArrayList<>();
        List<InspectionMethodStage> sortedList = InspectionMethod.sortStages(emptyList);
        assertTrue(sortedList.isEmpty(), "Sorted list should be empty.");
    }

    @Test
    public void testSortStagesWithSingleElement() {
        InspectionMethodStage singleStage = new InspectionMethodStage(1, "Single Stage");
        List<InspectionMethodStage> singleList = List.of(singleStage);
        List<InspectionMethodStage> sortedList = InspectionMethod.sortStages(singleList);
        assertEquals(1, sortedList.size(), "Sorted list should contain one element.");
        assertEquals(singleStage, sortedList.getFirst(), "The single element should match the original.");
    }

    @Test
    public void testSortStagesWithElementsInReverseOrder() {
        InspectionMethodStage stage1 = new InspectionMethodStage(3, "Stage 3");
        InspectionMethodStage stage2 = new InspectionMethodStage(2, "Stage 2");
        InspectionMethodStage stage3 = new InspectionMethodStage(1, "Stage 1");
        List<InspectionMethodStage> reverseList = Arrays.asList(stage1, stage2, stage3);
        List<InspectionMethodStage> sortedList = InspectionMethod.sortStages(reverseList);
        assertEquals(3, sortedList.size(), "Sorted list should contain three elements.");
        assertTrue(sortedList.get(0).getStage() < sortedList.get(1).getStage() && sortedList.get(1).getStage() < sortedList.get(2).getStage(), "Elements should be in ascending order.");
    }

    @Test
    public void testSortStagesWithUnorderedElements() {
        InspectionMethodStage stage1 = new InspectionMethodStage(5, "Stage 5");
        InspectionMethodStage stage2 = new InspectionMethodStage(1, "Stage 1");
        InspectionMethodStage stage3 = new InspectionMethodStage(3, "Stage 3");
        List<InspectionMethodStage> unorderedList = Arrays.asList(stage1, stage2, stage3);
        List<InspectionMethodStage> sortedList = InspectionMethod.sortStages(unorderedList);
        assertEquals(3, sortedList.size(), "Sorted list should contain three elements.");
        assertEquals(1, sortedList.get(0).getStage(), "First element should be Stage 1.");
        assertEquals(3, sortedList.get(1).getStage(), "Second element should be Stage 3.");
        assertEquals(5, sortedList.get(2).getStage(), "Third element should be Stage 5.");
    }

    @Test
    public void testSortStagesWithOrderedElements() {
        InspectionMethodStage stage1 = new InspectionMethodStage(1, "Stage 1");
        InspectionMethodStage stage2 = new InspectionMethodStage(3, "Stage 3");
        InspectionMethodStage stage3 = new InspectionMethodStage(5, "Stage 5");
        List<InspectionMethodStage> orderedList = Arrays.asList(stage1, stage2, stage3);
        List<InspectionMethodStage> sortedList = InspectionMethod.sortStages(orderedList);
        assertEquals(3, sortedList.size(), "Sorted list should contain three elements.");
        assertEquals(1, sortedList.get(0).getStage(), "First element should be Stage 1.");
        assertEquals(3, sortedList.get(1).getStage(), "Second element should be Stage 3.");
        assertEquals(5, sortedList.get(2).getStage(), "Third element should be Stage 5.");
    }

    @Test
    public void testSortStagesWithDuplicateElements() {
        InspectionMethodStage stage1 = new InspectionMethodStage(1, "Stage 1");
        InspectionMethodStage stage2 = new InspectionMethodStage(3, "Stage 3");
        InspectionMethodStage stage3 = new InspectionMethodStage(1, "Stage 1");
        List<InspectionMethodStage> duplicateList = Arrays.asList(stage1, stage2, stage3);
        List<InspectionMethodStage> sortedList = InspectionMethod.sortStages(duplicateList);
        assertEquals(3, sortedList.size(), "Sorted list should contain three elements.");
        assertEquals(1, sortedList.get(0).getStage(), "First element should be Stage 1.");
        assertEquals(1, sortedList.get(1).getStage(), "Second element should be Stage 1.");
        assertEquals(3, sortedList.get(2).getStage(), "Third element should be Stage 3.");
    }
}