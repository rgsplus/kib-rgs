package nl.rgs.kib.model.list;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "StandardStatus")
public enum InspectionListStatus {
    CONCEPT,
    DEFINITIVE,
    ARCHIVE,
}