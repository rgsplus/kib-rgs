package nl.rgs.kib.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
@ActiveProfiles("production")
public class FileImportServiceImplTest {

    @Autowired
    private FileImportService fileImportService;

    @Test
    public void testImportFile() throws IOException {
        Path path = Paths.get("src/test/resources/kib-blueprint.xlsx");
        byte[] file = Files.readAllBytes(path);
        fileImportService.importExcelBluePrint(file, "Test blueprint");
    }
}
