package nl.rgs.kib.model.list;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.Optional;

@Data()
@NoArgsConstructor()
@AllArgsConstructor()
public class InspectionListItemStageImage {
    private Boolean main;

    @NotNull()
    private ObjectId fileId;

    public String getFileId() {
        return Optional.ofNullable(fileId).map(ObjectId::toHexString).orElse(null);
    }

    public void setObjectId(String id) {
        this.fileId = Optional.ofNullable(id).map(ObjectId::new).orElse(null);
    }
}
