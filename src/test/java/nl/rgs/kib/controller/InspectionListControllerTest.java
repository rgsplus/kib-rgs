package nl.rgs.kib.controller;

import nl.rgs.kib.model.list.InspectionList;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.rgs.kib.service.InspectionListService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

@WebMvcTest(InspectionListController.class)
public class InspectionListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InspectionListService inspectionListService;

    private final static String domain = "/inspection-list";

    @Test
    @WithMockUser()
    public void findAll_Returns200() throws Exception {
        mockMvc.perform(get(domain)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(inspectionListService).findAll();
    }

    @Test
    @WithMockUser()
    public void findById_WhenExists_Returns200() throws Exception {
        ObjectId id = new ObjectId();
        InspectionList inspectionList = new InspectionList();
        inspectionList.setId(id.toHexString());
        when(inspectionListService.findById(id)).thenReturn(Optional.of(inspectionList));

        mockMvc.perform(get(domain + "/" + id.toHexString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(inspectionListService).findById(id);
    }

    @Test
    @WithMockUser()
    public void findById_WhenNotExists_Returns404() throws Exception {
        ObjectId id = new ObjectId();
        when(inspectionListService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(domain + "/" + id.toHexString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inspectionListService).findById(id);
    }
}