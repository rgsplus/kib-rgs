package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.model.method.InspectionMethodStage;
import nl.rgs.kib.shared.models.ImportResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FileImportServiceImplTest {

    @Autowired
    private FileImportService fileImportService;

    @MockBean
    private InspectionMethodService inspectionMethodService;

    @MockBean
    private InspectionListService inspectionListService;

    @Test
    public void testImportFileSuccess() throws IOException {
        Path path = Paths.get("src/test/resources/kib-blueprint.xlsx");
        byte[] file = Files.readAllBytes(path);
        String fileName = "Test blueprint";

        InspectionMethod inspectionMethod = new InspectionMethod();
        inspectionMethod.setName("Conditiescore");
        inspectionMethod.setStages(new LinkedList<>());
        for (int i = 1; i < 11; i++) {
            InspectionMethodStage inspectionMethodStage = new InspectionMethodStage();
            inspectionMethodStage.setName("" + i);
            inspectionMethodStage.setStage(i);
            inspectionMethod.getStages().add(inspectionMethodStage);
        }

        when(inspectionMethodService.findByName("Conditiescore")).thenReturn(Optional.of(inspectionMethod));
        when(inspectionListService.create(any())).thenReturn(new InspectionList());

        ImportResult<InspectionList> importResult = fileImportService.importExcelBluePrint(file, fileName);

        assertNotNull(importResult);
        assertNotNull(importResult.getResult());
        assertEquals(0, importResult.getErrors().size());

        verify(inspectionListService).create(any());
    }

    @Test
    public void testImportFileFailure() throws IOException {
        Path path = Paths.get("src/test/resources/kib-blueprint-with-errors.xlsx");
        byte[] file = Files.readAllBytes(path);
        String fileName = "Test blueprint with errors";

        InspectionMethod inspectionMethod = new InspectionMethod();
        inspectionMethod.setName("Conditiescore");
        inspectionMethod.setStages(new LinkedList<>());
        for (int i = 1; i < 11; i++) {
            InspectionMethodStage inspectionMethodStage = new InspectionMethodStage();
            inspectionMethodStage.setName("" + i);
            inspectionMethodStage.setStage(i);
            inspectionMethod.getStages().add(inspectionMethodStage);
        }

        when(inspectionMethodService.findByName("Conditiescore")).thenReturn(Optional.of(inspectionMethod));
        when(inspectionListService.create(any())).thenReturn(new InspectionList());

        ImportResult<InspectionList> importResult = fileImportService.importExcelBluePrint(file, fileName);

        assertNotNull(importResult);
        assertNull(importResult.getResult());
        assertEquals(6, importResult.getErrors().size());
    }
}
