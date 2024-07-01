package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionListCode;

import java.util.List;
import java.util.Optional;

public interface InspectionListCodeService {
     List<InspectionListCode> findAll();

     Optional<InspectionListCode> findById(String id);
}
