package nl.rgs.kib.service;

import java.io.IOException;

public interface FileImportService {

    void importExcelBluePrint(byte[] blueprint, String name) throws IOException;
}
