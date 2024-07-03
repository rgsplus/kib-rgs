package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data()
@Document(collection = "inspection_list_code")
public class InspectionList {
    @Id()
    @NotNull()
    @Schema(example = "5f622c23aeefb61a54365f33")
    private ObjectId id;

    @NotBlank()
    @Schema(example = "RGS+ NEN_2767")
    private String name;

    @NotNull()
    @Schema(example = "ACTIVE")
    private InspectionListStatus status;

    @NotNull()
    private List<InspectionListItem> items;

    @NotNull()
    private List<InspectionListLabel> labels;

    public String getId() {
        return id.toHexString();
    }

    public void setId(String id) {
        this.id = new ObjectId(id);
    }
}