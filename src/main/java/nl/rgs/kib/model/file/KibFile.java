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
     * Resizes the image data within the provided {@link KibFile} object to a specified resolution,
     * maintaining the original aspect ratio. The resizing is done in place, modifying the {@code data}
     * field of the {@code kibFile} object.
     *
     * <p>Supported image MIME types for resizing:</p>
     * <ul>
     *   <li>image/jpeg</li>
     *   <li>image/jpg</li>
     *   <li>image/png</li>
     *   <li>image/gif</li>
     * </ul>
     *
     * <p>Available target resolutions (width in pixels):</p>
     * <ul>
     *   <li>{@link KibFileResolution#THUMBNAIL}: 100px</li>
     *   <li>{@link KibFileResolution#MEDIUM}: 500px</li>
     *   <li>{@link KibFileResolution#LARGE}: 1000px</li>
     *   <li>{@link KibFileResolution#ORIGINAL}: No resizing is performed.</li>
     * </ul>
     *
     * <p>Resizing is skipped if:</p>
     * <ul>
     *     <li>The provided {@code kibFile} is null.</li>
     *     <li>The {@code kibFile}'s MIME type is not among the supported types.</li>
     *     <li>The specified {@code resolution} is null or {@link KibFileResolution#ORIGINAL}.</li>
     * </ul>
     *
     * <p>Note: The output format after resizing is always JPEG, regardless of the original format.</p>
     *
     * @param kibFile    The {@link KibFile} object containing the image data to resize. Its {@code data} field will be updated.
     *                   If the file is not a supported image type, it remains unchanged.
     * @param resolution The target {@link KibFileResolution}. If null or {@code ORIGINAL}, the image data remains unchanged.
     * @throws RuntimeException if an {@link IOException} occurs during image reading or writing.
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
