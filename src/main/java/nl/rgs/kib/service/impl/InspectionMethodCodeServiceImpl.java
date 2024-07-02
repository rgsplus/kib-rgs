package nl.rgs.kib.service.impl;

import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.model.method.InspectionMethodCode;
import nl.rgs.kib.model.method.dto.CreateInspectionMethodCode;
import nl.rgs.kib.repository.InspectionListCodeRepository;
import nl.rgs.kib.repository.InspectionMethodCodeRepository;
import nl.rgs.kib.service.InspectionMethodCodeService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InspectionMethodCodeServiceImpl implements InspectionMethodCodeService {

    @Autowired
    private InspectionMethodCodeRepository inspectionMethodCodeRepository;

    @Override
    public List<InspectionMethodCode> findAll() {
        return inspectionMethodCodeRepository.findAll();
    }

    @Override
    public Optional<InspectionMethodCode> findById(ObjectId id) {
        return inspectionMethodCodeRepository.findById(id);
    }

    @Override
    public InspectionMethodCode create(CreateInspectionMethodCode createInspectionMethodCode) {
        InspectionMethodCode inspectionMethodCode = new InspectionMethodCode();
        inspectionMethodCode.setName(createInspectionMethodCode.name());
        inspectionMethodCode.setInput(createInspectionMethodCode.input());
        inspectionMethodCode.setCalculationMethod(createInspectionMethodCode.calculationMethod());
        return inspectionMethodCodeRepository.save(inspectionMethodCode);
    }

    @Override
    public Optional<InspectionMethodCode> update(InspectionMethodCode inspectionMethodCode) {
        Optional<InspectionMethodCode> inspectionMethodCodeOptional = inspectionMethodCodeRepository.findById(new ObjectId(inspectionMethodCode.getId()));
        if(inspectionMethodCodeOptional.isEmpty()){
            return Optional.empty();
        }

        InspectionMethodCode inspectionMethodCodeToUpdate = inspectionMethodCodeOptional.get();
        inspectionMethodCodeToUpdate.setName(inspectionMethodCode.getName());
        inspectionMethodCodeToUpdate.setInput(inspectionMethodCode.getInput());
        inspectionMethodCodeToUpdate.setCalculationMethod(inspectionMethodCode.getCalculationMethod());

        return Optional.of(inspectionMethodCodeRepository.save(inspectionMethodCodeToUpdate));
    }

    @Override
    public Optional<InspectionMethodCode> deleteById(ObjectId id) {
        Optional<InspectionMethodCode> inspectionMethodCodeOptional = inspectionMethodCodeRepository.findById(id);
        if(inspectionMethodCodeOptional.isEmpty()){
            return Optional.empty();
        }

        inspectionMethodCodeRepository.deleteById(id);
        return inspectionMethodCodeOptional;
    }
}
