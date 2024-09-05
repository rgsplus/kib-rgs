package nl.rgs.kib.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.model.method.InspectionMethodCalculationMethod;
import nl.rgs.kib.model.method.InspectionMethodInput;
import nl.rgs.kib.model.method.dto.CreateInspectionMethod;
import nl.rgs.kib.service.ApiAccountService;
import nl.rgs.kib.service.InspectionMethodService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InspectionMethodController.class)
public class InspectionMethodControllerTest {

    private final static String domain = "/inspection-method";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InspectionMethodService inspectionMethodService;

    @MockBean
    private ApiAccountService apiAccountService;

    @Test
    @WithMockUser()
    public void findAll_Returns200() throws Exception {
        mockMvc.perform(get(domain)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(inspectionMethodService).findAll();
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
        InspectionMethod inspectionMethod = new InspectionMethod();
        inspectionMethod.setId(id);
        when(inspectionMethodService.findById(id)).thenReturn(Optional.of(inspectionMethod));

        mockMvc.perform(get(domain + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(inspectionMethodService).findById(id);
    }

    @Test
    @WithMockUser()
    public void findById_WhenNotExists_Returns404() throws Exception {
        String id = new ObjectId().toHexString();
        when(inspectionMethodService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(domain + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inspectionMethodService).findById(id);
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
        CreateInspectionMethod createInspectionMethod = new CreateInspectionMethod(
                "test",
                InspectionMethodInput.STAGE,
                InspectionMethodCalculationMethod.NEN2767,
                List.of()
        );

        InspectionMethod inspectionMethod = new InspectionMethod();
        inspectionMethod.setId(new ObjectId().toHexString());
        inspectionMethod.setName(createInspectionMethod.name());
        inspectionMethod.setInput(createInspectionMethod.input());
        inspectionMethod.setCalculationMethod(createInspectionMethod.calculationMethod());
        inspectionMethod.setStages(createInspectionMethod.stages());

        when(inspectionMethodService.create(createInspectionMethod)).thenReturn(inspectionMethod);

        mockMvc.perform(post(domain)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInspectionMethod)))
                .andExpect(status().isCreated());

        verify(inspectionMethodService).create(createInspectionMethod);
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
        InspectionMethod inspectionMethod = new InspectionMethod();
        inspectionMethod.setId(new ObjectId().toHexString());
        inspectionMethod.setName("test");
        inspectionMethod.setInput(InspectionMethodInput.STAGE);
        inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
        inspectionMethod.setStages(List.of());

        when(inspectionMethodService.update(inspectionMethod)).thenReturn(Optional.of(inspectionMethod));

        mockMvc.perform(put(domain + "/" + inspectionMethod.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inspectionMethod)))
                .andExpect(status().isOk());

        verify(inspectionMethodService).update(inspectionMethod);
    }

    @Test
    @WithMockUser()
    public void update_WhenInvalid_Returns400() throws Exception {
        InspectionMethod inspectionMethod = new InspectionMethod();
        inspectionMethod.setId(new ObjectId().toHexString());
        inspectionMethod.setName(null);
        inspectionMethod.setInput(InspectionMethodInput.STAGE);
        inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
        inspectionMethod.setStages(List.of());

        mockMvc.perform(put(domain + "/" + inspectionMethod.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inspectionMethod)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser()
    public void update_WhenNotExists_Returns404() throws Exception {
        InspectionMethod inspectionMethod = new InspectionMethod();
        inspectionMethod.setId(new ObjectId().toHexString());
        inspectionMethod.setName("test");
        inspectionMethod.setInput(InspectionMethodInput.STAGE);
        inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
        inspectionMethod.setStages(List.of());

        when(inspectionMethodService.update(inspectionMethod)).thenReturn(Optional.empty());

        mockMvc.perform(put(domain + "/" + inspectionMethod.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inspectionMethod)))
                .andExpect(status().isNotFound());

        verify(inspectionMethodService).update(inspectionMethod);
    }

    @Test
    public void update_WithoutAuthentication_Returns401() throws Exception {
        InspectionMethod inspectionMethod = new InspectionMethod();
        inspectionMethod.setId(new ObjectId().toHexString());
        inspectionMethod.setName("test");
        inspectionMethod.setInput(InspectionMethodInput.STAGE);
        inspectionMethod.setCalculationMethod(InspectionMethodCalculationMethod.NEN2767);
        inspectionMethod.setStages(List.of());

        mockMvc.perform(put(domain + "/" + inspectionMethod.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inspectionMethod)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser()
    public void deleteById_WhenExists_Returns204() throws Exception {
        InspectionMethod inspectionMethod = new InspectionMethod();
        inspectionMethod.setId(new ObjectId().toHexString());

        when(inspectionMethodService.deleteById(inspectionMethod.getId())).thenReturn(Optional.of(inspectionMethod));

        mockMvc.perform(delete(domain + "/" + inspectionMethod.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(inspectionMethodService).deleteById(inspectionMethod.getId());
    }

    @Test
    @WithMockUser()
    public void deleteById_WhenNotExists_Returns404() throws Exception {
        String id = new ObjectId().toHexString();

        when(inspectionMethodService.deleteById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete(domain + "/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inspectionMethodService).deleteById(id);
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
