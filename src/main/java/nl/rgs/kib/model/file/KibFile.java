package nl.rgs.kib.model.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Data
public class KibFile {
    @NotBlank
    @Schema(example = "5f622c23a8efb61a54365f33")
    private String id;

    @NotBlank
    @Schema(example = "photo.jpg", description = "File name")
    private String name;

    @NotBlank
    @Schema(example = "image/jpeg", description = "MIME type")
    private String type;

    @NotNull
    @Schema(description = "File data in bytes")
    private byte[] data;

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
    public static void resizeImage(KibFile kibFile, KibFileResolution resolution) {
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
