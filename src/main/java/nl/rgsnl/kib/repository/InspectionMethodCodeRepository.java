package nl.rgsnl.kib.repository;

import nl.rgsnl.kib.model.InspectionListCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionMethodCodeRepository extends MongoRepository<InspectionListCode, ObjectId> {
}
