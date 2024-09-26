package nl.rgs.kib.shared.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportResult<T> {
    private List<ImportResultError> errors = List.of();
    private T result;
}