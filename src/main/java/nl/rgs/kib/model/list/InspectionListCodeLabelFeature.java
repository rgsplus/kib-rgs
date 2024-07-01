package nl.rgs.kib.model.list;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data()
@AllArgsConstructor()
public class InspectionListCodeLabelFeature {
    @NotNull()
    private Integer index;

    @NotBlank()
    private String name;
}
