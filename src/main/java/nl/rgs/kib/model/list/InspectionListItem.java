package nl.rgs.kib.model.list;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import nl.rgs.kib.shared.models.StandarNoable;
import nl.rgs.kib.shared.validators.UniqueStages;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionListItem implements Indexable, Ideable, StandarNoable, Comparable<InspectionListItem> {

    @NotNull
    @Schema(example = "93c897cc-1ff0-4a38-8d89-cfe91fa3c66b")
    @Field(name = "id")
    private String id;

    @NotNull
    @Min(0)
    @Schema(example = "1", minimum = "0")
    private Integer index;

    @NotNull
    @Schema(example = "[\"Constructie\",\"Fundering\"]")
    private SortedSet<String> groups = new TreeSet<>();

    @Schema(example = "Veiligheid")
    private String theme;

    @Schema(example = "Constructief")
    private String field;

    @NotBlank
    @Schema(example = "Constructieve staat fundering en gevelmetselwerk")
    private String name;

    @NotBlank
    @Schema(example = "10B")
    private String standardNo;

    @Schema(example = "Visuele beoordeling fundering door gevel en vloeren")
    private String measuringMethod;

    @Schema(example = "Deze inspectie is bedoeld om de constructieve staat van de fundering en gevelmetselwerk te beoordelen.")
    private String description;

    @DBRef
    @NotNull
    @JsonIgnoreProperties({"metadata"})
    private InspectionMethod inspectionMethod;

    @Valid
    @Size(min = 2, max = 10)
    @NotNull
    @UniqueStages
    private List<InspectionListItemStage> stages = new ArrayList<>();

    @Override
    public int compareTo(InspectionListItem o) {
        return this.index.compareTo(o.index);
    }
}
