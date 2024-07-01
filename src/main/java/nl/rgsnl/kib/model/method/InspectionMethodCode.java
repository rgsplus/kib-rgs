package nl.rgsnl.kib.model.method;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data()
@Document(collection = "inspection_method_code")
public class InspectionMethodCode {
    @Id()
    private ObjectId id;

    @NotBlank()
    private String name;

    @NotNull()
    private InspectionMethodCodeInput input;

    @NotNull()
    private InspectionMethodCodeCalculationMethod calculationMethod;
}
