package nl.rgs.kib.repository;

import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.dto.SummaryInspectionList;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionListRepository extends MongoRepository<InspectionList, String> {
    @Aggregation(pipeline = {
            "{ $project: { id: '$_id', name: 1, status: 1, totalItems: { $size: '$items' }, _metadata: 1 } }"
    })
    List<SummaryInspectionList> findAllSummaries();
}
