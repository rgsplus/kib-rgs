package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.shared.models.ImportResult;

import java.io.IOException;

public interface FileImportService {

    ImportResult<InspectionList> importExcelBluePrint(byte[] blueprint, String name) throws IOException;

    void importRGS(byte[] rgsFile) throws IOException;
}
