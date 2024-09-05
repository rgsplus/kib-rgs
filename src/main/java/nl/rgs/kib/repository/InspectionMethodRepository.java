package nl.rgs.kib.repository;

import nl.rgs.kib.model.method.InspectionMethod;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InspectionMethodRepository extends MongoRepository<InspectionMethod, String> {
    Optional<InspectionMethod> findByName(String name);
}
