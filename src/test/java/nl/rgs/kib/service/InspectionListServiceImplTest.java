package nl.rgs.kib.service;

import nl.rgs.kib.model.file.KibFile;
import nl.rgs.kib.model.list.*;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import nl.rgs.kib.repository.InspectionListRepository;
import nl.rgs.kib.service.impl.InspectionListServiceImpl;
import nl.rgs.kib.service.impl.KibFileServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InspectionListServiceImplTest {

    @Mock
    private InspectionListRepository inspectionListRepository;

    @InjectMocks
    private InspectionListServiceImpl inspectionListService;

    @Mock
    private KibFileServiceImpl kibFileService;

    @Test
    public void count_ReturnsCount() {
        long expected = 5;
        when(inspectionListRepository.count()).thenReturn(expected);

        long result = inspectionListService.count();

        assertEquals(expected, result);
        verify(inspectionListRepository).count();
    }

    @Test
    public void findAll_ReturnsAllInspectionLists() {
        InspectionList list1 = new InspectionList();
        InspectionList list2 = new InspectionList();
        List<InspectionList> expectedList = Arrays.asList(list1, list2);
        when(inspectionListRepository.findAll()).thenReturn(expectedList);

        List<InspectionList> result = inspectionListService.findAll();

        assertEquals(expectedList, result);
        verify(inspectionListRepository).findAll();
    }

    @Test
    public void findById_ReturnsInspectionList() {
        ObjectId id = new ObjectId();
        InspectionList expected = new InspectionList();
        when(inspectionListRepository.findById(id)).thenReturn(Optional.of(expected));

        Optional<InspectionList> result = inspectionListService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
        verify(inspectionListRepository).findById(id);
    }

    @Test
    public void findById_ReturnsEmptyBecauseListNotFound() {
        ObjectId id = new ObjectId();
        when(inspectionListRepository.findById(id)).thenReturn(Optional.empty());

        Optional<InspectionList> result = inspectionListRepository.findById(id);

        assertTrue(result.isEmpty());
        verify(inspectionListRepository).findById(id);
    }

    @Test
    public void create_CreatesInspectionList() {
        CreateInspectionList createInspectionList = new CreateInspectionList(
                "name",
                InspectionListStatus.CONCEPT,
                List.of(),
                List.of()
        );

        InspectionList expected = new InspectionList();
        expected.setName(createInspectionList.name());
        expected.setStatus(createInspectionList.status());
        expected.setItems(createInspectionList.items());
        expected.setLabels(createInspectionList.labels());

        when(inspectionListRepository.save(expected)).thenReturn(expected);

        InspectionList result = inspectionListService.create(createInspectionList);

        assertEquals(expected, result);
        verify(inspectionListRepository).save(expected);
    }

    @Test
    public void update_UpdatesInspectionList() {
        InspectionList existingList = new InspectionList();
        existingList.setId(new ObjectId().toHexString());
        existingList.setName("existingName");
        existingList.setStatus(InspectionListStatus.CONCEPT);
        existingList.setItems(List.of());
        existingList.setLabels(List.of());

        InspectionList updatedList = new InspectionList();
        updatedList.setId(existingList.getId());
        updatedList.setName("updatedName");
        updatedList.setStatus(InspectionListStatus.CONCEPT);
        updatedList.setItems(List.of());
        updatedList.setLabels(List.of());

        when(inspectionListRepository.findById(new ObjectId(existingList.getId()))).thenReturn(Optional.of(existingList));
        when(kibFileService.deleteByIds(List.of())).thenReturn(List.of());
        when(inspectionListRepository.save(existingList)).thenReturn(existingList);

        Optional<InspectionList> result = inspectionListService.update(updatedList);

        assertTrue(result.isPresent());
        assertEquals(existingList, result.get());
        verify(inspectionListRepository).findById(new ObjectId(existingList.getId()));
        verify(kibFileService).deleteByIds(List.of());
        verify(inspectionListRepository).save(existingList);
    }

    @Test
    public void update_NotUpdatesBecauseListNotFound() {
        InspectionList updatedList = new InspectionList();
        updatedList.setId(new ObjectId().toHexString());
        updatedList.setName("updatedName");
        updatedList.setStatus(InspectionListStatus.CONCEPT);
        updatedList.setItems(List.of());
        updatedList.setLabels(List.of());

        when(inspectionListRepository.findById(new ObjectId(updatedList.getId()))).thenReturn(Optional.empty());

        Optional<InspectionList> result = inspectionListService.update(updatedList);

        assertTrue(result.isEmpty());
        verify(inspectionListRepository).findById(new ObjectId(updatedList.getId()));
    }

    @Test
    public void deleteById_DeletesInspectionList() {
        ObjectId id = new ObjectId();
        InspectionList list = new InspectionList();
        list.setItems(List.of());
        when(inspectionListRepository.findById(id)).thenReturn(Optional.of(list));
        when(kibFileService.deleteByIds(List.of())).thenReturn(List.of());

        Optional<InspectionList> result = inspectionListService.deleteById(id);

        assertTrue(result.isPresent());
        assertEquals(list, result.get());
        verify(inspectionListRepository).findById(id);
        verify(kibFileService).deleteByIds(List.of());
        verify(inspectionListRepository).deleteById(id);
    }

    @Test
    public void deleteById_NotDeletesBecauseListNotFound() {
        ObjectId id = new ObjectId();
        when(inspectionListRepository.findById(id)).thenReturn(Optional.empty());

        Optional<InspectionList> result = inspectionListService.deleteById(id);

        assertTrue(result.isEmpty());
        verify(inspectionListRepository).findById(id);
    }

    @Test
    public void copy_CopiesInspectionListWithoutImages() {
        //existing list
        InspectionList list = new InspectionList();
        list.setId(new ObjectId().toHexString());
        list.setName("name");
        list.setStatus(InspectionListStatus.CONCEPT);
        list.setLabels(List.of());
        list.setItems(List.of());

        //expected list
        InspectionList expected = new InspectionList();
        expected.setName("name - Copy");
        expected.setStatus(InspectionListStatus.CONCEPT);
        expected.setLabels(List.of());
        expected.setItems(List.of());

        when(inspectionListRepository.findById(new ObjectId(list.getId()))).thenReturn(Optional.of(list));
        when(inspectionListRepository.save(any())).thenReturn(expected);

        Optional<InspectionList> result = inspectionListService.copy(new ObjectId(list.getId()));

        assertTrue(result.isPresent());
        assertEquals("name - Copy", result.get().getName());
        assertEquals(InspectionListStatus.CONCEPT, result.get().getStatus());
        assertEquals(List.of(), result.get().getLabels());
        assertEquals(List.of(), result.get().getItems());

        verify(inspectionListRepository).findById(new ObjectId(list.getId()));
        verify(inspectionListRepository).save(result.get());
    }

    @Test
    public void copy_CopiesInspectionListWithImages() {
        //existing list
        InspectionListItemStageImage image = new InspectionListItemStageImage();
        image.setMain(true);
        image.setFileId(new ObjectId().toHexString());

        InspectionListItemStage stage = new InspectionListItemStage();
        stage.setImages(List.of(image));

        InspectionListItem item = new InspectionListItem();
        item.setStages(List.of(stage));

        InspectionList list = new InspectionList();
        list.setId(new ObjectId().toHexString());
        list.setName("name");
        list.setStatus(InspectionListStatus.CONCEPT);
        list.setLabels(List.of());
        list.setItems(List.of(item));

        //expected list
        InspectionListItemStageImage copiedImage = new InspectionListItemStageImage();
        copiedImage.setMain(true);
        copiedImage.setFileId(new ObjectId().toHexString());

        InspectionListItemStage copiedStage = new InspectionListItemStage();
        copiedStage.setImages(List.of(copiedImage));

        InspectionListItem copiedItem = new InspectionListItem();
        copiedItem.setStages(List.of(copiedStage));

        InspectionList expected = new InspectionList();
        expected.setName("name - Copy");
        expected.setStatus(InspectionListStatus.CONCEPT);
        expected.setLabels(List.of());
        expected.setItems(List.of(copiedItem));

        KibFile expectedFile = new KibFile();
        expectedFile.setId(copiedImage.getFileId());

        when(inspectionListRepository.findById(new ObjectId(list.getId()))).thenReturn(Optional.of(list));
        when(inspectionListRepository.save(any())).thenReturn(expected);
        when(kibFileService.copyById(new ObjectId(image.getFileId()))).thenReturn(expectedFile);

        Optional<InspectionList> result = inspectionListService.copy(new ObjectId(list.getId()));

        assertTrue(result.isPresent());
        assertEquals("name - Copy", result.get().getName());
        assertEquals(InspectionListStatus.CONCEPT, result.get().getStatus());
        assertEquals(List.of(), result.get().getLabels());
        assertEquals(1, result.get().getItems().size());
        assertEquals(1, result.get().getItems().getFirst().getStages().size());
        assertEquals(1, result.get().getItems().getFirst().getStages().getFirst().getImages().size());
        assertEquals(expectedFile.getId(), result.get().getItems().getFirst().getStages().getFirst().getImages().getFirst().getFileId());

        verify(inspectionListRepository).findById(new ObjectId(list.getId()));
        verify(inspectionListRepository).save(result.get());
        verify(kibFileService).copyById(new ObjectId(image.getFileId()));
    }

    @Test
    public void copyItem_CopiesInspectionListItemWithoutImages() {
        //existing list
        InspectionListItemStage stage = new InspectionListItemStage();
        stage.setImages(List.of());

        InspectionListItem item = new InspectionListItem();
        item.setId(new ObjectId().toHexString());
        item.setStages(List.of(stage));

        InspectionList list = new InspectionList();
        list.setId(new ObjectId().toHexString());
        list.setItems(List.of(item));

        //expected list
        InspectionListItemStage copiedStage = new InspectionListItemStage();
        copiedStage.setImages(List.of());

        InspectionListItem copiedItem = new InspectionListItem();
        copiedItem.setId(new ObjectId().toString());
        copiedItem.setName(item.getName() + " - Copy");
        copiedItem.setStages(List.of(copiedStage));

        InspectionList expected = new InspectionList();
        expected.setId(list.getId());
        expected.setItems(List.of(item, copiedItem));

        when(inspectionListRepository.findById(new ObjectId(list.getId()))).thenReturn(Optional.of(list));
        when(inspectionListRepository.save(list)).thenReturn(expected);

        Optional<InspectionList> result = inspectionListService.copyItem(new ObjectId(list.getId()), item.getId());

        assertTrue(result.isPresent());
        assertEquals(2, result.get().getItems().size());
        assertEquals(copiedItem, result.get().getItems().getLast());
        assertEquals(copiedStage, result.get().getItems().getLast().getStages().getFirst());

        verify(inspectionListRepository).findById(new ObjectId(list.getId()));
        verify(inspectionListRepository).save(list);
    }

    @Test
    public void copyItem_CopiesInspectionListItemWithImages() {
        //existing list
        InspectionListItemStageImage image = new InspectionListItemStageImage();
        image.setMain(true);
        image.setFileId(new ObjectId().toHexString());

        InspectionListItemStage stage = new InspectionListItemStage();
        stage.setImages(List.of(image));

        InspectionListItem item = new InspectionListItem();
        item.setId(new ObjectId().toHexString());
        item.setStages(List.of(stage));

        InspectionList list = new InspectionList();
        list.setId(new ObjectId().toHexString());
        list.setItems(List.of(item));

        //expected list
        InspectionListItemStageImage copiedImage = new InspectionListItemStageImage();
        copiedImage.setMain(true);
        copiedImage.setFileId(new ObjectId().toHexString());

        InspectionListItemStage copiedStage = new InspectionListItemStage();
        copiedStage.setImages(List.of(copiedImage));

        InspectionListItem copiedItem = new InspectionListItem();
        copiedItem.setId(new ObjectId().toString());
        copiedItem.setName(item.getName() + " - Copy");
        copiedItem.setStages(List.of(copiedStage));

        InspectionList expected = new InspectionList();
        expected.setId(list.getId());
        expected.setItems(List.of(item, copiedItem));

        KibFile expectedFile = new KibFile();
        expectedFile.setId(copiedImage.getFileId());

        when(inspectionListRepository.findById(new ObjectId(list.getId()))).thenReturn(Optional.of(list));
        when(inspectionListRepository.save(list)).thenReturn(expected);
        when(kibFileService.copyById(new ObjectId(image.getFileId()))).thenReturn(expectedFile);

        Optional<InspectionList> result = inspectionListService.copyItem(new ObjectId(list.getId()), item.getId());

        assertTrue(result.isPresent());
        assertEquals(2, result.get().getItems().size());
        assertEquals(copiedItem, result.get().getItems().getLast());
        assertEquals(copiedStage, result.get().getItems().getLast().getStages().getFirst());
        assertEquals(copiedImage, result.get().getItems().getLast().getStages().getFirst().getImages().getFirst());
        assertEquals(expectedFile.getId(), result.get().getItems().getLast().getStages().getFirst().getImages().getFirst().getFileId());

        verify(inspectionListRepository).findById(new ObjectId(list.getId()));
        verify(inspectionListRepository).save(list);
        verify(kibFileService).copyById(new ObjectId(image.getFileId()));
    }
}