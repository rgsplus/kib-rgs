package nl.rgs.kib.service.impl;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.rgs.kib.shared.models.ImportResultError.convertToImplSet;

@Slf4j
@Service
public class FileImportServiceImpl implements FileImportService {
    @Autowired
    private Validator validator = null;

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

        Optional<InspectionMethod> inspectionMethodOptional = inspectionMethodService.findByName("Inspectieklasse");
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
            } else {
                newInspectionMethod = inspectionMethodService.create(createInspectionMethod);
                return newInspectionMethod;
            }

            return null;
        });

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
                        SortedSet<String> groups = new TreeSet<>(inspectionListItem.getGroups());
                        groups.add(currentGroup);
                        inspectionListItem.setGroups(groups);

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

                        inspectionListItem.setInspectionMethod(inspectionMethod);
                        inspectionListItem.setStages(new LinkedList<>());

                        final String method = cell.getStringCellValue();
                        Set<Integer> stages = new HashSet<>();

                        if (method != null) {
                            for (int i = 0; i < method.length(); i++) {
                                if (Character.isDigit(method.charAt(i))) {
                                    int stage = Character.getNumericValue(method.charAt(i));
                                    if (stage >= 0 && stage <= 9) {
                                        stages.add(Character.getNumericValue(method.charAt(i)));
                                    }
                                }
                            }
                        }

                        for (Integer stage : stages) {
                            InspectionListItemStage inspectionListItemStage = new InspectionListItemStage();
                            inspectionListItemStage.setStage(stage + 1);
                            inspectionListItemStage.setName("Klasse " + stage);
                            inspectionListItemStage.setImages(new ArrayList<>());
                            inspectionListItem.getStages().add(inspectionListItemStage);
                        }

                        InspectionListItem existingItem = inspectionList.getItems().stream().filter(item -> item.getStandardNo().equals(inspectionListItem.getStandardNo())).findFirst().orElse(null);

                        if (existingItem == null) {
                            inspectionList.getItems().add(inspectionListItem);
                            index++;
                        } else {
                            SortedSet<String> existingGroups = new TreeSet<>(existingItem.getGroups());
                            existingGroups.addAll(inspectionListItem.getGroups());
                            existingItem.setGroups(existingGroups);
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

    public void importRGS(byte[] rgsFile) throws IOException {
        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(rgsFile));
        Sheet sheet = workbook.getSheetAt(0);

        Optional<InspectionList> inspectionListOptional = inspectionListService.findById("67326c69509fb80d9461995b");
        if (inspectionListOptional.isEmpty()) {
            return;
        }
        InspectionList inspectionList = inspectionListOptional.get();

        for (int rowno = 1; rowno < sheet.getLastRowNum(); rowno++) {
            if (sheet.getRow(rowno).getCell(0) == null) {
                return;
            }
            String naam = sheet.getRow(rowno).getCell(0).getStringCellValue();
            if (naam == null) {
                return;
            }
            Matcher matcher = Pattern.compile("^(KIB-+)(\\d+[A-Z]?)(.*)").matcher(naam);
            while (matcher.find()) {
                final String naam3Elements = matcher.group(2);
                final int row = rowno;
                inspectionList.getItems().stream().filter(inspectionListItem -> inspectionListItem.getStandardNo().equals(naam3Elements)).forEach(inspectionListItem -> {
                    Set<InspectionListItemStage> stageToRemove = new HashSet<>();
                    inspectionListItem.getStages().forEach(inspectionListItemStage -> {
                        inspectionListItemStage.setName(null);
                        Cell cell = sheet.getRow(row).getCell(inspectionListItemStage.getStage() + 3);
                        if (cell != null) {
                            final String stageDesc = cell.getStringCellValue();
                            if (stageDesc != null) {
                                inspectionListItemStage.setName(stageDesc);
                            }
                        }
                        if ( inspectionListItemStage.getName() == null ) {
                            stageToRemove.add(inspectionListItemStage);
                        }
                    });
                    inspectionListItem.getStages().removeAll(stageToRemove);
                });
            }
        }

        inspectionListService.update(inspectionList);
    }
}
