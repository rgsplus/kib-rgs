package nl.rgs.kib.service.impl;

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import nl.rgs.kib.model.file.KibFile;
import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.InspectionListItem;
import nl.rgs.kib.model.list.InspectionListItemStage;
import nl.rgs.kib.model.list.InspectionListItemStageImage;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import nl.rgs.kib.model.list.dto.SummaryInspectionList;
import nl.rgs.kib.repository.InspectionListRepository;
import nl.rgs.kib.service.FileImportService;
import nl.rgs.kib.service.InspectionListService;
import nl.rgs.kib.service.KibFileService;
import nl.rgs.kib.shared.models.ImportDocument;
import nl.rgs.kib.shared.models.ImportResult;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InspectionListServiceImpl implements InspectionListService {

    @Autowired
    private KibFileService kibFileService;

    @Autowired
    private InspectionListRepository inspectionListRepository;

    @Autowired
    private FileImportService fileImportService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public List<InspectionList> findAll() {
        return inspectionListRepository.findAll();
    }

    @Override
    public List<SummaryInspectionList> findAllSummaries() {
        return inspectionListRepository.findAllSummaries();
    }

    @Override
    public Optional<InspectionList> findById(String id) {
        return inspectionListRepository.findById(id);
    }

    @Override
    public InspectionList create(@NotNull CreateInspectionList createInspectionList) {
        InspectionList inspectionList = new InspectionList();
        inspectionList.setName(createInspectionList.name());
        inspectionList.setStatus(createInspectionList.status());
        inspectionList.setItems(InspectionList.sortItemsStagesAndImages(createInspectionList.items()));

        return inspectionListRepository.save(inspectionList);
    }

    @Override
    @Transactional
    public Optional<InspectionList> update(@NotNull InspectionList inspectionList) {
        return inspectionListRepository.findById(inspectionList.getId()).map(existingList -> {
            List<String> deletedFileIds = InspectionList.getDeletedFileIds(existingList, inspectionList);
            kibFileService.deleteByIds(deletedFileIds);

            existingList.setName(inspectionList.getName());
            existingList.setStatus(inspectionList.getStatus());
            existingList.setItems(InspectionList.sortItemsStagesAndImages(inspectionList.getItems()));

            return inspectionListRepository.save(existingList);
        });
    }

    @Override
    @Transactional
    public Optional<InspectionList> deleteById(String id) {
        Optional<InspectionList> inspectionList = inspectionListRepository.findById(id);
        inspectionList.ifPresent(list ->
                {
                    List<String> allFileIds = InspectionList.getAllFileIds(list);
                    kibFileService.deleteByIds(allFileIds);

                    inspectionListRepository.deleteById(id);
                }
        );
        return inspectionList;
    }

    @Override
    @Transactional
    public Optional<InspectionList> copy(String id) {
        return inspectionListRepository.findById(id).map(inspectionList -> {
            InspectionList copy = new InspectionList();

            Locale locale = LocaleContextHolder.getLocale();
            String copySuffix = messageSource.getMessage("copy.suffix", null, locale);
            String separator = messageSource.getMessage("copy.separator", null, locale);
            copy.setName(inspectionList.getName() + " " + separator + " " + copySuffix);

            copy.setStatus(inspectionList.getStatus());

            List<InspectionListItem> copiedItems = inspectionList.getItems().stream().map(item -> {
                InspectionListItem copiedItem = new InspectionListItem();
                copiedItem.setId(item.getId());
                copiedItem.setIndex(item.getIndex());
                copiedItem.setName(item.getName());
                copiedItem.setGroup(item.getGroup());
                copiedItem.setTheme(item.getTheme());
                copiedItem.setField(item.getField());
                copiedItem.setStandardNo(item.getStandardNo());
                copiedItem.setMeasuringMethod(item.getMeasuringMethod());
                copiedItem.setDescription(item.getDescription());
                copiedItem.setInspectionMethod(item.getInspectionMethod());
                copiedItem.setStages(item.getStages().stream().map(stage -> {
                    InspectionListItemStage copiedStage = new InspectionListItemStage();
                    copiedStage.setStage(stage.getStage());
                    copiedStage.setName(stage.getName());
                    copiedStage.setImages(stage.getImages().stream().map(image -> {
                        InspectionListItemStageImage copiedImage = new InspectionListItemStageImage();
                        copiedImage.setMain(image.getMain());

                        KibFile copiedFile = kibFileService.copyById(image.getFileId());
                        copiedImage.setFileId(copiedFile.getId());

                        return copiedImage;
                    }).collect(Collectors.toList()));
                    return copiedStage;
                }).collect(Collectors.toList()));
                return copiedItem;
            }).toList();

            copy.setItems(copiedItems);

            return inspectionListRepository.save(copy);
        });
    }

    @Override
    @Transactional
    public Optional<InspectionList> copyItem(String id, String itemId) {
        return inspectionListRepository.findById(id).map(inspectionList -> {
            InspectionListItem item = inspectionList.getItems().stream().filter(i -> i.getId().equals(itemId)).findFirst().orElseThrow();

            InspectionListItem copiedItem = new InspectionListItem();
            copiedItem.setId(new ObjectId().toHexString());
            copiedItem.setIndex(inspectionList.getItems().size());

            Locale locale = LocaleContextHolder.getLocale();
            String copySuffix = messageSource.getMessage("copy.suffix", null, locale);
            String separator = messageSource.getMessage("copy.separator", null, locale);
            copiedItem.setName(item.getName() + " " + separator + " " + copySuffix);

            copiedItem.setGroup(item.getGroup());
            copiedItem.setTheme(item.getTheme());
            copiedItem.setField(item.getField());
            copiedItem.setStandardNo(item.getStandardNo());
            copiedItem.setMeasuringMethod(item.getMeasuringMethod());
            copiedItem.setDescription(item.getDescription());
            copiedItem.setInspectionMethod(item.getInspectionMethod());
            copiedItem.setStages(item.getStages().stream().map(stage -> {
                InspectionListItemStage copiedStage = new InspectionListItemStage();
                copiedStage.setStage(stage.getStage());
                copiedStage.setName(stage.getName());
                copiedStage.setImages(stage.getImages().stream().map(image -> {
                    InspectionListItemStageImage copiedImage = new InspectionListItemStageImage();
                    copiedImage.setMain(image.getMain());

                    KibFile copiedFile = kibFileService.copyById(image.getFileId());
                    copiedImage.setFileId(copiedFile.getId());

                    return copiedImage;
                }).collect(Collectors.toList()));
                return copiedStage;
            }).collect(Collectors.toList()));

            List<InspectionListItem> mutableItems = new ArrayList<>(inspectionList.getItems());
            mutableItems.add(copiedItem);
            inspectionList.setItems(mutableItems);

            return inspectionListRepository.save(inspectionList);
        });
    }

    //    @Scheduled(cron = "${app.inspection-list-service.delete-orphan-documents.cron}")
    private void deleteOrphanDocuments() {
        GridFSFindIterable files = this.kibFileService.findAll();
        for (GridFSFile file : files) {
            if (isOrphanDocument(file)) {
                this.kibFileService.deleteById(file.getObjectId().toHexString());
            }
        }
    }

    private boolean isOrphanDocument(GridFSFile file) {
        if (file.getMetadata() == null ||
                !file.getMetadata().containsKey("collection") ||
                !file.getMetadata().containsKey("String")) {
            return true;
        }

        String fileId = file.getObjectId().toHexString();
        String collection = String.valueOf(file.getMetadata().get("collection"));
        String objectId = String.valueOf(file.getMetadata().get("String"));

        if (collection.equals("inspection-list")) {
            Optional<InspectionList> inspectionListOptional = this.findById(objectId);

            if (inspectionListOptional.isEmpty()) {
                return true;
            }

            Stream<InspectionListItemStageImage> stageImages = inspectionListOptional.get().getItems().stream()
                    .flatMap(item -> item.getStages().stream())
                    .flatMap(stage -> stage.getImages().stream());

            return stageImages.noneMatch(stageImage -> stageImage.getFileId().equals(fileId));
        } else {
            return true;
        }
    }

    @Override
    public ImportResult<InspectionList> importInspectionList(ImportDocument importDocument) throws IOException {
        String base64Data = importDocument.getDocument();

        if (base64Data.startsWith("data:")) {
            base64Data = base64Data.substring(base64Data.indexOf(",") + 1);
        }

        byte[] fileBytes = Base64.getDecoder().decode(base64Data);

        return this.fileImportService.importExcelBluePrint(fileBytes, importDocument.getName());
    }
}
