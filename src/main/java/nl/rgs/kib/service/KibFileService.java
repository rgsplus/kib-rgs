package nl.rgs.kib.service;

import com.mongodb.client.gridfs.GridFSFindIterable;
import nl.rgs.kib.model.file.KibFile;
import nl.rgs.kib.model.file.KibFileResolution;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface KibFileService {
    KibFile create(MultipartFile file, String collection, String objectId) throws IOException;

    Optional<KibFile> findById(String id);

    Optional<KibFile> findByIdAndResolution(String id, KibFileResolution resolution);

    Optional<KibFile> deleteById(String id);

    List<KibFile> deleteByIds(List<String> ids);

    KibFile copyById(String id);

    GridFSFindIterable findAll();
}
