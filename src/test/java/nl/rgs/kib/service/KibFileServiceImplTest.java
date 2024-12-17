package nl.rgs.kib.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import nl.rgs.kib.model.file.KibFile;
import nl.rgs.kib.model.file.KibFileResolution;
import nl.rgs.kib.service.impl.KibFileServiceImpl;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KibFileServiceImplTest {

    @Mock
    private GridFsTemplate template;

    @Mock
    private GridFsOperations operations;

    @InjectMocks
    private KibFileServiceImpl kibFileService;

    public static MultipartFile getMultipartFileFromResource(String resourcePath) {
        Resource resource = new ClassPathResource(resourcePath);

        try {
            return new MockMultipartFile(
                    Objects.requireNonNull(resource.getFilename()),
                    resource.getFilename(),
                    Files.probeContentType(resource.getFile().toPath()),
                    resource.getInputStream()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void create_Success() throws IOException {
        MultipartFile multipartFile = getMultipartFileFromResource("file-test.png");
        ObjectId resultId = ObjectId.get();
        String collection = "collection_name_example";
        String objectId = ObjectId.get().toHexString();

        Document metadata = new Document();
        metadata.put("_contentType", multipartFile.getContentType());
        metadata.put("collection", collection);
        metadata.put("objectId", objectId);

        GridFSFile gridFSFile = mock(GridFSFile.class);
        when(gridFSFile.getObjectId()).thenReturn(resultId);
        when(gridFSFile.getFilename()).thenReturn(multipartFile.getOriginalFilename());
        when(gridFSFile.getMetadata()).thenReturn(metadata);

        when(template.store(any(InputStream.class), anyString(), anyString(), any(BasicDBObject.class))).thenReturn(resultId);
        when(operations.findOne(any())).thenReturn(gridFSFile);

        GridFsResource gridFSResource = mock(GridFsResource.class);
        when(gridFSResource.getInputStream()).thenReturn(multipartFile.getInputStream());
        when(operations.getResource(gridFSFile)).thenReturn(gridFSResource);

        KibFile kibFile = kibFileService.create(multipartFile, collection, objectId);

        assertEquals(resultId.toHexString(), kibFile.getId());
        assertEquals(multipartFile.getOriginalFilename(), kibFile.getName());
        assertEquals(multipartFile.getContentType(), kibFile.getType());
        assertEquals(multipartFile.getSize(), kibFile.getData().length);

        verify(template, times(1)).store(any(InputStream.class), anyString(), anyString(), any(BasicDBObject.class));
        verify(operations, times(1)).findOne(any());
        verify(operations, times(1)).getResource(gridFSFile);
    }


    @Test
    public void findById_Success() throws IOException {
        MultipartFile multipartFile = getMultipartFileFromResource("file-test.png");
        String id = ObjectId.get().toHexString();
        String collection = "collection_name_example";
        String objectId = ObjectId.get().toHexString();

        Document metadata = new Document();
        metadata.put("_contentType", multipartFile.getContentType());
        metadata.put("collection", collection);
        metadata.put("objectId", objectId);

        GridFSFile gridFSFile = mock(GridFSFile.class);
        when(gridFSFile.getObjectId()).thenReturn(new ObjectId(id));
        when(gridFSFile.getFilename()).thenReturn(multipartFile.getOriginalFilename());
        when(gridFSFile.getMetadata()).thenReturn(metadata);

        when(operations.findOne(any())).thenReturn(gridFSFile);

        GridFsResource gridFSResource = mock(GridFsResource.class);
        when(gridFSResource.getInputStream()).thenReturn(multipartFile.getInputStream());
        when(operations.getResource(gridFSFile)).thenReturn(gridFSResource);

        Optional<KibFile> kibFile = kibFileService.findById(id);

        assertTrue(kibFile.isPresent());
        assertEquals(id, kibFile.get().getId());
        assertEquals(multipartFile.getOriginalFilename(), kibFile.get().getName());
        assertEquals(multipartFile.getContentType(), kibFile.get().getType());
        assertEquals(multipartFile.getSize(), kibFile.get().getData().length);
    }

    @Test
    public void findById_NotFound() {
        String id = ObjectId.get().toHexString();

        when(operations.findOne(any())).thenReturn(null);

        Optional<KibFile> kibFile = kibFileService.findById(id);

        assertTrue(kibFile.isEmpty());
    }

    @Test
    public void findByIdAndResolution_Null_Success() throws IOException {
        MultipartFile multipartFile = getMultipartFileFromResource("file-test.png");
        String id = ObjectId.get().toHexString();
        String collection = "collection_name_example";
        String objectId = ObjectId.get().toHexString();

        Document metadata = new Document();
        metadata.put("_contentType", multipartFile.getContentType());
        metadata.put("collection", collection);
        metadata.put("objectId", objectId);

        GridFSFile gridFSFile = mock(GridFSFile.class);
        when(gridFSFile.getObjectId()).thenReturn(new ObjectId(id));
        when(gridFSFile.getFilename()).thenReturn(multipartFile.getOriginalFilename());
        when(gridFSFile.getMetadata()).thenReturn(metadata);

        when(operations.findOne(any())).thenReturn(gridFSFile);

        GridFsResource gridFSResource = mock(GridFsResource.class);
        when(gridFSResource.getInputStream()).thenReturn(multipartFile.getInputStream());
        when(operations.getResource(gridFSFile)).thenReturn(gridFSResource);

        Optional<KibFile> kibFile = kibFileService.findByIdAndResolution(id, null);

        assertTrue(kibFile.isPresent());
        assertEquals(id, kibFile.get().getId());
        assertEquals(multipartFile.getOriginalFilename(), kibFile.get().getName());
        assertEquals(multipartFile.getContentType(), kibFile.get().getType());
        assertEquals(multipartFile.getSize(), kibFile.get().getData().length);

        verify(operations, times(1)).getResource(gridFSFile);
    }

    @Test
    public void findByIdAndResolution_Thumbnail_Success() throws IOException {
        MultipartFile multipartFile = getMultipartFileFromResource("file-test.png");
        String id = ObjectId.get().toHexString();
        String collection = "collection_name_example";
        String objectId = ObjectId.get().toHexString();

        Document metadata = new Document();
        metadata.put("_contentType", multipartFile.getContentType());
        metadata.put("collection", collection);
        metadata.put("objectId", objectId);

        GridFSFile gridFSFile = mock(GridFSFile.class);
        when(gridFSFile.getObjectId()).thenReturn(new ObjectId(id));
        when(gridFSFile.getFilename()).thenReturn(multipartFile.getOriginalFilename());
        when(gridFSFile.getMetadata()).thenReturn(metadata);

        when(operations.findOne(any())).thenReturn(gridFSFile);

        GridFsResource gridFSResource = mock(GridFsResource.class);
        when(gridFSResource.getInputStream()).thenReturn(multipartFile.getInputStream());
        when(operations.getResource(gridFSFile)).thenReturn(gridFSResource);

        Optional<KibFile> kibFile = kibFileService.findByIdAndResolution(id, KibFileResolution.THUMBNAIL);

        assertTrue(kibFile.isPresent());
        assertEquals(id, kibFile.get().getId());
        assertEquals(multipartFile.getOriginalFilename(), kibFile.get().getName());
        assertEquals(multipartFile.getContentType(), kibFile.get().getType());
        assertTrue(kibFile.get().getData().length < multipartFile.getSize());

        verify(operations, times(1)).getResource(gridFSFile);
    }

    @Test
    public void findAll_Success() {
        GridFSFindIterable gridFSFindIterable = mock(GridFSFindIterable.class);

        when(operations.find(any())).thenReturn(gridFSFindIterable);

        GridFSFindIterable result = kibFileService.findAll();

        assertEquals(gridFSFindIterable, result);

        verify(operations).find(any());
    }

    @Test
    public void deleteById_Success() throws IOException {
        MultipartFile multipartFile = getMultipartFileFromResource("file-test.png");
        String id = ObjectId.get().toHexString();
        String collection = "collection_name_example";
        String objectId = ObjectId.get().toHexString();

        Document metadata = new Document();
        metadata.put("_contentType", multipartFile.getContentType());
        metadata.put("collection", collection);
        metadata.put("objectId", objectId);

        GridFSFile gridFSFile = mock(GridFSFile.class);
        when(gridFSFile.getObjectId()).thenReturn(new ObjectId(id));
        when(gridFSFile.getFilename()).thenReturn(multipartFile.getOriginalFilename());
        when(gridFSFile.getMetadata()).thenReturn(metadata);

        when(operations.findOne(any())).thenReturn(gridFSFile);

        GridFsResource gridFSResource = mock(GridFsResource.class);
        when(gridFSResource.getInputStream()).thenReturn(multipartFile.getInputStream());
        when(operations.getResource(gridFSFile)).thenReturn(gridFSResource);

        Optional<KibFile> kibFile = kibFileService.deleteById(id);

        assertTrue(kibFile.isPresent());
        assertEquals(id, kibFile.get().getId());
        assertEquals(multipartFile.getOriginalFilename(), kibFile.get().getName());
        assertEquals(multipartFile.getContentType(), kibFile.get().getType());
        assertEquals(multipartFile.getSize(), kibFile.get().getData().length);

        verify(operations, times(1)).getResource(gridFSFile);
        verify(operations, times(1)).delete(any());
    }

    @Test
    public void deleteById_NotFound() {
        String id = ObjectId.get().toHexString();

        when(operations.findOne(any())).thenReturn(null);

        Optional<KibFile> kibFile = kibFileService.deleteById(id);

        assertTrue(kibFile.isEmpty());

        verify(operations, never()).getResource(anyString());
        verify(operations, never()).delete(any());
    }

    @Test
    public void deleteByIds_Success() throws IOException {
        MultipartFile multipartFile = getMultipartFileFromResource("file-test.png");
        String id1 = ObjectId.get().toHexString();
        String id2 = ObjectId.get().toHexString();
        String collection = "collection_name_example";
        String objectId = ObjectId.get().toHexString();

        Document metadata = new Document();
        metadata.put("_contentType", multipartFile.getContentType());
        metadata.put("collection", collection);
        metadata.put("objectId", objectId);

        GridFSFile gridFSFile1 = mock(GridFSFile.class);
        when(gridFSFile1.getObjectId()).thenReturn(new ObjectId(id1));
        when(gridFSFile1.getFilename()).thenReturn(multipartFile.getOriginalFilename());
        when(gridFSFile1.getMetadata()).thenReturn(metadata);

        GridFSFile gridFSFile2 = mock(GridFSFile.class);
        when(gridFSFile2.getObjectId()).thenReturn(new ObjectId(id2));
        when(gridFSFile2.getFilename()).thenReturn(multipartFile.getOriginalFilename());
        when(gridFSFile2.getMetadata()).thenReturn(metadata);

        Query query1 = new Query(Criteria.where("_id").is(id1));
        Query query2 = new Query(Criteria.where("_id").is(id2));

        when(operations.findOne(query1)).thenReturn(gridFSFile1);
        when(operations.findOne(query2)).thenReturn(gridFSFile2);

        GridFsResource gridFSResource = mock(GridFsResource.class);
        when(gridFSResource.getInputStream()).thenReturn(multipartFile.getInputStream());
        when(operations.getResource(gridFSFile1)).thenReturn(gridFSResource);
        when(operations.getResource(gridFSFile2)).thenReturn(gridFSResource);

        kibFileService.deleteByIds(Set.of(id1, id2));

        verify(operations, times(2)).delete(any());
    }

    @Test
    public void copyById_Success() throws IOException {
        MultipartFile multipartFile = getMultipartFileFromResource("file-test.png");
        String id = ObjectId.get().toHexString();
        String collection = "collection_name_example";
        String objectId = ObjectId.get().toHexString();

        Document metadata = new Document();
        metadata.put("_contentType", multipartFile.getContentType());
        metadata.put("collection", collection);
        metadata.put("objectId", objectId);

        GridFSFile gridFSFile = mock(GridFSFile.class);
        when(gridFSFile.getObjectId()).thenReturn(new ObjectId(id));
        when(gridFSFile.getFilename()).thenReturn(multipartFile.getOriginalFilename());
        when(gridFSFile.getMetadata()).thenReturn(metadata);

        when(operations.findOne(any())).thenReturn(gridFSFile);

        GridFsResource gridFSResource = mock(GridFsResource.class);
        when(gridFSResource.getInputStream()).thenReturn(multipartFile.getInputStream());
        when(operations.getResource(gridFSFile)).thenReturn(gridFSResource);

        ObjectId resultId = ObjectId.get();
        when(template.store(any(InputStream.class), anyString(), anyString(), any(BasicDBObject.class))).thenReturn(resultId);

        KibFile kibFile = kibFileService.copyById(id);

        assertEquals(multipartFile.getOriginalFilename(), kibFile.getName());
        assertEquals(multipartFile.getContentType(), kibFile.getType());

        verify(operations, times(2)).getResource(gridFSFile);
        verify(template, times(1)).store(any(InputStream.class), anyString(), anyString(), any(BasicDBObject.class));
    }

    @Test
    public void copyById_NotFound() {
        String id = ObjectId.get().toHexString();

        when(operations.findOne(any())).thenReturn(null);

        try {
            kibFileService.copyById(id);
        } catch (RuntimeException e) {
            assertEquals("File not found", e.getMessage());
        }

        verify(operations, never()).getResource(anyString());
        verify(template, never()).store(any(InputStream.class), anyString(), anyString(), any(BasicDBObject.class));
    }

    @Test
    public void resizeImage_OriginalResolution() throws IOException {
        MultipartFile multipartFile = getMultipartFileFromResource("file-test.png");

        KibFile kibFile = new KibFile();
        kibFile.setData(multipartFile.getInputStream().readAllBytes());
        kibFile.setType(multipartFile.getContentType());
        kibFile.setName(multipartFile.getOriginalFilename());
        kibFile.setId(ObjectId.get().toHexString());

        KibFile.resizeImage(kibFile, KibFileResolution.ORIGINAL);

        assertEquals(multipartFile.getSize(), kibFile.getData().length);
    }

    @Test
    public void resizeImage_ThumbnailResolution() throws IOException {
        MultipartFile multipartFile = getMultipartFileFromResource("file-test.png");

        KibFile kibFile = new KibFile();
        kibFile.setData(multipartFile.getInputStream().readAllBytes());
        kibFile.setType(multipartFile.getContentType());
        kibFile.setName(multipartFile.getOriginalFilename());
        kibFile.setId(ObjectId.get().toHexString());

        KibFile.resizeImage(kibFile, KibFileResolution.THUMBNAIL);

        assertTrue(kibFile.getData().length < multipartFile.getSize());
    }

    @Test
    public void resizeImage_NullResolution() throws IOException {
        MultipartFile multipartFile = getMultipartFileFromResource("file-test.png");

        KibFile kibFile = new KibFile();
        kibFile.setData(multipartFile.getInputStream().readAllBytes());
        kibFile.setType(multipartFile.getContentType());
        kibFile.setName(multipartFile.getOriginalFilename());
        kibFile.setId(ObjectId.get().toHexString());

        KibFile.resizeImage(kibFile, null);

        assertEquals(multipartFile.getSize(), kibFile.getData().length);
    }
}
