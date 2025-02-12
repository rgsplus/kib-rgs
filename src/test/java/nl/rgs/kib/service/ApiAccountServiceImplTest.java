package nl.rgs.kib.service;

import nl.rgs.kib.model.account.ApiAccount;
import nl.rgs.kib.model.account.dto.CreateApiAccount;
import nl.rgs.kib.repository.ApiAccountRepository;
import nl.rgs.kib.service.impl.ApiAccountServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApiAccountServiceImplTest {
    @Mock
    private ApiAccountRepository apiAccountRepository;

    @InjectMocks
    private ApiAccountServiceImpl apiAccountService;

    @Test
    public void findAll_ReturnsAllApiAccounts() {
        ApiAccount account1 = new ApiAccount();
        account1.setName("example 1");
        ApiAccount account2 = new ApiAccount();
        account2.setName("example 2");

        List<ApiAccount> expectedList = List.of(account1, account2);

        when(apiAccountRepository.findAll()).thenReturn(expectedList);

        List<ApiAccount> result = apiAccountService.findAll();

        assertEquals(expectedList, result);

        verify(apiAccountRepository).findAll();
    }

    @Test
    public void findAll_ReturnsEmptyList() {
        when(apiAccountRepository.findAll()).thenReturn(List.of());

        List<ApiAccount> result = apiAccountService.findAll();

        assertTrue(result.isEmpty());

        verify(apiAccountRepository).findAll();
    }

    @Test
    public void findById_ReturnsApiAccount() {
        String id = ObjectId.get().toHexString();
        ApiAccount expected = new ApiAccount();
        expected.setId(id);

        when(apiAccountRepository.findById(id)).thenReturn(Optional.of(expected));

        Optional<ApiAccount> result = apiAccountService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());

        verify(apiAccountRepository).findById(id);
    }

    @Test
    public void findById_ReturnsEmptyBecauseAccountNotFound() {
        String id = ObjectId.get().toHexString();
        when(apiAccountRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ApiAccount> result = apiAccountService.findById(id);

        assertTrue(result.isEmpty());

        verify(apiAccountRepository).findById(id);
    }

    @Test
    public void findByApiKey_ReturnsApiAccount() {
        String apiKey = ApiAccount.generateApiKey();

        ApiAccount expected = new ApiAccount();
        expected.setApiKey(apiKey);

        when(apiAccountRepository.findByApiKey(apiKey)).thenReturn(Optional.of(expected));

        Optional<ApiAccount> result = apiAccountService.findByApiKey(apiKey);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());

        verify(apiAccountRepository).findByApiKey(apiKey);
    }

    @Test
    public void findByApiKey_ReturnsEmptyBecauseAccountNotFound() {
        String apiKey = ApiAccount.generateApiKey();

        when(apiAccountRepository.findByApiKey(apiKey)).thenReturn(Optional.empty());

        Optional<ApiAccount> result = apiAccountService.findByApiKey(apiKey);

        assertTrue(result.isEmpty());

        verify(apiAccountRepository).findByApiKey(apiKey);
    }

    @Test
    public void create_CreatesApiAccount() {
        Date startDate = new Date();
        CreateApiAccount createApiAccount = new CreateApiAccount(
                "John Doe",
                "Api account for Facebook",
                "Facebook",
                startDate,
                null,
                true
        );

        ApiAccount expected = new ApiAccount();
        expected.setId(ObjectId.get().toHexString());
        expected.setApiKey(ApiAccount.generateApiKey());
        expected.setName(createApiAccount.name());
        expected.setDescription(createApiAccount.description());
        expected.setBusinessName(createApiAccount.businessName());
        expected.setStartDate(createApiAccount.startDate());
        expected.setEndDate(createApiAccount.endDate());
        expected.setActive(createApiAccount.active());

        when(apiAccountRepository.save(any())).thenReturn(expected);

        ApiAccount result = apiAccountService.create(createApiAccount);

        assertEquals(expected, result);

        verify(apiAccountRepository).save(any());
    }

    @Test
    public void update_UpdatesApiAccount() {
        ApiAccount existingAccount = new ApiAccount();
        existingAccount.setId(ObjectId.get().toHexString());
        existingAccount.setName("existingName");
        existingAccount.setDescription("existingDescription");
        existingAccount.setBusinessName("existingBusinessName");
        existingAccount.setStartDate(new Date());
        existingAccount.setEndDate(new Date());
        existingAccount.setActive(true);

        ApiAccount updatedAccount = new ApiAccount();
        updatedAccount.setId(existingAccount.getId());
        updatedAccount.setName("updatedName");
        updatedAccount.setDescription("updatedDescription");
        updatedAccount.setBusinessName("updatedBusinessName");
        updatedAccount.setStartDate(new Date());
        updatedAccount.setEndDate(new Date());
        updatedAccount.setActive(true);

        when(apiAccountRepository.findById(existingAccount.getId())).thenReturn(Optional.of(existingAccount));
        when(apiAccountRepository.save(existingAccount)).thenReturn(existingAccount);

        Optional<ApiAccount> result = apiAccountService.update(updatedAccount);

        assertTrue(result.isPresent());
        assertEquals(existingAccount, result.get());

        verify(apiAccountRepository).findById(existingAccount.getId());
        verify(apiAccountRepository).save(existingAccount);
    }

    @Test
    public void update_NotUpdatesBecauseAccountNotFound() {
        ApiAccount updatedAccount = new ApiAccount();
        updatedAccount.setId(ObjectId.get().toHexString());
        updatedAccount.setName("updatedName");
        updatedAccount.setDescription("updatedDescription");
        updatedAccount.setBusinessName("updatedBusinessName");
        updatedAccount.setStartDate(new Date());
        updatedAccount.setEndDate(new Date());
        updatedAccount.setActive(true);

        when(apiAccountRepository.findById(updatedAccount.getId())).thenReturn(Optional.empty());

        Optional<ApiAccount> result = apiAccountService.update(updatedAccount);

        assertTrue(result.isEmpty());

        verify(apiAccountRepository).findById(updatedAccount.getId());
    }

    @Test
    public void deleteById_DeletesApiAccount() {
        String id = ObjectId.get().toHexString();
        ApiAccount account = new ApiAccount();
        when(apiAccountRepository.findById(id)).thenReturn(Optional.of(account));

        Optional<ApiAccount> result = apiAccountService.deleteById(id);

        assertTrue(result.isPresent());
        assertEquals(account, result.get());

        verify(apiAccountRepository).findById(id);
        verify(apiAccountRepository).deleteById(id);
    }

    @Test
    public void deleteById_NotDeletesBecauseAccountNotFound() {
        String id = ObjectId.get().toHexString();
        when(apiAccountRepository.findById(id)).thenReturn(Optional.empty());

        Optional<ApiAccount> result = apiAccountService.deleteById(id);

        assertTrue(result.isEmpty());

        verify(apiAccountRepository).findById(id);
    }
}
