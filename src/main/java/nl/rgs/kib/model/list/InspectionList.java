package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.rgs.kib.shared.models.BaseObject;
import nl.rgs.kib.shared.validators.UniqueItemIds;
import nl.rgs.kib.shared.validators.UniqueLabelIds;
import nl.rgs.kib.shared.validators.ValidIndexes;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data()
@EqualsAndHashCode(callSuper = true)
@Document(collection = "inspection_list_code")
public class InspectionList extends BaseObject {
    @Id()
    @NotNull()
    @Schema(example = "5f622c23aeefb61a54365f33")
    private ObjectId id;

    @NotBlank()
    @Schema(example = "RGS+ NEN_2767")
    private String name;

    @NotNull()
    @Schema(example = "DEFINITIVE")
    private InspectionListStatus status;

    @NotNull()
    @UniqueItemIds()
    @ValidIndexes()
    private List<InspectionListItem> items;

    @NotNull()
    @UniqueLabelIds()
    @ValidIndexes()
    private List<InspectionListLabel> labels;

    public String getId() {
        return id.toHexString();
    }

    public void setId(String id) {
        this.id = new ObjectId(id);
    }
}