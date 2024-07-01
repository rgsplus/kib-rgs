package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.model.method.InspectionMethodCode;

import java.util.List;
import java.util.Optional;

public interface InspectionMethodCodeService {
    List<InspectionMethodCode> findAll();

    Optional<InspectionMethodCode> findById(String id);
}
