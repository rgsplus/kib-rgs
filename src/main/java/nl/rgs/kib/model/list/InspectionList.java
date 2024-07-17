package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.rgs.kib.shared.models.BaseObject;
import nl.rgs.kib.shared.validators.UniqueIds;
import nl.rgs.kib.shared.validators.UniqueIndexes;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Data()
@EqualsAndHashCode(callSuper = true)
@Document(collection = "inspection_list")
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

    @Valid()
    @NotNull()
    @UniqueIds()
    @UniqueIndexes()
    private List<InspectionListItem> items;

    @Valid()
    @NotNull()
    @UniqueIds()
    @UniqueIndexes()
    private List<InspectionListLabel> labels;

    public static List<InspectionListItem> sortItemsAndStages(List<InspectionListItem> items) {
        return items.stream()
                .sorted(Comparator.comparing(InspectionListItem::getIndex))
                .peek(item -> item.setStages(item.getStages().stream()
                        .sorted(Comparator.comparing(InspectionListItemStage::getStage))
                        .toList()))
                .toList();
    }

    public static List<InspectionListLabel> sortLabelsAndFeatures(List<InspectionListLabel> labels) {
        return labels.stream()
                .sorted(Comparator.comparing(InspectionListLabel::getIndex))
                .peek(label -> label.setFeatures(label.getFeatures().stream()
                        .sorted(Comparator.comparing(InspectionListLabelFeature::getIndex))
                        .toList()))
                .toList();
    }

    public String getId() {
        return Optional.ofNullable(id).map(ObjectId::toHexString).orElse(null);
    }

    public void setId(String id) {
        this.id = Optional.ofNullable(id).map(ObjectId::new).orElse(null);
    }
}