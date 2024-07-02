package nl.rgs.kib.service.impl;

import nl.rgs.kib.model.list.InspectionListCode;
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
}
