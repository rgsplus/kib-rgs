package nl.rgs.kib.shared.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@AllArgsConstructor()
@NoArgsConstructor()
public class ImportDocument {
    @NotBlank()
    private String document;

    @NotBlank()
    private String name;
}