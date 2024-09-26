package nl.rgs.kib.repository;

import nl.rgs.kib.model.list.InspectionList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionListRepository extends MongoRepository<InspectionList, String> {
}
