package nl.rgs.kib.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rgs.kib.model.account.ApiAccount;
import nl.rgs.kib.model.file.KibFile;
import nl.rgs.kib.service.ApiAccountService;
import nl.rgs.kib.service.KibFileService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiKibFileController.class)
public class ApiKIbFileControllerTest {

    private final static String domain = "/api/kib-file";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KibFileService kibFileService;

    @MockBean
    private ApiAccountService apiAccountService;

    private ApiAccount apiAccount;

    @BeforeEach
    public void setUp() {
        apiAccount = new ApiAccount();
        apiAccount.setId(new ObjectId().toHexString());
        apiAccount.setApiKey(ApiAccount.generateApiKey());
        apiAccount.setName("Test");
        apiAccount.setBusinessName("Facebook");
        apiAccount.setStartDate(new Date(0));
        apiAccount.setEndDate(null);
        apiAccount.setActive(true);
    }

    @Test
    @WithMockUser
    public void findById_WhenNotExists_Returns404() throws Exception {
        String apiKey = ApiAccount.generateApiKey();
        apiAccount.setApiKey(apiKey);
        when(apiAccountService.findByApiKey(apiKey)).thenReturn(Optional.of(apiAccount));

        String id = new ObjectId().toHexString();
        when(kibFileService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(domain + "/" + id).contentType(MediaType.APPLICATION_JSON).header("api-key", apiKey)).andExpect(status().isNotFound());

        verify(apiAccountService).findByApiKey(apiKey);
        verify(kibFileService).findById(id);
    }

    @Test
    public void findById_WithoutApiKey_Returns401() throws Exception {
        String id = new ObjectId().toHexString();
        mockMvc.perform(get(domain + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void findById_WhenExists_Returns200() throws Exception {
        String apiKey = ApiAccount.generateApiKey();
        apiAccount.setApiKey(apiKey);

        KibFile kibFile = new KibFile();
        kibFile.setId(new ObjectId().toHexString());
        kibFile.setName("Test");
        kibFile.setType("text/plain");
        kibFile.setData("Test".getBytes());

        when(apiAccountService.findByApiKey(apiKey)).thenReturn(Optional.of(apiAccount));
        when(kibFileService.findById(kibFile.getId())).thenReturn(Optional.of(kibFile));

        mockMvc.perform(get(domain + "/" + kibFile.getId()).contentType(MediaType.APPLICATION_JSON).header("api-key", apiKey)).andExpect(status().isOk());

        verify(apiAccountService).findByApiKey(apiKey);
        verify(kibFileService).findById(kibFile.getId());
    }
}