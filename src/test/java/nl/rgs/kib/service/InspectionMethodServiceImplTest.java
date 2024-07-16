package nl.rgs.kib.service;

import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.repository.InspectionMethodRepository;
import nl.rgs.kib.service.impl.InspectionMethodServiceImpl;
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
public class InspectionMethodServiceImplTest {

    @Mock
    private InspectionMethodRepository inspectionMethodRepository;

    @InjectMocks
    private InspectionMethodServiceImpl inspectionMethodService;

    @Test
    public void findAll_ReturnsAllInspectionMethods() {
        InspectionMethod method1 = new InspectionMethod();
        InspectionMethod method2 = new InspectionMethod();
        List<InspectionMethod> expectedList = Arrays.asList(method1, method2);
        when(inspectionMethodRepository.findAll()).thenReturn(expectedList);

        List<InspectionMethod> result = inspectionMethodService.findAll();

        assertEquals(expectedList, result);
        verify(inspectionMethodRepository).findAll();
    }

    @Test
    public void findById_ReturnsInspectionList() {
        ObjectId id = new ObjectId();
        InspectionMethod expected = new InspectionMethod();
        when(inspectionMethodRepository.findById(id)).thenReturn(Optional.of(expected));

        Optional<InspectionMethod> result = inspectionMethodService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
        verify(inspectionMethodRepository).findById(id);
    }

    //TODO: Implement tests for count method
    //TODO: Implement tests for create method
    //TODO: Implement tests for update method
    //TODO: Implement tests for delete method
}