package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.rgs.kib.shared.models.BaseObject;
import nl.rgs.kib.shared.validators.UniqueIds;
import nl.rgs.kib.shared.validators.ValidIndexes;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Data()
@EqualsAndHashCode(callSuper = true)
@Document(collection = "inspection_list")
public class InspectionList extends BaseObject {
    @Id()
    @NotBlank()
    @Schema(example = "5f622c23a8efb61a54365f33")
    private String id;

    @NotBlank()
    @Schema(example = "RGS+ NEN_2767")
    private String name;

    @NotNull()
    @Schema(example = "DEFINITIVE")
    private InspectionListStatus status;

    @Valid()
    @NotNull()
    @UniqueIds()
    @ValidIndexes()
    private List<InspectionListItem> items = List.of();

    public static List<InspectionListItem> sortItemsStagesAndImages(List<InspectionListItem> items) {
        return items.stream()
                .sorted(Comparator.comparing(InspectionListItem::getIndex))
                .peek(item -> item.setStages(item.getStages().stream()
                        .sorted(Comparator.comparing(InspectionListItemStage::getStage))
                        .peek(stage -> stage.setImages(stage.getImages().stream()
                                .sorted(Comparator.comparing(InspectionListItemStageImage::getMain).reversed())
                                .toList()))
                        .toList()))
                .toList();
    }

    public static List<String> getDeletedFileIds(InspectionList existingList, InspectionList updatedList) {
        return existingList.getItems().stream()
                .flatMap(item -> item.getStages().stream())
                .flatMap(stage -> stage.getImages().stream())
                .filter(existingImage -> existingImage.getFileId() != null && updatedList.getItems().stream()
                        .flatMap(item -> item.getStages().stream())
                        .flatMap(stage -> stage.getImages().stream())
                        .noneMatch(image -> image.getFileId().equals(existingImage.getFileId())))
                .map(InspectionListItemStageImage::getFileId)
                .filter(Objects::nonNull)
                .map(String::new)
                .toList();
    }

    public static List<String> getAllFileIds(InspectionList list) {
        return list.getItems().stream()
                .flatMap(item -> item.getStages().stream())
                .flatMap(stage -> stage.getImages().stream())
                .map(InspectionListItemStageImage::getFileId)
                .filter(Objects::nonNull)
                .map(String::new)
                .toList();
    }
}
