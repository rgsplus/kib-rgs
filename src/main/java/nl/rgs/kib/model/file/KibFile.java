package nl.rgs.kib.model.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
}
