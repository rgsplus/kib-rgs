package nl.rgs.kib.controller;

import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.model.method.InspectionMethodCode;
import nl.rgs.kib.service.InspectionListCodeService;
import nl.rgs.kib.service.InspectionMethodCodeService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InspectionMethodCodeController.class)
public class InspectionMethodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InspectionMethodCodeService inspectionMethodCodeService;

    private final static String domain = "/inspection-method-code";

    @Test
    @WithMockUser()
    public void findAll_Returns200() throws Exception {
        mockMvc.perform(get(domain)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(inspectionMethodCodeService).findAll();
    }

    @Test
    @WithMockUser()
    public void findById_WhenExists_Returns200() throws Exception {
        ObjectId id = new ObjectId();
        InspectionMethodCode inspectionMethodCode = new InspectionMethodCode();
        inspectionMethodCode.setId(id.toHexString());
        when(inspectionMethodCodeService.findById(id)).thenReturn(Optional.of(inspectionMethodCode));

        mockMvc.perform(get(domain + "/" + id.toHexString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(inspectionMethodCodeService).findById(id);
    }

    @Test
    @WithMockUser()
    public void findById_WhenNotExists_Returns404() throws Exception {
        ObjectId id = new ObjectId();
        when(inspectionMethodCodeService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(domain + "/" + id.toHexString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inspectionMethodCodeService).findById(id);
    }
}