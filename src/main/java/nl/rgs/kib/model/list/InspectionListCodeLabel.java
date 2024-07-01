package nl.rgs.kib.model.list;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data()
@AllArgsConstructor()
public class InspectionListCodeLabel {
    @NotNull()
    private Integer index;

    @NotBlank()
    private String name;

    private String group;

    private List<InspectionListCodeLabelFeature> features;
}
