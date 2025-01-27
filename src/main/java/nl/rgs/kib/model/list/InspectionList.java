package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.rgs.kib.shared.models.BaseObject;
import nl.rgs.kib.shared.models.Sortable;
import nl.rgs.kib.shared.validators.UniqueIds;
import nl.rgs.kib.shared.validators.UniqueStandardNos;
import nl.rgs.kib.shared.validators.ValidIndexes;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "inspection_list")
public class InspectionList extends BaseObject implements Sortable {
    @Id
    @NotBlank
    @Schema(example = "5f622c23a8efb61a54365f33")
    private String id;

    @NotBlank
    @Schema(example = "RGS+ NEN_2767")
    private String name;

    @NotNull
    @Schema(example = "DEFINITIVE")
    private InspectionListStatus status;

    @Valid
    @NotNull
    @UniqueIds
    @ValidIndexes
    @UniqueStandardNos
    private List<InspectionListItem> items = new ArrayList<>();

    /**
     * Returns the file IDs of the images that are deleted from the updated list.
     * <ul>
     *     <li>The file IDs are extracted from the images of the stages of the items in the existing list.</li>
     *     <li>The file IDs are compared with the file IDs of the images of the stages of the items in the updated list.</li>
     *     <li>The file IDs that are not present in the updated list are returned.</li>
     * </ul>
     *
     * @param existingList the existing inspection list
     * @param updatedList  the updated inspection list
     * @return the set of file IDs of the deleted images
     * @see InspectionList
     * @see InspectionListItem
     * @see InspectionListItemStage
     * @see InspectionListItemStageImage
     */
    public static Set<String> getDeletedFileIds(InspectionList existingList, InspectionList updatedList) {
        return existingList.getItems().stream()
                .flatMap(item -> item.getStages().stream())
                .flatMap(stage -> stage.getImages().stream())
                .filter(existingImage -> existingImage.getFileId() != null && updatedList.getItems().stream()
                        .flatMap(item -> item.getStages().stream())
                        .flatMap(stage -> stage.getImages().stream())
                        .noneMatch(image -> image.getFileId().equals(existingImage.getFileId())))
                .map(InspectionListItemStageImage::getFileId)
                .collect(Collectors.toSet());
    }

    /**
     * Returns all file IDs of the images in the inspection list.
     *
     * @param list the inspection list
     * @return the set of file IDs of the images
     * @see InspectionList
     * @see InspectionListItem
     * @see InspectionListItemStage
     * @see InspectionListItemStageImage
     */
    public static Set<String> getAllFileIds(InspectionList list) {
        return list.getItems().stream()
                .flatMap(item -> item.getStages().stream())
                .flatMap(stage -> stage.getImages().stream())
                .map(InspectionListItemStageImage::getFileId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * Sort all the properties that implement Comparable class.
     *
     * <ul>
     *     <li>Sort the items of the inspection list.</li>
     *     <li>Sort the item stages of the items.</li>
     *     <li>Sort the images of the item stages.</li>
     *     <li>Execute "applySort" method of the inspection method.</li>
     * </ul>
     *
     * @see InspectionList
     * @see InspectionListItem
     * @see InspectionListItemStage
     * @see InspectionListItemStageImage
     */
    @Override
    public void applySort() {
        List<InspectionListItem> mutableItems = new ArrayList<>(this.items);
        mutableItems.sort(null);
        for (InspectionListItem item : mutableItems) {
            item.getInspectionMethod().applySort();

            List<InspectionListItemStage> mutableStages = new ArrayList<>(item.getStages());
            mutableStages.sort(null);
            for (InspectionListItemStage stage : mutableStages) {
                List<InspectionListItemStageImage> mutableImages = new ArrayList<>(stage.getImages());
                mutableImages.sort(null);
                stage.setImages(mutableImages);
            }
            item.setStages(mutableStages);
        }
        this.items = mutableItems;
    }
}
