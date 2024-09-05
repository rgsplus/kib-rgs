package nl.rgs.kib.repository;

import nl.rgs.kib.model.account.ApiAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiAccountRepository extends MongoRepository<ApiAccount, String> {
    Optional<ApiAccount> findByApiKey(String apiKey);
}
