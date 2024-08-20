package nl.rgs.kib.service.impl;

import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.InspectionListItem;
import nl.rgs.kib.service.FileImportService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileImportServiceImpl implements FileImportService {

    @Override
    public void importExcelBluePrint(byte[] blueprint, String name) throws IOException {

        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(blueprint));
        InspectionList inspectionList = new InspectionList();
        inspectionList.setItems(new ArrayList<>());
        inspectionList.setName(name);

        for ( int sheetno = 0; sheetno < workbook.getNumberOfSheets(); sheetno++ ) {
            final String sheetName = workbook.getSheetName(sheetno);
            if ( Character.isDigit(sheetName.charAt(0)) ) {
                Sheet sheet = workbook.getSheet(sheetName);

                String currentGroup = null;
                for ( int rowno = 5; rowno < sheet.getLastRowNum(); rowno++ ) {
                    InspectionListItem inspectionListItem = new InspectionListItem();


                    Cell cell = sheet.getRow(rowno).getCell(3);
                    String value = cell.getStringCellValue();
                    if ( value != null && value.equalsIgnoreCase("aan") ) {
                        cell = sheet.getRow(rowno).getCell(0);
                        if ( cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null ) {
                            currentGroup = cell.getStringCellValue();
                        }
                        inspectionListItem.setGroup(currentGroup);

                        cell = sheet.getRow(rowno).getCell(1);
                        if ( cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null ) {
                            inspectionListItem.setTheme(cell.getStringCellValue());
                        }

                    }
                }
            }
        }

    }
}
