package nl.rgs.kib.controller;

import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.service.InspectionMethodService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InspectionMethodController.class)
public class InspectionMethodControllerTest {

    private final static String domain = "/inspection-method";
    
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InspectionMethodService inspectionMethodService;

    @Test
    @WithMockUser()
    public void findAll_Returns200() throws Exception {
        mockMvc.perform(get(domain)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(inspectionMethodService).findAll();
    }

    @Test
    @WithMockUser()
    public void findById_WhenExists_Returns200() throws Exception {
        ObjectId id = new ObjectId();
        InspectionMethod inspectionMethod = new InspectionMethod();
        inspectionMethod.setId(id.toHexString());
        when(inspectionMethodService.findById(id)).thenReturn(Optional.of(inspectionMethod));

        mockMvc.perform(get(domain + "/" + id.toHexString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(inspectionMethodService).findById(id);
    }

    @Test
    @WithMockUser()
    public void findById_WhenNotExists_Returns404() throws Exception {
        ObjectId id = new ObjectId();
        when(inspectionMethodService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(domain + "/" + id.toHexString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inspectionMethodService).findById(id);
    }

    //TODO: Implement tests for count endpoint
    //TODO: Implement tests for create endpoint
    //TODO: Implement tests for update endpoint
    //TODO: Implement tests for delete endpoint
}