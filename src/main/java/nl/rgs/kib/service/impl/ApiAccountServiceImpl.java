package nl.rgs.kib.service.impl;

import nl.rgs.kib.model.account.ApiAccount;
import nl.rgs.kib.model.account.dto.CreateApiAccount;
import nl.rgs.kib.repository.ApiAccountRepository;
import nl.rgs.kib.service.ApiAccountService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApiAccountServiceImpl implements ApiAccountService {
    @Autowired
    private ApiAccountRepository apiAccountRepository;

    @Override
    public List<ApiAccount> findAll() {
        return this.apiAccountRepository.findAll();
    }

    @Override
    public Optional<ApiAccount> findById(String id) {
        return this.apiAccountRepository.findById(id);
    }

    @Override
    public Optional<ApiAccount> findByApiKey(String apiKey) {
        return this.apiAccountRepository.findByApiKey(apiKey);
    }

    @Override
    public ApiAccount create(@NotNull() CreateApiAccount createApiAccount) {
        ApiAccount apiAccount = new ApiAccount();
        apiAccount.setApiKey(ApiAccount.generateApiKey());
        apiAccount.setName(createApiAccount.name());
        apiAccount.setBusinessName(createApiAccount.businessName());
        apiAccount.setStartDate(createApiAccount.startDate());
        apiAccount.setEndDate(createApiAccount.endDate());
        apiAccount.setActive(createApiAccount.active());

        return this.apiAccountRepository.save(apiAccount);
    }

    @Override
    public Optional<ApiAccount> update(@NotNull() ApiAccount apiAccount) {
        return apiAccountRepository.findById(apiAccount.getId()).map(existingApiAccount -> {
            existingApiAccount.setName(apiAccount.getName());
            existingApiAccount.setBusinessName(apiAccount.getBusinessName());
            existingApiAccount.setStartDate(apiAccount.getStartDate());
            existingApiAccount.setEndDate(apiAccount.getEndDate());
            existingApiAccount.setActive(apiAccount.getActive());

            return apiAccountRepository.save(existingApiAccount);
        });
    }

    @Override
    public Optional<ApiAccount> deleteById(String id) {
        Optional<ApiAccount> apiAccount = apiAccountRepository.findById(id);
        apiAccount.ifPresent(method -> apiAccountRepository.deleteById(id));
        return apiAccount;
    }
}
