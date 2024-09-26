package nl.rgs.kib.service;

import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.model.method.dto.CreateInspectionMethod;

import java.util.List;
import java.util.Optional;

public interface InspectionMethodService {
    List<InspectionMethod> findAll();

    Optional<InspectionMethod> findById(String id);

    Optional<InspectionMethod> findByName(String name);

    InspectionMethod create(CreateInspectionMethod createInspectionMethod);

    Optional<InspectionMethod> update(InspectionMethod inspectionMethod);

    Optional<InspectionMethod> deleteById(String id);
}
