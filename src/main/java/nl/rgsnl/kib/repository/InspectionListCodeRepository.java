package nl.rgsnl.kib.repository;

import nl.rgsnl.kib.model.InspectionListCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.io.ObjectInput;

@Repository
public interface InspectionListCodeRepository extends MongoRepository<InspectionListCode, ObjectId> {
}
