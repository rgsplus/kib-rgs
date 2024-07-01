package nl.rgs.kib.model.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data()
@AllArgsConstructor()
public class InspectionItemCodeStage {
    private Integer stage;
    private String name;
    private Double max;
}
