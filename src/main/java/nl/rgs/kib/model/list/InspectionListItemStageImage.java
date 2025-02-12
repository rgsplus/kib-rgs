package nl.rgs.kib.model.list;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionListItemStageImage implements Comparable<InspectionListItemStageImage> {
    private Boolean main;

    @NotNull
    private String fileId;

    @Override
    public int compareTo(InspectionListItemStageImage o) {
        if (this.main && !o.main) {
            return -1;
        } else if (!this.main && o.main) {
            return 1;
        } else {
            return 0;
        }
    }
}
