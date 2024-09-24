package nl.rgs.kib.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.model.file.KibFile;
import nl.rgs.kib.model.file.KibFileResolution;
import nl.rgs.kib.service.KibFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/kib-file")
@Tag(name = "Kib File")
public class KibFileController {
    @Autowired
    private KibFileService kibFileService;

    @PreAuthorize("hasRole('ROLE_KIB_USER') or hasRole('ROLE_KIB_ADMIN')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Find a kib file by id",
            description = "Find a kib file by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found the kib file"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kib file not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<KibFile> findById(@PathVariable String id, @RequestParam(required = false) KibFileResolution resolution) {
        return kibFileService.findByIdAndResolution(id, resolution)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @PostMapping
    @Operation(
            summary = "Create a kib file",
            description = "Create a kib file",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created the kib file"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<KibFile> create(
            @RequestParam("file") @NotNull MultipartFile file,
            @RequestParam("collection") @NotBlank String collection,
            @RequestParam("objectId") @NotBlank String objectId
    ) throws IOException {
        return ResponseEntity.status(201).body(kibFileService.create(file, collection, objectId));
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a kib file by id",
            description = "Delete a kib file by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted the kib file",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Kib file not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return kibFileService.deleteById(id)
                .map(kibFile -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}
