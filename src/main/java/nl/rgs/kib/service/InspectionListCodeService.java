package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.model.method.InspectionMethodCode;
import nl.rgs.kib.model.method.dto.CreateInspectionMethodCode;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface InspectionListCodeService {
     List<InspectionListCode> findAll();

     Optional<InspectionListCode> findById(ObjectId id);
}
