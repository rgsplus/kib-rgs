package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface InspectionListService {
    List<InspectionList> findAll();

    Optional<InspectionList> findById(ObjectId id);

    InspectionList create(CreateInspectionList createInspectionList);

    Optional<InspectionList> update(InspectionList inspectionMethod);

    Optional<InspectionList> deleteById(ObjectId id);

    Optional<InspectionList> copy(ObjectId id);

    Optional<InspectionList> copyItem(ObjectId objectId, String itemId);
}
