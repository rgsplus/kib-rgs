package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import nl.rgs.kib.shared.models.ImportDocument;
import nl.rgs.kib.shared.models.ImportResult;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface InspectionListService {
    List<InspectionList> findAll();

    Optional<InspectionList> findById(ObjectId id);

    InspectionList create(CreateInspectionList createInspectionList);

    Optional<InspectionList> update(InspectionList inspectionList);

    Optional<InspectionList> deleteById(ObjectId id);

    Optional<InspectionList> copy(ObjectId id);

    Optional<InspectionList> copyItem(ObjectId objectId, String itemId);

    ImportResult<InspectionList> importInspectionList(ImportDocument importDocument) throws IOException;
}
