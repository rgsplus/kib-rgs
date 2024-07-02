package nl.rgs.kib.service.impl;

import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.model.list.dto.CreateInspectionListCode;
import nl.rgs.kib.repository.InspectionListCodeRepository;
import nl.rgs.kib.service.InspectionListCodeService;
import nl.rgs.kib.model.list.InspectionListCodeStatus;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InspectionListCodeServiceImpl implements InspectionListCodeService {

    @Autowired
    private InspectionListCodeRepository inspectionListCodeRepository;

    @Override
    public List<InspectionListCode> findAll() {
        return inspectionListCodeRepository.findAll();
    }

    @Override
    public Optional<InspectionListCode> findById(ObjectId id) {
        return inspectionListCodeRepository.findById(id);
    }

    @Override
    public InspectionListCode create(CreateInspectionListCode createInspectionListCode) {
        InspectionListCode inspectionListCode = new InspectionListCode();
        inspectionListCode.setName(createInspectionListCode.name());
        inspectionListCode.setStatus(createInspectionListCode.status());
        inspectionListCode.setItems(createInspectionListCode.items());
        inspectionListCode.setLabels(createInspectionListCode.labels());
        return inspectionListCodeRepository.save(inspectionListCode);
    }

    @Override
    public Optional<InspectionListCode> update(InspectionListCode inspectionListCode) {
        Optional<InspectionListCode> optionalInspectionListCode = inspectionListCodeRepository.findById(new ObjectId(inspectionListCode.getId()));
        if (optionalInspectionListCode.isEmpty()) {
            return Optional.empty();
        }

        InspectionListCode existingInspectionListCode = optionalInspectionListCode.get();
        existingInspectionListCode.setName(inspectionListCode.getName());
        existingInspectionListCode.setStatus(inspectionListCode.getStatus());
        existingInspectionListCode.setItems(inspectionListCode.getItems());
        existingInspectionListCode.setLabels(inspectionListCode.getLabels());

        return Optional.of(inspectionListCodeRepository.save(existingInspectionListCode));
    }

    @Override
    public Optional<InspectionListCode> deleteById(ObjectId id) {
        Optional<InspectionListCode> optionalInspectionListCode = inspectionListCodeRepository.findById(id);
        if (optionalInspectionListCode.isEmpty()) {
            return Optional.empty();
        }

        inspectionListCodeRepository.deleteById(id);
        return optionalInspectionListCode;
    }
}
