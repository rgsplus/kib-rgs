package nl.rgs.kib.service;

import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.model.method.InspectionMethodCalculationMethod;
import nl.rgs.kib.model.method.InspectionMethodInput;
import nl.rgs.kib.model.method.dto.CreateInspectionMethod;
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
        String id = new ObjectId().toHexString();
        InspectionMethod expected = new InspectionMethod();
        when(inspectionMethodRepository.findById(id)).thenReturn(Optional.of(expected));

        Optional<InspectionMethod> result = inspectionMethodService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
        verify(inspectionMethodRepository).findById(id);
    }

    @Test
    public void findById_ReturnsEmptyBecauseMethodNotFound() {
        String id = new ObjectId().toHexString();
        when(inspectionMethodRepository.findById(id)).thenReturn(Optional.empty());

        Optional<InspectionMethod> result = inspectionMethodService.findById(id);

        assertTrue(result.isEmpty());
        verify(inspectionMethodRepository).findById(id);
    }

    @Test
    public void create_CreatesInspectionMethod() {
        CreateInspectionMethod createInspectionMethod = new CreateInspectionMethod(
                "name",
                InspectionMethodInput.STAGE,
                InspectionMethodCalculationMethod.NEN2767,
                List.of()
        );

        InspectionMethod expected = new InspectionMethod();
        expected.setName(createInspectionMethod.name());
        expected.setInput(createInspectionMethod.input());
        expected.setCalculationMethod(createInspectionMethod.calculationMethod());
        expected.setStages(InspectionMethod.sortStages(createInspectionMethod.stages()));

        when(inspectionMethodRepository.save(expected)).thenReturn(expected);

        InspectionMethod result = inspectionMethodService.create(createInspectionMethod);

        assertEquals(expected, result);
        verify(inspectionMethodRepository).save(expected);
    }

    @Test
    public void update_UpdatesInspectionMethod() {
        InspectionMethod existingMethod = new InspectionMethod();
        existingMethod.setId(new ObjectId().toHexString());
        existingMethod.setName("existingName");
        existingMethod.setInput(InspectionMethodInput.STAGE);
        existingMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
        existingMethod.setStages(List.of());

        InspectionMethod updatedMethod = new InspectionMethod();
        updatedMethod.setId(existingMethod.getId());
        updatedMethod.setName("updatedName");
        updatedMethod.setInput(InspectionMethodInput.STAGE);
        updatedMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
        updatedMethod.setStages(List.of());

        when(inspectionMethodRepository.findById(existingMethod.getId())).thenReturn(Optional.of(existingMethod));
        when(inspectionMethodRepository.save(existingMethod)).thenReturn(existingMethod);

        Optional<InspectionMethod> result = inspectionMethodService.update(updatedMethod);

        assertTrue(result.isPresent());
        assertEquals(existingMethod, result.get());
        verify(inspectionMethodRepository).findById(existingMethod.getId());
        verify(inspectionMethodRepository).save(existingMethod);
    }

    @Test
    public void update_NotUpdatesBecauseMethodNotFound() {
        InspectionMethod updatedMethod = new InspectionMethod();
        updatedMethod.setId(new ObjectId().toHexString());
        updatedMethod.setName("updatedName");
        updatedMethod.setInput(InspectionMethodInput.STAGE);
        updatedMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
        updatedMethod.setStages(List.of());

        when(inspectionMethodRepository.findById(updatedMethod.getId())).thenReturn(Optional.empty());

        Optional<InspectionMethod> result = inspectionMethodService.update(updatedMethod);

        assertTrue(result.isEmpty());
        verify(inspectionMethodRepository).findById(updatedMethod.getId());
    }

    @Test
    public void deleteById_DeletesInspectionMethod() {
        String id = new ObjectId().toHexString();
        InspectionMethod method = new InspectionMethod();
        when(inspectionMethodRepository.findById(id)).thenReturn(Optional.of(method));

        Optional<InspectionMethod> result = inspectionMethodService.deleteById(id);

        assertTrue(result.isPresent());
        assertEquals(method, result.get());
        verify(inspectionMethodRepository).findById(id);
        verify(inspectionMethodRepository).deleteById(id);
    }

    @Test
    public void deleteById_NotDeletesBecauseMethodNotFound() {
        String id = new ObjectId().toHexString();
        when(inspectionMethodRepository.findById(id)).thenReturn(Optional.empty());

        Optional<InspectionMethod> result = inspectionMethodService.deleteById(id);

        assertTrue(result.isEmpty());
        verify(inspectionMethodRepository).findById(id);
    }
}
