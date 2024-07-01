package nl.rgsnl.kib.repository;

import nl.rgsnl.kib.model.list.InspectionListCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionListCodeRepository extends MongoRepository<InspectionListCode, ObjectId> {
}
