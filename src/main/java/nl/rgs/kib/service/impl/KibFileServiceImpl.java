package nl.rgs.kib.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import nl.rgs.kib.model.file.KibFile;
import nl.rgs.kib.service.KibFileService;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class KibFileServiceImpl implements KibFileService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    @Override
    public KibFile create(MultipartFile file, String collection, ObjectId objectId) throws IOException {
        BasicDBObject metadata = new BasicDBObject();
        metadata.put("collection", collection);
        metadata.put("objectId", objectId.toHexString());
        
        ObjectId id = template.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metadata);

        return this.findById(id).orElse(null);
    }

    @Override
    public Optional<KibFile> findById(ObjectId id) {
        GridFSFile gridFSFile = operations.findOne(new Query(Criteria.where("_id").is(id)));

        return Optional.ofNullable(gridFSFile).map(file -> {
            KibFile result = new KibFile();
            result.setId(file.getObjectId().toHexString());
            result.setName(file.getFilename());

            if (file.getMetadata() != null) {
                result.setType(file.getMetadata().getString("_contentType"));
            }

            try {
                result.setData(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return result;
        });
    }

    @Override
    public Optional<KibFile> deleteById(ObjectId id) {
        GridFSFile gridFSFile = operations.findOne(new Query(Criteria.where("_id").is(id)));
        if (gridFSFile != null) {
            Optional<KibFile> file = this.findById(id);
            operations.delete(new Query(Criteria.where("_id").is(id)));
            return file;
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public List<KibFile> deleteByIds(List<ObjectId> ids) {
        return ids.stream()
                .map(this::deleteById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
