package nl.rgs.kib.model.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Optional;

@Data()
public class KibFile {

    @NotNull()
    @Schema(example = "5f622c23a8efb61a54365f33")
    private ObjectId id;

    @NotBlank()
    @Schema(example = "photo.jpg", description = "File name")
    private String name;

    @NotBlank()
    @Schema(example = "image/jpeg", description = "MIME type")
    private String type;

    @NotNull()
    @Schema(description = "File data in bytes")
    private byte[] data;
    
    public String getId() {
        return Optional.ofNullable(id).map(ObjectId::toHexString).orElse(null);
    }

    public void setId(String id) {
        this.id = Optional.ofNullable(id).map(ObjectId::new).orElse(null);
    }
}
