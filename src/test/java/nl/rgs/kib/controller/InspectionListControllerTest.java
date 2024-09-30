package nl.rgs.kib.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.InspectionListStatus;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import nl.rgs.kib.service.ApiAccountService;
import nl.rgs.kib.service.InspectionListService;
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

@WebMvcTest(InspectionListController.class)
public class InspectionListControllerTest {

    private final static String domain = "/inspection-list";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InspectionListService inspectionListService;

    @MockBean
    private ApiAccountService apiAccountService;
    
    @Test
    public void findAll_WithoutAuthentication_Returns401() throws Exception {
        mockMvc.perform(get(domain)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void findById_WhenExists_Returns200() throws Exception {
        String id = new ObjectId().toHexString();
        InspectionList inspectionList = new InspectionList();
        inspectionList.setId(id);
        when(inspectionListService.findById(id)).thenReturn(Optional.of(inspectionList));

        mockMvc.perform(get(domain + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(inspectionListService).findById(id);
    }

    @Test
    @WithMockUser
    public void findById_WhenNotExists_Returns404() throws Exception {
        String id = new ObjectId().toHexString();
        when(inspectionListService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(domain + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inspectionListService).findById(id);
    }

    @Test
    public void findById_WithoutAuthentication_Returns401() throws Exception {
        String id = new ObjectId().toHexString();
        mockMvc.perform(get(domain + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void create_Returns201() throws Exception {
        CreateInspectionList createInspectionList = new CreateInspectionList(
                "test",
                InspectionListStatus.CONCEPT,
                List.of()
        );

        InspectionList inspectionList = new InspectionList();
        inspectionList.setId(new ObjectId().toHexString());
        inspectionList.setName(createInspectionList.name());
        inspectionList.setStatus(createInspectionList.status());
        inspectionList.setItems(createInspectionList.items());

        when(inspectionListService.create(createInspectionList)).thenReturn(inspectionList);

        mockMvc.perform(post(domain)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInspectionList)))
                .andExpect(status().isCreated());

        verify(inspectionListService).create(createInspectionList);
    }

    @Test
    @WithMockUser
    public void create_WhenInvalid_Returns400() throws Exception {
        CreateInspectionList createInspectionList = new CreateInspectionList(
                null,
                InspectionListStatus.CONCEPT,
                List.of()
        );

        mockMvc.perform(post(domain)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInspectionList)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_WithoutAuthentication_Returns401() throws Exception {
        CreateInspectionList createInspectionList = new CreateInspectionList(
                "test",
                InspectionListStatus.CONCEPT,
                List.of()
        );

        mockMvc.perform(post(domain)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createInspectionList)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void update_WhenExists_Returns200() throws Exception {
        InspectionList inspectionList = new InspectionList();
        inspectionList.setId(new ObjectId().toHexString());
        inspectionList.setName("test");
        inspectionList.setStatus(InspectionListStatus.CONCEPT);
        inspectionList.setItems(List.of());

        when(inspectionListService.update(inspectionList)).thenReturn(Optional.of(inspectionList));

        mockMvc.perform(put(domain + "/" + inspectionList.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inspectionList)))
                .andExpect(status().isOk());

        verify(inspectionListService).update(inspectionList);
    }

    @Test
    @WithMockUser
    public void update_WhenInvalid_Returns400() throws Exception {
        InspectionList inspectionList = new InspectionList();
        inspectionList.setId(new ObjectId().toHexString());
        inspectionList.setName(null);
        inspectionList.setStatus(InspectionListStatus.CONCEPT);
        inspectionList.setItems(List.of());

        mockMvc.perform(put(domain + "/" + inspectionList.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inspectionList)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void update_WhenNotExists_Returns404() throws Exception {
        InspectionList inspectionList = new InspectionList();
        inspectionList.setId(new ObjectId().toHexString());
        inspectionList.setName("test");
        inspectionList.setStatus(InspectionListStatus.CONCEPT);
        inspectionList.setItems(List.of());

        when(inspectionListService.update(inspectionList)).thenReturn(Optional.empty());

        mockMvc.perform(put(domain + "/" + inspectionList.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inspectionList)))
                .andExpect(status().isNotFound());

        verify(inspectionListService).update(inspectionList);
    }

    @Test
    public void update_WithoutAuthentication_Returns401() throws Exception {
        InspectionList inspectionList = new InspectionList();
        inspectionList.setId(new ObjectId().toHexString());
        inspectionList.setName("test");
        inspectionList.setStatus(InspectionListStatus.CONCEPT);
        inspectionList.setItems(List.of());

        mockMvc.perform(put(domain + "/" + inspectionList.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inspectionList)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void deleteById_WhenExists_Returns204() throws Exception {
        InspectionList inspectionList = new InspectionList();
        inspectionList.setId(new ObjectId().toHexString());

        when(inspectionListService.deleteById(inspectionList.getId())).thenReturn(Optional.of(inspectionList));

        mockMvc.perform(delete(domain + "/" + inspectionList.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(inspectionListService).deleteById(inspectionList.getId());
    }

    @Test
    @WithMockUser
    public void deleteById_WhenNotExists_Returns404() throws Exception {
        String id = new ObjectId().toHexString();

        when(inspectionListService.deleteById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete(domain + "/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inspectionListService).deleteById(id);
    }

    @Test
    public void deleteById_WithoutAuthentication_Returns401() throws Exception {
        String id = new ObjectId().toHexString();

        mockMvc.perform(delete(domain + "/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void copy_WhenExists_Returns200() throws Exception {
        String id = new ObjectId().toHexString();
        InspectionList inspectionList = new InspectionList();
        inspectionList.setId(id);

        when(inspectionListService.copy(id)).thenReturn(Optional.of(inspectionList));

        mockMvc.perform(post(domain + "/copy/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(inspectionListService).copy(id);
    }

    @Test
    @WithMockUser
    public void copy_WhenNotExists_Returns404() throws Exception {
        String id = new ObjectId().toHexString();

        when(inspectionListService.copy(id)).thenReturn(Optional.empty());

        mockMvc.perform(post(domain + "/copy/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inspectionListService).copy(id);
    }

    @Test
    public void copy_WithoutAuthentication_Returns401() throws Exception {
        String id = new ObjectId().toHexString();

        mockMvc.perform(post(domain + "/copy/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void copyItem_WhenExists_Returns200() throws Exception {
        String id = new ObjectId().toHexString();
        String itemId = "itemId";
        InspectionList inspectionList = new InspectionList();
        inspectionList.setId(id);

        when(inspectionListService.copyItem(id, itemId)).thenReturn(Optional.of(inspectionList));

        mockMvc.perform(put(domain + "/copy/" + id + "/item/" + itemId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(inspectionListService).copyItem(id, itemId);
    }

    @Test
    @WithMockUser
    public void copyItem_WhenNotExists_Returns404() throws Exception {
        String id = new ObjectId().toHexString();
        String itemId = "itemId";

        when(inspectionListService.copyItem(id, itemId)).thenReturn(Optional.empty());

        mockMvc.perform(put(domain + "/copy/" + id + "/item/" + itemId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(inspectionListService).copyItem(id, itemId);
    }

    @Test
    public void copyItem_WithoutAuthentication_Returns401() throws Exception {
        String id = new ObjectId().toHexString();
        String itemId = "itemId";

        mockMvc.perform(put(domain + "/copy/" + id + "/item/" + itemId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}