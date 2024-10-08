package nl.rgs.kib.service;

import nl.rgs.kib.model.account.ApiAccount;
import nl.rgs.kib.model.account.dto.CreateApiAccount;

import java.util.List;
import java.util.Optional;

public interface ApiAccountService {
    List<ApiAccount> findAll();

    Optional<ApiAccount> findById(String id);

    Optional<ApiAccount> findByApiKey(String apiKey);

    ApiAccount create(CreateApiAccount createApiAccount);

    Optional<ApiAccount> update(ApiAccount apiAccount);

    Optional<ApiAccount> deleteById(String id);
}
