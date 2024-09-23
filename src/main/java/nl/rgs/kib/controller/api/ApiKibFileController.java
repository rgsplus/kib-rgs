package nl.rgs.kib.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.rgs.kib.model.file.KibFile;
import nl.rgs.kib.service.KibFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController()
@RequestMapping("/api/kib-file")
@Tag(name = "Kib File")
public class ApiKibFileController {
    @Autowired
    private KibFileService kibFileService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Find a kib file by id",
            description = "Find a kib file by id",
            parameters = {
                    @Parameter(
                            name = "api-key",
                            description = "API Key",
                            required = true,
                            in = ParameterIn.HEADER,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found the kib file"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Missing API Key",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid API Key || Expired API Key || Inactive API Key",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kib file not found",
                            content = @Content()
                    ),
            }
    )
    public ResponseEntity<KibFile> findById(@PathVariable() String id) throws IOException {
        return kibFileService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
