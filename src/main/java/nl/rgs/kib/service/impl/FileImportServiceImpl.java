package nl.rgs.kib.service.impl;

import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.InspectionListItem;
import nl.rgs.kib.model.list.InspectionListItemStage;
import nl.rgs.kib.model.list.InspectionListStatus;
import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.model.method.InspectionMethodStage;
import nl.rgs.kib.repository.InspectionListRepository;
import nl.rgs.kib.repository.InspectionMethodRepository;
import nl.rgs.kib.service.FileImportService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileImportServiceImpl implements FileImportService {

    private final InspectionMethodRepository inspectionMethodRepository;
    private final InspectionListRepository inspectionListRepository;

    public FileImportServiceImpl(InspectionMethodRepository inspectionMethodRepository, InspectionListRepository inspectionListRepository) {
        this.inspectionMethodRepository = inspectionMethodRepository;
        this.inspectionListRepository = inspectionListRepository;
    }


    @Override
    public void importExcelBluePrint(byte[] blueprint, String name) throws IOException {

        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(blueprint));
        InspectionList inspectionList = new InspectionList();
        inspectionList.setItems(new ArrayList<>());
        inspectionList.setName(name);
        inspectionList.setStatus(InspectionListStatus.CONCEPT);

        int index = 0;
        for ( int sheetno = 0; sheetno < workbook.getNumberOfSheets(); sheetno++ ) {
            final String sheetName = workbook.getSheetName(sheetno);
            if ( Character.isDigit(sheetName.charAt(0)) ) {
                Sheet sheet = workbook.getSheet(sheetName);

                String currentGroup = null;
                for ( int rowno = 5; rowno < sheet.getLastRowNum(); rowno++ ) {
                    InspectionListItem inspectionListItem = new InspectionListItem();
                    inspectionListItem.setIndex(index);
                    inspectionListItem.setId(UUID.randomUUID().toString());

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

                        cell = sheet.getRow(rowno).getCell(2);
                        if ( cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null ) {
                            inspectionListItem.setField(cell.getStringCellValue());
                        }

                        cell = sheet.getRow(rowno).getCell(4);
                        if ( cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null ) {
                            inspectionListItem.setName(cell.getStringCellValue());
                        }

                        cell = sheet.getRow(rowno).getCell(7);
                        if ( cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null ) {
                            inspectionListItem.setStandardNo(cell.getStringCellValue());
                        }
                        else if ( cell.getCellType().equals(CellType.NUMERIC) ) {
                            inspectionListItem.setStandardNo("" + (int) cell.getNumericCellValue());
                        }

                        cell = sheet.getRow(rowno).getCell(8);
                        if ( cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null ) {
                            inspectionListItem.setMeasuringMethod(cell.getStringCellValue());
                        }

                        cell = sheet.getRow(rowno).getCell(9);
                        if ( cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null ) {
                            Optional<InspectionMethod> inspectionMethodOptional = inspectionMethodRepository.findByName("Conditiescore");
                            InspectionMethod inspectionMethod = inspectionMethodOptional.orElseGet(() -> {
                                InspectionMethod newInspectionMethod = new InspectionMethod();
                                newInspectionMethod.setName("Conditiescore");
                                newInspectionMethod.setStages(new LinkedList<>());
                                for ( int i = 1; i < 11; i++ ) {
                                    InspectionMethodStage inspectionMethodStage = new InspectionMethodStage();
                                    inspectionMethodStage.setName(""+i);
                                    inspectionMethodStage.setStage(i);
                                    newInspectionMethod.getStages().add(inspectionMethodStage);
                                }
                                newInspectionMethod = inspectionMethodRepository.save(newInspectionMethod);

                                return newInspectionMethod;
                            });

                            inspectionListItem.setInspectionMethod(inspectionMethod);
                            inspectionListItem.setStages(new LinkedList<>());

                            final String method = cell.getStringCellValue();
                            switch (method) {
                                case "Klasse 0 of 5" -> {
                                    InspectionListItemStage inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(1);
                                    inspectionListItemStage.setName("Klasse 0");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(6);
                                    inspectionListItemStage.setName("Klasse 5");
                                    inspectionListItem.getStages().add(inspectionListItemStage);
                                }
                                case "Klasse 0, 1, 2 of 5" -> {
                                    InspectionListItemStage inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(1);
                                    inspectionListItemStage.setName("Klasse 0");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(2);
                                    inspectionListItemStage.setName("Klasse 1");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(3);
                                    inspectionListItemStage.setName("Klasse 2");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(6);
                                    inspectionListItemStage.setName("Klasse 5");
                                    inspectionListItem.getStages().add(inspectionListItemStage);
                                }
                                case "Klasse 0, 1, 2, 3, 4 of 5" -> {
                                    InspectionListItemStage inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(1);
                                    inspectionListItemStage.setName("Klasse 0");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(2);
                                    inspectionListItemStage.setName("Klasse 1");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(3);
                                    inspectionListItemStage.setName("Klasse 2");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(4);
                                    inspectionListItemStage.setName("Klasse 3");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(5);
                                    inspectionListItemStage.setName("Klasse 4");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(6);
                                    inspectionListItemStage.setName("Klasse 5");
                                    inspectionListItem.getStages().add(inspectionListItemStage);
                                }
                                case "Klasse 0, 1, 2, 4 of 5" -> {
                                    InspectionListItemStage inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(1);
                                    inspectionListItemStage.setName("Klasse 0");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(2);
                                    inspectionListItemStage.setName("Klasse 1");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(3);
                                    inspectionListItemStage.setName("Klasse 2");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(5);
                                    inspectionListItemStage.setName("Klasse 4");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(6);
                                    inspectionListItemStage.setName("Klasse 5");
                                    inspectionListItem.getStages().add(inspectionListItemStage);
                                }
                                case "Klasse 0, 1, 3 of 5" -> {
                                    InspectionListItemStage inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(1);
                                    inspectionListItemStage.setName("Klasse 0");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(2);
                                    inspectionListItemStage.setName("Klasse 1");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(4);
                                    inspectionListItemStage.setName("Klasse 3");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(6);
                                    inspectionListItemStage.setName("Klasse 5");
                                    inspectionListItem.getStages().add(inspectionListItemStage);
                                }
                                case "Klasse 0, 1, 3 of 6" -> {
                                    InspectionListItemStage inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(1);
                                    inspectionListItemStage.setName("Klasse 0");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(2);
                                    inspectionListItemStage.setName("Klasse 1");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(4);
                                    inspectionListItemStage.setName("Klasse 3");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(7);
                                    inspectionListItemStage.setName("Klasse 6");
                                    inspectionListItem.getStages().add(inspectionListItemStage);
                                }
                                case "Klasse 0, 1, 3 of 7" -> {
                                    InspectionListItemStage inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(1);
                                    inspectionListItemStage.setName("Klasse 0");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(2);
                                    inspectionListItemStage.setName("Klasse 1");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(4);
                                    inspectionListItemStage.setName("Klasse 3");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(8);
                                    inspectionListItemStage.setName("Klasse 7");
                                    inspectionListItem.getStages().add(inspectionListItemStage);
                                }
                                case "Klasse 0, 1, 3 of 9" -> {
                                    InspectionListItemStage inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(1);
                                    inspectionListItemStage.setName("Klasse 0");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(2);
                                    inspectionListItemStage.setName("Klasse 1");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(4);
                                    inspectionListItemStage.setName("Klasse 3");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(10);
                                    inspectionListItemStage.setName("Klasse 9");
                                    inspectionListItem.getStages().add(inspectionListItemStage);
                                }
                                case "Klasse 0, 2, 3 of 5" -> {
                                    InspectionListItemStage inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(1);
                                    inspectionListItemStage.setName("Klasse 0");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(3);
                                    inspectionListItemStage.setName("Klasse 2");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(4);
                                    inspectionListItemStage.setName("Klasse 3");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(6);
                                    inspectionListItemStage.setName("Klasse 5");
                                    inspectionListItem.getStages().add(inspectionListItemStage);
                                }
                                case "Klasse 0, 3 of 5" -> {
                                    InspectionListItemStage inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(1);
                                    inspectionListItemStage.setName("Klasse 0");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(4);
                                    inspectionListItemStage.setName("Klasse 3");
                                    inspectionListItem.getStages().add(inspectionListItemStage);

                                    inspectionListItemStage = new InspectionListItemStage();
                                    inspectionListItemStage.setStage(6);
                                    inspectionListItemStage.setName("Klasse 5");
                                    inspectionListItem.getStages().add(inspectionListItemStage);
                                }
                            }

                            inspectionList.getItems().add(inspectionListItem);
                            index++;
                        }
                    }
                }
            }
        }
        inspectionListRepository.save(inspectionList);
    }
}
