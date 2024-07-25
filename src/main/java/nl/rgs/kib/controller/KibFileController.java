package nl.rgs.kib.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.rgs.kib.model.file.KibFile;
import nl.rgs.kib.service.KibFileService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController()
@RequestMapping("/kib-file")
@Tag(name = "Kib File")
public class KibFileController {
    @Autowired
    private KibFileService kibFileService;

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the kib file"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Kib file not found"
            ),
    })
    public ResponseEntity<KibFile> findById(@PathVariable() String id) throws IOException {
        return kibFileService.findById(new ObjectId(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created the kib file"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
    })
    public ResponseEntity<KibFile> create(
            @RequestParam("file") @NotNull() MultipartFile file,
            @RequestParam("collection") @NotBlank() String collection,
            @RequestParam("objectId") @NotBlank() String objectId
    ) throws IOException {
        return ResponseEntity.status(201).body(kibFileService.create(file, collection, new ObjectId(objectId)));
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Deleted the kib file"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Kib file not found"
            ),
    })
    public ResponseEntity<Void> deleteById(@PathVariable() String id) {
        return kibFileService.deleteById(new ObjectId(id))
                .map(kibFile -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}
