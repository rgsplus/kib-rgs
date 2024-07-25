package nl.rgs.kib.service.impl;

import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import nl.rgs.kib.repository.InspectionListRepository;
import nl.rgs.kib.service.InspectionListService;
import nl.rgs.kib.service.KibFileService;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InspectionListServiceImpl implements InspectionListService {

    @Autowired
    private KibFileService kibFileService;

    @Autowired
    private InspectionListRepository inspectionListRepository;

    @Override
    public Long count() {
        return inspectionListRepository.count();
    }

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
}
