package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.shared.models.Ideable;
import nl.rgs.kib.shared.models.Indexable;
import nl.rgs.kib.shared.validators.UniqueStages;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data()
@NoArgsConstructor()
@AllArgsConstructor()
public class InspectionListItem implements Indexable, Ideable {
    @NotNull()
    @Min(0)
    @Schema(example = "1", minimum = "0")
    private Integer index;

    @NotNull()
    @Schema(example = "5f622c23a8efb61a54365f33")
    @Field(name = "id")
    private String id;

    @NotBlank()
    @Schema(example = "Roof")
    private String name;

    @Schema(example = "Wooden")
    private String group;

    @NotNull()
    @Schema(example = "SIGNIFICANT")
    private InspectionListItemCategory category;

    @DBRef()
    @NotNull()
    private InspectionMethod inspectionMethod;

    @Valid()
    @Size(min = 2, max = 10)
    @NotNull()
    @UniqueStages()
    private List<InspectionListItemStage> stages;
}
