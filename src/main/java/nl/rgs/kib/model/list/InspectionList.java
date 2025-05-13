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
     * Calculates the set of image file IDs that are present in the {@code existingList}
     * but not in the {@code updatedList}. This is useful for identifying images
     * that may need to be deleted after an update operation.
     * <p>
     * It iterates through all images in all stages of all items in the {@code existingList}
     * and checks if their file IDs exist in any image within the {@code updatedList}.
     *
     * @param existingList The original inspection list before updates.
     * @param updatedList  The inspection list after updates have been applied.
     * @return A {@link Set} containing the unique file IDs of images present in {@code existingList} but absent in {@code updatedList}.
     *         Returns an empty set if no images were deleted or if {@code existingList} has no images.
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
     * Collects all unique, non-null image file IDs from all items and their stages within the given inspection list.
     *
     * @param list The inspection list from which to extract file IDs.
     * @return A {@link Set} containing all unique, non-null file IDs found in the list's images.
     *         Returns an empty set if the list contains no items or no images with file IDs.
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
     * Sorts the nested elements of this {@code InspectionList} in place based on their natural order (implementation of {@link Comparable}).
     * This method ensures that items, their stages, and the images within those stages are consistently ordered.
     * <p>
     * Specifically, it performs the following sorting actions:
     * <ul>
     *     <li>Sorts the {@code items} list based on {@link InspectionListItem#compareTo}.</li>
     *     <li>For each item, sorts its {@code stages} list based on {@link InspectionListItemStage#compareTo}.</li>
     *     <li>For each stage, sorts its {@code images} list based on {@link InspectionListItemStageImage#compareTo}.</li>
     *     <li>Recursively calls {@link Sortable#applySort()} on the {@code inspectionMethod} of each item.</li>
     * </ul>
     * 
     * @see InspectionListItem#compareTo(InspectionListItem)
     * @see InspectionListItemStage#compareTo(InspectionListItemStage)
     * @see InspectionListItemStageImage#compareTo(InspectionListItemStageImage)
     * @see Sortable#applySort()
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
