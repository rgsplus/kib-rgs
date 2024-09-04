package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.shared.models.ImportResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("production")
public class FileImportServiceImplTest {

    @Autowired
    private FileImportService fileImportService;

    @Test
    public void testImportFileSuccess() throws IOException {
        Path path = Paths.get("src/test/resources/kib-blueprint.xlsx");
        byte[] file = Files.readAllBytes(path);
        String fileName = "Test blueprint";

        ImportResult<InspectionList> importResult = fileImportService.importExcelBluePrint(file, fileName);

        assertNotNull(importResult);
        assertNotNull(importResult.getResult());
        assertNotNull(importResult.getResult().getId());
        assertEquals(fileName, importResult.getResult().getName());
        assertEquals(0, importResult.getErrors().size());
    }

    @Test
    public void testImportFileFailure() throws IOException {
        Path path = Paths.get("src/test/resources/kib-blueprint-with-errors.xlsx");
        byte[] file = Files.readAllBytes(path);
        String fileName = "Test blueprint with errors";

        ImportResult<InspectionList> importResult = fileImportService.importExcelBluePrint(file, fileName);

        assertNotNull(importResult);
        assertNull(importResult.getResult());
        assertEquals(6, importResult.getErrors().size());
    }
}
