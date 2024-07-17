package nl.rgs.kib.service.impl;

import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.model.method.dto.CreateInspectionMethod;
import nl.rgs.kib.repository.InspectionMethodRepository;
import nl.rgs.kib.service.InspectionMethodService;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InspectionMethodServiceImpl implements InspectionMethodService {

    @Autowired
    private InspectionMethodRepository inspectionMethodRepository;

    @Override
    public Long count() {
        return inspectionMethodRepository.count();
    }

    @Override
    public List<InspectionMethod> findAll() {
        return inspectionMethodRepository.findAll();
    }

    @Override
    public Optional<InspectionMethod> findById(ObjectId id) {
        return inspectionMethodRepository.findById(id);
    }

    @Override
    public InspectionMethod create(@NotNull() CreateInspectionMethod createInspectionMethod) {
        InspectionMethod inspectionMethod = new InspectionMethod();
        inspectionMethod.setName(createInspectionMethod.name());
        inspectionMethod.setInput(createInspectionMethod.input());
        inspectionMethod.setCalculationMethod(createInspectionMethod.calculationMethod());
        inspectionMethod.setStages(InspectionMethod.sortStages(createInspectionMethod.stages()));

        return inspectionMethodRepository.save(inspectionMethod);
    }

    @Override
    public Optional<InspectionMethod> update(@NotNull() InspectionMethod inspectionMethod) {
        return inspectionMethodRepository.findById(new ObjectId(inspectionMethod.getId())).map(existingMethod -> {
            existingMethod.setName(inspectionMethod.getName());
            existingMethod.setInput(inspectionMethod.getInput());
            existingMethod.setCalculationMethod(inspectionMethod.getCalculationMethod());
            existingMethod.setStages(InspectionMethod.sortStages(inspectionMethod.getStages()));
            
            return inspectionMethodRepository.save(existingMethod);
        });
    }

    @Override
    public Optional<InspectionMethod> deleteById(ObjectId id) {
        Optional<InspectionMethod> inspectionMethod = inspectionMethodRepository.findById(id);
        inspectionMethod.ifPresent(method -> inspectionMethodRepository.deleteById(id));
        return inspectionMethod;
    }
}
