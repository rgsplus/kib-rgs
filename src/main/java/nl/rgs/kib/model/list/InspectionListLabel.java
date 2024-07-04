package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rgs.kib.shared.models.Indexable;
import nl.rgs.kib.shared.validators.ValidIndexes;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
public class InspectionListLabel implements Indexable {
    @NotNull()
    @Schema(example = "5f622c23aeefb61a54365f33")
    @Field(name = "id")
    private String id;

    @NotNull()
    @Min(0)
    @Schema(example = "1", minimum = "0")
    private Integer index;

    @NotBlank()
    @Schema(example = "Construction year")
    private String name;

    @Schema(example = "General")
    private String group;

    @NotNull()
    @ValidIndexes()
    @Valid()
    private List<InspectionListLabelFeature> features;
}
