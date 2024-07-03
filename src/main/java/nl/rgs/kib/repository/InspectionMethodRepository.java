package nl.rgs.kib.repository;

import nl.rgs.kib.model.method.InspectionMethod;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionMethodRepository extends MongoRepository<InspectionMethod, ObjectId> {
}
