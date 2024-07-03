package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.repository.InspectionListCodeRepository;
import nl.rgs.kib.service.impl.InspectionListCodeServiceImpl;
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
public class InspectionListCodeServiceImplTest {

    @Mock
    private InspectionListCodeRepository inspectionListCodeRepository;

    @InjectMocks
    private InspectionListCodeServiceImpl inspectionListCodeService;

    @Test
    public void findAll_ReturnsAllInspectionListCodes() {
        InspectionListCode list1 = new InspectionListCode();
        InspectionListCode list2 = new InspectionListCode();
        List<InspectionListCode> expectedList = Arrays.asList(list1, list2);
        when(inspectionListCodeRepository.findAll()).thenReturn(expectedList);

        List<InspectionListCode> result = inspectionListCodeService.findAll();

        assertEquals(expectedList, result);
        verify(inspectionListCodeRepository).findAll();
    }

    @Test
    public void findById_ReturnsInspectionListCode() {
        ObjectId id = new ObjectId();
        InspectionListCode expected = new InspectionListCode();
        when(inspectionListCodeRepository.findById(id)).thenReturn(Optional.of(expected));

        Optional<InspectionListCode> result = inspectionListCodeService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
        verify(inspectionListCodeRepository).findById(id);
    }
}