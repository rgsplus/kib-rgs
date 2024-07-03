package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.shared.validators.ValidStages;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data()
@NoArgsConstructor()
@AllArgsConstructor()
public class InspectionListItemValue {
    @NotNull()
    @Schema(example = "5f622c23aeefb61a54365f33")
    @Field(name = "id")
    private String id;

    @NotBlank()
    @Schema(example = "Roof")
    private String name;

    @Schema(example = "Wooden")
    private String group;

    @NotNull()
    @Schema(example = "SERIOUSLY")
    private InspectionListItemValueCategory category;

    @DBRef()
    @NotNull()
    private InspectionMethod inspectionMethod;

    @NotNull()
    @ValidStages()
    private List<InspectionListItemValueStage> stages;
}
