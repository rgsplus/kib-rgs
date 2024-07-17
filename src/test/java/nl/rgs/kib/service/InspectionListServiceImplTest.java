package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.InspectionListStatus;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import nl.rgs.kib.repository.InspectionListRepository;
import nl.rgs.kib.service.impl.InspectionListServiceImpl;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InspectionListServiceImplTest {

    @Mock
    private InspectionListRepository inspectionListRepository;

    @InjectMocks
    private InspectionListServiceImpl inspectionListService;

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
        when(inspectionListRepository.save(existingList)).thenReturn(existingList);

        Optional<InspectionList> result = inspectionListService.update(updatedList);

        assertTrue(result.isPresent());
        assertEquals(existingList, result.get());
        verify(inspectionListRepository).findById(new ObjectId(existingList.getId()));
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
        when(inspectionListRepository.findById(id)).thenReturn(Optional.of(list));

        Optional<InspectionList> result = inspectionListService.deleteById(id);

        assertTrue(result.isPresent());
        assertEquals(list, result.get());
        verify(inspectionListRepository).findById(id);
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
}