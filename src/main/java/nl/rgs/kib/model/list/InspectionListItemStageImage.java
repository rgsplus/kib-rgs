package nl.rgs.kib.model.list;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@NoArgsConstructor()
@AllArgsConstructor()
public class InspectionListItemStageImage {
    private Boolean main;

    @NotNull()
    private String fileId;
}
