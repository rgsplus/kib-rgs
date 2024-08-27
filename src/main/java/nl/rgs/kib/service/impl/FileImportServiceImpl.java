package nl.rgs.kib.service.impl;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.InspectionListItem;
import nl.rgs.kib.model.list.InspectionListItemStage;
import nl.rgs.kib.model.list.InspectionListStatus;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.model.method.InspectionMethodStage;
import nl.rgs.kib.model.method.dto.CreateInspectionMethod;
import nl.rgs.kib.service.FileImportService;
import nl.rgs.kib.service.InspectionListService;
import nl.rgs.kib.service.InspectionMethodService;
import nl.rgs.kib.shared.models.ImportResult;
import nl.rgs.kib.shared.models.ImportResultError;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static nl.rgs.kib.shared.models.ImportResultError.convertToImplSet;

@Slf4j
@Service
public class FileImportServiceImpl implements FileImportService {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private InspectionMethodService inspectionMethodService;

    @Autowired
    private InspectionListService inspectionListService;


    @Override
    public ImportResult<InspectionList> importExcelBluePrint(byte[] blueprint, String name) throws IOException {

        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(blueprint));
        InspectionList inspectionList = new InspectionList();
        inspectionList.setItems(new ArrayList<>());
        inspectionList.setName(name);
        inspectionList.setStatus(InspectionListStatus.CONCEPT);

        int index = 0;
        for (int sheetno = 0; sheetno < workbook.getNumberOfSheets(); sheetno++) {
            final String sheetName = workbook.getSheetName(sheetno);
            if (Character.isDigit(sheetName.charAt(0))) {
                Sheet sheet = workbook.getSheet(sheetName);

                String currentGroup = null;
                for (int rowno = 5; rowno < sheet.getLastRowNum(); rowno++) {
                    InspectionListItem inspectionListItem = new InspectionListItem();
                    inspectionListItem.setIndex(index);
                    inspectionListItem.setId(UUID.randomUUID().toString());

                    Cell cell = sheet.getRow(rowno).getCell(3);
                    String value = cell.getStringCellValue();
                    if (value != null && value.equalsIgnoreCase("aan")) {
                        cell = sheet.getRow(rowno).getCell(0);
                        if (cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null) {
                            currentGroup = cell.getStringCellValue();
                        }
                        inspectionListItem.setGroup(currentGroup);

                        cell = sheet.getRow(rowno).getCell(1);
                        if (cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null) {
                            inspectionListItem.setTheme(cell.getStringCellValue());
                        }

                        cell = sheet.getRow(rowno).getCell(2);
                        if (cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null) {
                            inspectionListItem.setField(cell.getStringCellValue());
                        }

                        cell = sheet.getRow(rowno).getCell(4);
                        if (cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null) {
                            inspectionListItem.setName(cell.getStringCellValue());
                        }

                        cell = sheet.getRow(rowno).getCell(7);
                        if (cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null) {
                            inspectionListItem.setStandardNo(cell.getStringCellValue());
                        } else if (cell.getCellType().equals(CellType.NUMERIC)) {
                            inspectionListItem.setStandardNo("" + (int) cell.getNumericCellValue());
                        }

                        cell = sheet.getRow(rowno).getCell(8);
                        if (cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null) {
                            inspectionListItem.setMeasuringMethod(cell.getStringCellValue());
                        }

                        cell = sheet.getRow(rowno).getCell(9);
                        if (cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue() != null) {
                            Optional<InspectionMethod> inspectionMethodOptional = inspectionMethodService.findByName("Conditiescore");

                            ImportResult<InspectionList> importResult = new ImportResult<>();

                            InspectionMethod inspectionMethod = inspectionMethodOptional.orElseGet(() -> {
                                InspectionMethod newInspectionMethod = new InspectionMethod();
                                newInspectionMethod.setName("Conditiescore");
                                newInspectionMethod.setStages(new LinkedList<>());
                                for (int i = 1; i < 11; i++) {
                                    InspectionMethodStage inspectionMethodStage = new InspectionMethodStage();
                                    inspectionMethodStage.setName("" + i);
                                    inspectionMethodStage.setStage(i);
                                    newInspectionMethod.getStages().add(inspectionMethodStage);
                                }

                                CreateInspectionMethod createInspectionMethod = new CreateInspectionMethod(
                                        newInspectionMethod.getName(),
                                        newInspectionMethod.getInput(),
                                        newInspectionMethod.getCalculationMethod(),
                                        newInspectionMethod.getStages()
                                );


                                Set<ConstraintViolationImpl<CreateInspectionMethod>> inspectionMethodViolations = convertToImplSet(validator.validate(createInspectionMethod));

                                if (!inspectionMethodViolations.isEmpty()) {
                                    log.error("Invalid InspectionMethod: {}", inspectionMethodViolations);
                                    importResult.setErrors(ImportResultError.constraintViolationsImplToImportResultsError(inspectionMethodViolations));
                                } else {
                                    newInspectionMethod = inspectionMethodService.create(createInspectionMethod);
                                    return newInspectionMethod;
                                }

                                return null;
                            });

                            if (!importResult.getErrors().isEmpty()) {
                                return importResult;
                            }

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

        CreateInspectionList createInspectionList = new CreateInspectionList(
                inspectionList.getName(),
                inspectionList.getStatus(),
                inspectionList.getItems()
        );


        Set<ConstraintViolationImpl<CreateInspectionList>> inspectionListViolations = convertToImplSet(validator.validate(createInspectionList));

        ImportResult<InspectionList> importResult = new ImportResult<>();

        if (!inspectionListViolations.isEmpty()) {
            log.error("Invalid InspectionList: {}", inspectionListViolations);
            importResult.setErrors(ImportResultError.constraintViolationsImplToImportResultsError(inspectionListViolations));
            return importResult;
        }

        InspectionList createdInspectionList = inspectionListService.create(createInspectionList);
        importResult.setResult(createdInspectionList);
        return importResult;
    }

}
