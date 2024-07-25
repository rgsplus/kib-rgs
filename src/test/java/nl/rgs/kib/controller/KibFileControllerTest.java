package nl.rgs.kib.controller;

import nl.rgs.kib.model.file.KibFile;
import nl.rgs.kib.service.KibFileService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KibFileController.class)
public class KibFileControllerTest {

    private final static String domain = "/kib-file";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KibFileService kibFileService;

    @Test
    @WithMockUser()
    public void findById_WhenExists_Returns200() throws Exception {
        ObjectId id = new ObjectId();
        KibFile kibFile = new KibFile();
        kibFile.setId(id.toHexString());

        when(kibFileService.findById(id)).thenReturn(Optional.of(kibFile));

        mockMvc.perform(get(domain + "/" + id.toHexString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(kibFileService).findById(id);
    }

    @Test
    @WithMockUser()
    public void findById_WhenNotExists_Returns404() throws Exception {
        ObjectId id = new ObjectId();
        when(kibFileService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get(domain + "/" + id.toHexString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(kibFileService).findById(id);
    }

    @Test
    public void findById_WithoutAuthentication_Returns401() throws Exception {
        ObjectId id = new ObjectId();
        mockMvc.perform(get(domain + "/" + id.toHexString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser()
    public void create_Returns201_withJPG() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "photo.jpg", "image/jpeg", "some image".getBytes());
        String collection = "collection";
        String objectId = new ObjectId().toHexString();

        KibFile kibFile = new KibFile();
        kibFile.setId(new ObjectId().toHexString());
        kibFile.setName(file.getOriginalFilename());
        kibFile.setType(file.getContentType());
        kibFile.setData(file.getBytes());

        when(kibFileService.create(file, collection, new ObjectId(objectId))).thenReturn(kibFile);

        mockMvc.perform(multipart(domain).file(file).param("collection", collection).param("objectId", objectId).with(csrf())).andExpect(status().isCreated());

        verify(kibFileService).create(file, collection, new ObjectId(objectId));
    }

    @Test
    @WithMockUser()
    public void create_Returns201_withPNG() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "photo.png", "image/png", "some image".getBytes());
        String collection = "collection";
        String objectId = new ObjectId().toHexString();

        KibFile kibFile = new KibFile();
        kibFile.setId(new ObjectId().toHexString());
        kibFile.setName(file.getOriginalFilename());
        kibFile.setType(file.getContentType());
        kibFile.setData(file.getBytes());

        when(kibFileService.create(file, collection, new ObjectId(objectId))).thenReturn(kibFile);

        mockMvc.perform(multipart(domain).file(file).param("collection", collection).param("objectId", objectId).with(csrf())).andExpect(status().isCreated());

        verify(kibFileService).create(file, collection, new ObjectId(objectId));
    }

    @Test
    @WithMockUser()
    public void create_Returns201_withGIF() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "photo.gif", "image/gif", "some image".getBytes());
        String collection = "collection";
        String objectId = new ObjectId().toHexString();

        KibFile kibFile = new KibFile();
        kibFile.setId(new ObjectId().toHexString());
        kibFile.setName(file.getOriginalFilename());
        kibFile.setType(file.getContentType());
        kibFile.setData(file.getBytes());

        when(kibFileService.create(file, collection, new ObjectId(objectId))).thenReturn(kibFile);

        mockMvc.perform(multipart(domain).file(file).param("collection", collection).param("objectId", objectId).with(csrf())).andExpect(status().isCreated());

        verify(kibFileService).create(file, collection, new ObjectId(objectId));
    }

    @Test
    @WithMockUser()
    public void create_Returns201_withExcel() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "some image".getBytes());
        String collection = "collection";
        String objectId = new ObjectId().toHexString();

        KibFile kibFile = new KibFile();
        kibFile.setId(new ObjectId().toHexString());
        kibFile.setName(file.getOriginalFilename());
        kibFile.setType(file.getContentType());
        kibFile.setData(file.getBytes());

        when(kibFileService.create(file, collection, new ObjectId(objectId))).thenReturn(kibFile);

        mockMvc.perform(multipart(domain).file(file).param("collection", collection).param("objectId", objectId).with(csrf())).andExpect(status().isCreated());

        verify(kibFileService).create(file, collection, new ObjectId(objectId));
    }

    @Test
    @WithMockUser()
    public void create_Returns201_withWord() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "some image".getBytes());
        String collection = "collection";
        String objectId = new ObjectId().toHexString();

        KibFile kibFile = new KibFile();
        kibFile.setId(new ObjectId().toHexString());
        kibFile.setName(file.getOriginalFilename());
        kibFile.setType(file.getContentType());
        kibFile.setData(file.getBytes());

        when(kibFileService.create(file, collection, new ObjectId(objectId))).thenReturn(kibFile);

        mockMvc.perform(multipart(domain).file(file).param("collection", collection).param("objectId", objectId).with(csrf())).andExpect(status().isCreated());

        verify(kibFileService).create(file, collection, new ObjectId(objectId));
    }

    @Test
    @WithMockUser()
    public void create_Returns400_withNullCollection() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "some image".getBytes());
        String collection = null;
        String objectId = new ObjectId().toHexString();

        mockMvc.perform(multipart(domain).file(file).param("collection", collection).param("objectId", objectId).with(csrf())).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser()
    public void create_Returns400_withBlankCollection() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "some image".getBytes());
        String collection = "";
        String objectId = new ObjectId().toHexString();

        mockMvc.perform(multipart(domain).file(file).param("collection", collection).param("objectId", objectId).with(csrf())).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser()
    public void create_Returns400_withNullObjectId() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "some image".getBytes());
        String collection = "collection";
        String objectId = null;

        mockMvc.perform(multipart(domain).file(file).param("collection", collection).param("objectId", objectId).with(csrf())).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser()
    public void create_Returns400_withBlankObjectId() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "file.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "some image".getBytes());
        String collection = "collection";
        String objectId = "";

        mockMvc.perform(multipart(domain).file(file).param("collection", collection).param("objectId", objectId).with(csrf())).andExpect(status().isBadRequest());
    }


    @Test
    public void create_WithoutAuthentication_Returns401() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "photo.jpg", "image/jpeg", "some image".getBytes());

        mockMvc.perform(multipart(domain).file(file).with(csrf())).andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser()
    public void deleteById_WhenExists_Returns204() throws Exception {
        KibFile kibFile = new KibFile();
        kibFile.setId(new ObjectId().toHexString());

        when(kibFileService.deleteById(new ObjectId(kibFile.getId()))).thenReturn(Optional.of(kibFile));

        mockMvc.perform(delete(domain + "/" + kibFile.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(kibFileService).deleteById(new ObjectId(kibFile.getId()));
    }

    @Test
    @WithMockUser()
    public void deleteById_WhenNotExists_Returns404() throws Exception {
        ObjectId id = new ObjectId();

        when(kibFileService.deleteById(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete(domain + "/" + id.toHexString())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(kibFileService).deleteById(id);
    }

    @Test
    public void deleteById_WithoutAuthentication_Returns401() throws Exception {
        ObjectId id = new ObjectId();

        mockMvc.perform(delete(domain + "/" + id.toHexString())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}