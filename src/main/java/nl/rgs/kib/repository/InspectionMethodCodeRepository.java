package nl.rgs.kib.repository;

import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.model.method.InspectionMethodCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionMethodCodeRepository extends MongoRepository<InspectionMethodCode, ObjectId> {
}
