package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.model.method.InspectionMethodCode;
import nl.rgs.kib.model.method.dto.CreateInspectionMethodCode;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface InspectionMethodCodeService {
    List<InspectionMethodCode> findAll();

    Optional<InspectionMethodCode> findById(ObjectId id);

    InspectionMethodCode create(CreateInspectionMethodCode createInspectionMethodCode);

    Optional<InspectionMethodCode> update(ObjectId id, InspectionMethodCode inspectionMethodCode);

    Optional<InspectionMethodCode> deleteById(ObjectId id);
}
