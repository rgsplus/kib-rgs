package nl.rgs.kib.service;

import com.mongodb.client.gridfs.GridFSFindIterable;
import nl.rgs.kib.model.file.KibFile;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface KibFileService {
    KibFile create(MultipartFile file, String collection, ObjectId objectId) throws IOException;

    Optional<KibFile> findById(ObjectId id);

    Optional<KibFile> deleteById(ObjectId id);

    List<KibFile> deleteByIds(List<ObjectId> ids);

    KibFile copyById(ObjectId id);

    GridFSFindIterable findAll();
}
