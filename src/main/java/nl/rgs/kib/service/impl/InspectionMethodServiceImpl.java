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
        return inspectionMethodRepository.save(inspectionMethod);
    }

    @Override
    public Optional<InspectionMethod> update(@NotNull() InspectionMethod inspectionMethod) {
        Optional<InspectionMethod> inspectionMethodOptional = inspectionMethodRepository.findById(new ObjectId(inspectionMethod.getId()));
        if(inspectionMethodOptional.isEmpty()){
            return Optional.empty();
        }

        InspectionMethod inspectionMethodToUpdate = inspectionMethodOptional.get();
        inspectionMethodToUpdate.setName(inspectionMethod.getName());
        inspectionMethodToUpdate.setInput(inspectionMethod.getInput());
        inspectionMethodToUpdate.setCalculationMethod(inspectionMethod.getCalculationMethod());

        return Optional.of(inspectionMethodRepository.save(inspectionMethodToUpdate));
    }

    @Override
    public Optional<InspectionMethod> deleteById(ObjectId id) {
        Optional<InspectionMethod> inspectionMethodOptional = inspectionMethodRepository.findById(id);
        if(inspectionMethodOptional.isEmpty()){
            return Optional.empty();
        }

        inspectionMethodRepository.deleteById(id);
        return inspectionMethodOptional;
    }
}
