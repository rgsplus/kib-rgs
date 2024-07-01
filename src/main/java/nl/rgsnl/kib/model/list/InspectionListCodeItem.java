package nl.rgsnl.kib.model.list;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.rgsnl.kib.model.item.InspectionItemCode;

@Data()
@AllArgsConstructor()
public class InspectionListCodeItem {
    @NotNull()
    private Integer index;

    @NotNull()
    private InspectionItemCode inspectionItemCode;
}
