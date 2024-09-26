package nl.rgs.kib.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import nl.rgs.kib.model.file.KibFile;
import nl.rgs.kib.model.file.KibFileResolution;
import nl.rgs.kib.service.KibFileService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@EnableScheduling
public class KibFileServiceImpl implements KibFileService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    /**
     * Create a new file.
     * <p>
     * The file will be stored in the database. The metadata will contain the collection and objectId that the file belongs to.
     * </p>
     *
     * @param file       The file to store
     * @param collection The collection that the file belongs to
     *                   (e.g. "users", "inspections", "companies")
     * @param objectId   The id of the object that the file belongs to
     *                   (e.g. the id of the user, inspection, or company)
     * @return The created file
     * @throws IOException If an I/O error occurs
     */
    @Override
    public KibFile create(MultipartFile file, String collection, String objectId) throws IOException {
        BasicDBObject metadata = new BasicDBObject();
        metadata.put("collection", collection);
        metadata.put("objectId", objectId);

        String id = template.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metadata).toHexString();

        return this.findById(id).orElse(null);
    }

    @Override
    public Optional<KibFile> findById(String id) {
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
    public Optional<KibFile> findByIdAndResolution(String id, KibFileResolution resolution) {
        Optional<KibFile> kibFile = this.findById(id);

        kibFile.ifPresent(file -> this.resizeImage(file, resolution));

        return kibFile;
    }

    @Override
    public Optional<KibFile> deleteById(String id) {
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
    public List<KibFile> deleteByIds(List<String> ids) {
        return ids.stream()
                .map(this::deleteById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    /**
     * Copy a file by id.
     * <p>
     * The file will be copied to a new file with a new id.
     * The metadata will contain the id of the original file.
     * </p>
     *
     * @param id The id of the file to copy
     * @return The copied file
     */
    @Override
    public KibFile copyById(String id) {
        GridFSFile gridFSFile = operations.findOne(new Query(Criteria.where("_id").is(id)));

        if (gridFSFile == null) {
            throw new RuntimeException("File not found");
        }

        byte[] data = null;

        try {
            data = IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BasicDBObject metadata = new BasicDBObject();
        metadata.put("copiedFrom", id);

        if (gridFSFile.getMetadata() != null) {
            metadata.putAll(gridFSFile.getMetadata());
        }

        String contentType = null;

        if (metadata.containsField("_contentType")) {
            contentType = metadata.getString("_contentType");
        }


        String newId = template.store(
                new ByteArrayInputStream(data),
                gridFSFile.getFilename(),
                contentType,
                metadata
        ).toHexString();

        return this.findById(newId).orElse(null);
    }

    public GridFSFindIterable findAll() {
        return operations.find(new Query());
    }

    /**
     * Resize an image to the specified resolution.
     * <p>
     * Supported types: image/jpeg, image/jpg, image/png, image/gif
     * </p>
     *
     * <p>
     * Resolutions:
     * <ul>
     *   <li>THUMBNAIL: 100px</li>
     *   <li>MEDIUM: 500px</li>
     *   <li>LARGE: 1000px</li>
     *   <li>ORIGINAL: no resize</li>
     * </ul>
     * </p>
     *
     * @param kibFile    The KibFile. If not an image, or type is not supported, no resize will be done.
     * @param resolution The KibFileResolution. If null or ORIGINAL, no resize will be done.
     */
    private void resizeImage(KibFile kibFile, KibFileResolution resolution) {
        final Set<String> supportedTypes = Set.of("image/jpeg", "image/jpg", "image/png", "image/gif");
        final Map<KibFileResolution, Integer> resolutions = Map.of(
                KibFileResolution.THUMBNAIL, 100,
                KibFileResolution.MEDIUM, 500,
                KibFileResolution.LARGE, 1000
        );

        if (kibFile == null || !supportedTypes.contains(kibFile.getType())) {
            return;
        }

        if (resolution == null || resolution == KibFileResolution.ORIGINAL) {
            return;
        }

        try {
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(kibFile.getData()));
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            int resizedWidth = resolutions.get(resolution);
            int resizedHeight = (int) (originalHeight * ((double) resizedWidth / originalWidth));

            BufferedImage resizedImage = new BufferedImage(resizedWidth, resizedHeight, originalImage.getType());
            resizedImage.createGraphics().drawImage(originalImage.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", baos);
            baos.flush();
            kibFile.setData(baos.toByteArray());
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
