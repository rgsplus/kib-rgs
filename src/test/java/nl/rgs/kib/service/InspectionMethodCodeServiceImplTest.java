package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.model.method.InspectionMethodCode;
import nl.rgs.kib.repository.InspectionListCodeRepository;
import nl.rgs.kib.repository.InspectionMethodCodeRepository;
import nl.rgs.kib.service.impl.InspectionListCodeServiceImpl;
import nl.rgs.kib.service.impl.InspectionMethodCodeServiceImpl;
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
public class InspectionMethodCodeServiceImplTest {

    @Mock
    private InspectionMethodCodeRepository inspectionMethodCodeRepository;

    @InjectMocks
    private InspectionMethodCodeServiceImpl inspectionMethodCodeService;

    @Test
    public void findAll_ReturnsAllInspectionMethodCodes() {
        InspectionMethodCode method1 = new InspectionMethodCode();
        InspectionMethodCode method2 = new InspectionMethodCode();
        List<InspectionMethodCode> expectedList = Arrays.asList(method1, method2);
        when(inspectionMethodCodeRepository.findAll()).thenReturn(expectedList);

        List<InspectionMethodCode> result = inspectionMethodCodeService.findAll();

        assertEquals(expectedList, result);
        verify(inspectionMethodCodeRepository).findAll();
    }

    @Test
    public void findById_ReturnsInspectionListCode() {
        ObjectId id = new ObjectId();
        InspectionMethodCode expected = new InspectionMethodCode();
        when(inspectionMethodCodeRepository.findById(id)).thenReturn(Optional.of(expected));

        Optional<InspectionMethodCode> result = inspectionMethodCodeService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
        verify(inspectionMethodCodeRepository).findById(id);
    }
}