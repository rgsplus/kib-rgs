package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionList;
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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InspectionListServiceImplTest {

    @Mock
    private InspectionListRepository inspectionListRepository;

    @InjectMocks
    private InspectionListServiceImpl inspectionListService;

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
}