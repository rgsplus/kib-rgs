package nl.rgs.kib.service;

import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import nl.rgs.kib.model.list.dto.SummaryInspectionList;
import nl.rgs.kib.shared.models.ImportDocument;
import nl.rgs.kib.shared.models.ImportResult;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface InspectionListService {
    List<SummaryInspectionList> findAllSummaries();

    Optional<InspectionList> findById(String id);

    InspectionList create(CreateInspectionList createInspectionList);

    Optional<InspectionList> update(InspectionList inspectionList);

    Optional<InspectionList> deleteById(String id);

    Optional<InspectionList> copy(String id);

    Optional<InspectionList> copyItem(String String, String itemId);

    ImportResult<InspectionList> importInspectionList(ImportDocument importDocument) throws IOException;
}
