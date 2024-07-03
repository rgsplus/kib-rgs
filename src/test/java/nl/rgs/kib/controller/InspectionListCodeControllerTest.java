package nl.rgs.kib.controller;

import nl.rgs.kib.model.list.InspectionListCode;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.rgs.kib.service.InspectionListCodeService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

@WebMvcTest(InspectionListCodeController.class)
public class InspectionListCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InspectionListCodeService inspectionListCodeService;

    private final static String domain = "/inspection-list-code";

    @Test
    @WithMockUser()
    public void findAll_Returns200() throws Exception {
        mockMvc.perform(get(domain)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(inspectionListCodeService).findAll();
    }

    @Test
    @WithMockUser()
    public void findById_WhenExists_Returns200() throws Exception {
        ObjectId id = new ObjectId();
        InspectionListCode inspectionListCode = new InspectionListCode();
        inspectionListCode.setId(id.toHexString());
        when(inspectionListCodeService.findById(id)).thenReturn(Optional.of(inspectionListCode));

        mockMvc.perform(get(domain + "/" + id.toHexString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(inspectionListCodeService).findById(id);
    }

    @Test
    @WithMockUser()
    public void findById_WhenNotExists_Returns404() throws Exception {
        ObjectId id = new ObjectId();
        when(inspectionListCodeService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(domain + "/" + id.toHexString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inspectionListCodeService).findById(id);
    }
}