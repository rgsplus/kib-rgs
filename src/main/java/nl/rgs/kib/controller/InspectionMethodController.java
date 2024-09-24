package nl.rgs.kib.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.rgs.kib.model.method.InspectionMethod;
import nl.rgs.kib.model.method.dto.CreateInspectionMethod;
import nl.rgs.kib.service.InspectionMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inspection-method")
@Tag(name = "Inspection Method")
public class InspectionMethodController {
    @Autowired
    private InspectionMethodService inspectionMethodService;

    @PreAuthorize("hasRole('ROLE_KIB_USER') or hasRole('ROLE_KIB_ADMIN')")
    @GetMapping
    @Operation(
            summary = "Find all inspection methods",
            description = "Find all inspection methods",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found all inspection method"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<List<InspectionMethod>> findAll() {
        return ResponseEntity.ok(inspectionMethodService.findAll());
    }

    @PreAuthorize("hasRole('ROLE_KIB_USER') or hasRole('ROLE_KIB_ADMIN')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Find an inspection method by id",
            description = "Find an inspection method by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found the inspection method"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Inspection method not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<InspectionMethod> findById(@PathVariable String id) {
        return inspectionMethodService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @PostMapping
    @Operation(
            summary = "Create an inspection method",
            description = "Create an inspection method",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created the inspection method"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<InspectionMethod> create(@Valid @RequestBody CreateInspectionMethod createInspectionMethod) {
        return ResponseEntity.status(201).body(inspectionMethodService.create(createInspectionMethod));
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @PutMapping("/{id}")
    @Operation(
            summary = "Update an inspection method",
            description = "Update an inspection method",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Inspection method not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<InspectionMethod> update(@Valid @RequestBody InspectionMethod inspectionMethod) {
        return inspectionMethodService.update(inspectionMethod)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an inspection method by id",
            description = "Delete an inspection method by id",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Deleted",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Inspection method not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return inspectionMethodService.deleteById(id)
                .map(inspectionMethod -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}
