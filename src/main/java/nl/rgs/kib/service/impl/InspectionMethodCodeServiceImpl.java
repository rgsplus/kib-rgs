package nl.rgs.kib.service.impl;

import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.model.method.InspectionMethodCode;
import nl.rgs.kib.repository.InspectionListCodeRepository;
import nl.rgs.kib.repository.InspectionMethodCodeRepository;
import nl.rgs.kib.service.InspectionMethodCodeService;
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
        return List.of();
    }

    @Override
    public Optional<InspectionMethodCode> findById(String id) {
        return Optional.empty();
    }
}
