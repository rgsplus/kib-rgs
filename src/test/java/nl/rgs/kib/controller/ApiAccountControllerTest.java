package nl.rgs.kib.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rgs.kib.model.account.ApiAccount;
import nl.rgs.kib.model.account.dto.CreateApiAccount;
import nl.rgs.kib.model.method.InspectionMethodCalculationMethod;
import nl.rgs.kib.model.method.InspectionMethodInput;
import nl.rgs.kib.model.method.dto.CreateInspectionMethod;
import nl.rgs.kib.service.ApiAccountService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiAccountController.class)
public class ApiAccountControllerTest {

    private final static String domain = "/api-account";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApiAccountService apiAccountService;

    @Test
    @WithMockUser()
    public void findAll_Returns200() throws Exception {
        mockMvc.perform(get(domain)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(apiAccountService).findAll();
    }

    @Test
    public void findAll_WithoutAuthentication_Returns401() throws Exception {
        mockMvc.perform(get(domain)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser()
    public void findById_WhenExists_Returns200() throws Exception {
        String id = new ObjectId().toHexString();
        ApiAccount apiAccount = new ApiAccount();
        apiAccount.setId(id);
        when(apiAccountService.findById(id)).thenReturn(Optional.of(apiAccount));

        mockMvc.perform(get(domain + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(apiAccountService).findById(id);
    }

    @Test
    @WithMockUser()
    public void findById_WhenNotExists_Returns404() throws Exception {
        String id = new ObjectId().toHexString();
        when(apiAccountService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(domain + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(apiAccountService).findById(id);
    }

    @Test
    public void findById_WithoutAuthentication_Returns401() throws Exception {
        String id = new ObjectId().toHexString();
        mockMvc.perform(get(domain + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser()
    public void create_Returns201() throws Exception {
        CreateApiAccount createApiAccount = new CreateApiAccount(
                "test",
                "Facebook",
                new Date(),
                null,
                true
        );

        ApiAccount apiAccount = new ApiAccount();
        apiAccount.setId(new ObjectId().toHexString());
        apiAccount.setApiKey(ApiAccount.generateApiKey());
        apiAccount.setName(createApiAccount.name());
        apiAccount.setBusinessName(createApiAccount.businessName());
        apiAccount.setStartDate(createApiAccount.startDate());
        apiAccount.setEndDate(createApiAccount.endDate());
        apiAccount.setActive(createApiAccount.active());

        when(apiAccountService.create(createApiAccount)).thenReturn(apiAccount);

        mockMvc.perform(post(domain)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createApiAccount)))
                .andExpect(status().isCreated());

        verify(apiAccountService).create(createApiAccount);
    }

    @Test
    @WithMockUser()
    public void create_WhenInvalid_Returns400() throws Exception {
        CreateInspectionMethod createInspectionMethod = new CreateInspectionMethod(
                null,
                InspectionMethodInput.STAGE,
                InspectionMethodCalculationMethod.NEN2767,
                List.of()
        );

        mockMvc.perform(post(domain)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInspectionMethod)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_WithoutAuthentication_Returns401() throws Exception {
        CreateInspectionMethod createInspectionMethod = new CreateInspectionMethod(
                "test",
                InspectionMethodInput.STAGE,
                InspectionMethodCalculationMethod.NEN2767,
                List.of()
        );

        mockMvc.perform(post(domain)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInspectionMethod)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser()
    public void update_WhenExists_Returns200() throws Exception {
        ApiAccount apiAccount = new ApiAccount();
        apiAccount.setId(new ObjectId().toHexString());
        apiAccount.setApiKey(ApiAccount.generateApiKey());
        apiAccount.setName("test");
        apiAccount.setBusinessName("Facebook");
        apiAccount.setStartDate(new Date());
        apiAccount.setEndDate(null);
        apiAccount.setActive(true);

        when(apiAccountService.update(apiAccount)).thenReturn(Optional.of(apiAccount));

        mockMvc.perform(put(domain + "/" + apiAccount.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apiAccount)))
                .andExpect(status().isOk());

        verify(apiAccountService).update(apiAccount);
    }

    @Test
    @WithMockUser()
    public void update_WhenInvalid_Returns400() throws Exception {
        ApiAccount apiAccount = new ApiAccount();
        apiAccount.setId(new ObjectId().toHexString());
        apiAccount.setApiKey(ApiAccount.generateApiKey());
        apiAccount.setName("");
        apiAccount.setBusinessName("Facebook");
        apiAccount.setStartDate(new Date());
        apiAccount.setEndDate(null);
        apiAccount.setActive(true);

        mockMvc.perform(put(domain + "/" + apiAccount.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apiAccount)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser()
    public void update_WhenNotExists_Returns404() throws Exception {
        ApiAccount apiAccount = new ApiAccount();
        apiAccount.setId(new ObjectId().toHexString());
        apiAccount.setApiKey(ApiAccount.generateApiKey());
        apiAccount.setName("test");
        apiAccount.setBusinessName("Facebook");
        apiAccount.setStartDate(new Date());
        apiAccount.setEndDate(null);
        apiAccount.setActive(true);

        when(apiAccountService.update(apiAccount)).thenReturn(Optional.empty());

        mockMvc.perform(put(domain + "/" + apiAccount.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apiAccount)))
                .andExpect(status().isNotFound());

        verify(apiAccountService).update(apiAccount);
    }

    @Test
    public void update_WithoutAuthentication_Returns401() throws Exception {
        ApiAccount apiAccount = new ApiAccount();
        apiAccount.setId(new ObjectId().toHexString());
        apiAccount.setApiKey(ApiAccount.generateApiKey());
        apiAccount.setName("test");
        apiAccount.setBusinessName("Facebook");
        apiAccount.setStartDate(new Date());
        apiAccount.setEndDate(null);
        apiAccount.setActive(true);

        mockMvc.perform(put(domain + "/" + apiAccount.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(apiAccount)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser()
    public void deleteById_WhenExists_Returns204() throws Exception {
        ApiAccount apiAccount = new ApiAccount();
        apiAccount.setId(new ObjectId().toHexString());

        when(apiAccountService.deleteById(apiAccount.getId())).thenReturn(Optional.of(apiAccount));

        mockMvc.perform(delete(domain + "/" + apiAccount.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(apiAccountService).deleteById(apiAccount.getId());
    }

    @Test
    @WithMockUser()
    public void deleteById_WhenNotExists_Returns404() throws Exception {
        String id = new ObjectId().toHexString();

        when(apiAccountService.deleteById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete(domain + "/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(apiAccountService).deleteById(id);
    }

    @Test
    public void deleteById_WithoutAuthentication_Returns401() throws Exception {
        String id = new ObjectId().toHexString();

        mockMvc.perform(delete(domain + "/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
