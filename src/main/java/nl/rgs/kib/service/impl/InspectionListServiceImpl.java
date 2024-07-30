package nl.rgs.kib.service.impl;

import nl.rgs.kib.model.file.KibFile;
import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.InspectionListItem;
import nl.rgs.kib.model.list.InspectionListItemStage;
import nl.rgs.kib.model.list.InspectionListItemStageImage;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import nl.rgs.kib.repository.InspectionListRepository;
import nl.rgs.kib.service.InspectionListService;
import nl.rgs.kib.service.KibFileService;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InspectionListServiceImpl implements InspectionListService {

    @Autowired
    private KibFileService kibFileService;

    @Autowired
    private InspectionListRepository inspectionListRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public List<InspectionList> findAll() {
        return inspectionListRepository.findAll();
    }

    @Override
    public Optional<InspectionList> findById(ObjectId id) {
        return inspectionListRepository.findById(id);
    }

    @Override
    public InspectionList create(@NotNull() CreateInspectionList createInspectionList) {
        InspectionList inspectionList = new InspectionList();
        inspectionList.setName(createInspectionList.name());
        inspectionList.setStatus(createInspectionList.status());
        inspectionList.setItems(InspectionList.sortItemsStagesAndImages(createInspectionList.items()));
        inspectionList.setLabels(InspectionList.sortLabelsAndFeatures(createInspectionList.labels()));

        return inspectionListRepository.save(inspectionList);
    }

    @Override
    @Transactional
    public Optional<InspectionList> update(@NotNull() InspectionList inspectionList) {
        return inspectionListRepository.findById(new ObjectId(inspectionList.getId())).map(existingList -> {
            List<ObjectId> deletedFileIds = InspectionList.getDeletedFileIds(existingList, inspectionList);
            kibFileService.deleteByIds(deletedFileIds);

            existingList.setName(inspectionList.getName());
            existingList.setStatus(inspectionList.getStatus());
            existingList.setItems(InspectionList.sortItemsStagesAndImages(inspectionList.getItems()));
            existingList.setLabels(InspectionList.sortLabelsAndFeatures(inspectionList.getLabels()));

            return inspectionListRepository.save(existingList);
        });
    }

    @Override
    @Transactional
    public Optional<InspectionList> deleteById(ObjectId id) {
        Optional<InspectionList> inspectionList = inspectionListRepository.findById(id);
        inspectionList.ifPresent(list ->
                {
                    List<ObjectId> allFileIds = InspectionList.getAllFileIds(list);
                    kibFileService.deleteByIds(allFileIds);

                    inspectionListRepository.deleteById(id);
                }
        );
        return inspectionList;
    }

    @Override
    @Transactional
    public Optional<InspectionList> copy(ObjectId id) {
        return inspectionListRepository.findById(id).map(inspectionList -> {
            InspectionList copy = new InspectionList();

            Locale locale = LocaleContextHolder.getLocale();
            String copySuffix = messageSource.getMessage("copy.suffix", null, locale);
            String separator = messageSource.getMessage("copy.separator", null, locale);
            copy.setName(inspectionList.getName() + " " + separator + " " + copySuffix);

            copy.setStatus(inspectionList.getStatus());
            copy.setLabels(inspectionList.getLabels());

            List<InspectionListItem> copiedItems = inspectionList.getItems().stream().map(item -> {
                InspectionListItem copiedItem = new InspectionListItem();
                copiedItem.setId(item.getId());
                copiedItem.setIndex(item.getIndex());
                copiedItem.setName(item.getName());
                copiedItem.setGroup(item.getGroup());
                copiedItem.setCategory(item.getCategory());
                copiedItem.setInspectionMethod(item.getInspectionMethod());
                copiedItem.setStages(item.getStages().stream().map(stage -> {
                    InspectionListItemStage copiedStage = new InspectionListItemStage();
                    copiedStage.setStage(stage.getStage());
                    copiedStage.setName(stage.getName());
                    copiedStage.setMax(stage.getMax());
                    copiedStage.setImages(stage.getImages().stream().map(image -> {
                        InspectionListItemStageImage copiedImage = new InspectionListItemStageImage();
                        copiedImage.setMain(image.getMain());

                        KibFile copiedFile = kibFileService.copyById(new ObjectId(image.getFileId()));
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
    public Optional<InspectionList> copyItem(ObjectId id, String itemId) {
        return inspectionListRepository.findById(id).map(inspectionList -> {
            InspectionListItem item = inspectionList.getItems().stream().filter(i -> i.getId().equals(itemId)).findFirst().orElseThrow();

            InspectionListItem copiedItem = new InspectionListItem();
            copiedItem.setId(new ObjectId().toString());
            copiedItem.setIndex(inspectionList.getItems().size());

            Locale locale = LocaleContextHolder.getLocale();
            String copySuffix = messageSource.getMessage("copy.suffix", null, locale);
            String separator = messageSource.getMessage("copy.separator", null, locale);
            copiedItem.setName(item.getName() + " " + separator + " " + copySuffix);

            copiedItem.setGroup(item.getGroup());
            copiedItem.setCategory(item.getCategory());
            copiedItem.setInspectionMethod(item.getInspectionMethod());
            copiedItem.setStages(item.getStages().stream().map(stage -> {
                InspectionListItemStage copiedStage = new InspectionListItemStage();
                copiedStage.setStage(stage.getStage());
                copiedStage.setName(stage.getName());
                copiedStage.setMax(stage.getMax());
                copiedStage.setImages(stage.getImages().stream().map(image -> {
                    InspectionListItemStageImage copiedImage = new InspectionListItemStageImage();
                    copiedImage.setMain(image.getMain());

                    KibFile copiedFile = kibFileService.copyById(new ObjectId(image.getFileId()));
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
}
