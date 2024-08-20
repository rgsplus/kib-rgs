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
    @Schema(example = "93c897cc-1ff0-4a38-8d89-cfe91fa3c66b")
    @Field(name = "id")
    private String id;

    @NotNull()
    @Min(0)
    @Schema(example = "1", minimum = "0")
    private Integer index;

    @Schema(example = "Fundering")
    private String group;

    @Schema(example = "Veiligheid")
    private String theme;

    @Schema(example = "Constructief")
    private String field;

    @NotBlank()
    @Schema(example = "Constructieve staat fundering en gevelmetselwerk")
    private String name;

    @Schema(example = "10B")
    private String standardNo;

    @Schema(example = "Visuele beoordeling fundering door gevel en vloeren")
    private String measuringMethod;

//
//    @NotNull()
//    @Schema(example = "SIGNIFICANT")
//    private InspectionListItemCategory category;

    @DBRef()
    @NotNull()
    private InspectionMethod inspectionMethod;

    @Valid()
    @Size(min = 2, max = 10)
    @NotNull()
    @UniqueStages()
    private List<InspectionListItemStage> stages;
}
