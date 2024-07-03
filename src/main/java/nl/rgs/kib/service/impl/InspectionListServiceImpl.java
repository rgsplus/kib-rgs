package nl.rgs.kib.service.impl;

import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import nl.rgs.kib.repository.InspectionListRepository;
import nl.rgs.kib.service.InspectionListService;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InspectionListServiceImpl implements InspectionListService {

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
        inspectionList.setItems(createInspectionList.items());
        inspectionList.setLabels(createInspectionList.labels());
        return inspectionListRepository.save(inspectionList);
    }

    @Override
    public Optional<InspectionList> update(@NotNull() InspectionList inspectionList) {
        Optional<InspectionList> optionalInspectionList = inspectionListRepository.findById(new ObjectId(inspectionList.getId()));
        if (optionalInspectionList.isEmpty()) {
            return Optional.empty();
        }

        InspectionList existingInspectionList = optionalInspectionList.get();
        existingInspectionList.setName(inspectionList.getName());
        existingInspectionList.setStatus(inspectionList.getStatus());
        existingInspectionList.setItems(inspectionList.getItems());
        existingInspectionList.setLabels(inspectionList.getLabels());

        return Optional.of(inspectionListRepository.save(existingInspectionList));
    }

    @Override
    public Optional<InspectionList> deleteById(ObjectId id) {
        Optional<InspectionList> optionalInspectionList = inspectionListRepository.findById(id);
        if (optionalInspectionList.isEmpty()) {
            return Optional.empty();
        }

        inspectionListRepository.deleteById(id);
        return optionalInspectionList;
    }
}
