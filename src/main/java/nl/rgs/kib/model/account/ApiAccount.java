package nl.rgs.kib.model.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import nl.rgs.kib.shared.models.BaseObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Data()
@EqualsAndHashCode(callSuper = true)
@Document(collection = "api_account")
public class ApiAccount extends BaseObject {
    @NotNull()
    @Schema(example = "5f622c23a8efb61a54365f33")
    private ObjectId id;

    @NotNull()
    @Schema(example = "e0e4fe66-5114-4bd8-83f7-28c4bd3461a6")
    private String apiKey;

    @NotBlank()
    @Schema(example = "John Doe", description = "Name of the account holder")
    private String name;

    @NotBlank()
    @Schema(example = "Facebook", description = "Name of the business")
    private String businessName;

    @NotNull()
    @Schema(example = "2021-09-15T00:00:00.000Z", description = "Start date of the account")
    private Date startDate;

    @Schema(example = "2021-09-15T00:00:00.000Z", description = "End date of the account")
    private Date endDate;

    @NotNull()
    @Schema(example = "true")
    private Boolean active;

    public static String generateApiKey() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return Optional.ofNullable(id).map(ObjectId::toHexString).orElse(null);
    }

    public void setId(String id) {
        this.id = Optional.ofNullable(id).map(ObjectId::new).orElse(null);
    }
}