package nl.rgs.kib.service;

import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.model.method.dto.CreateInspectionMethod;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface InspectionMethodService {
    List<InspectionMethod> findAll();

    Optional<InspectionMethod> findById(ObjectId id);

    InspectionMethod create(CreateInspectionMethod createInspectionMethod);

    Optional<InspectionMethod> update(InspectionMethod inspectionMethod);

    Optional<InspectionMethod> deleteById(ObjectId id);
}
