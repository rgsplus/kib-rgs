package nl.rgs.kib.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import nl.rgs.kib.model.list.dto.SummaryInspectionList;
import nl.rgs.kib.service.InspectionListService;
import nl.rgs.kib.shared.models.ImportDocument;
import nl.rgs.kib.shared.models.ImportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/inspection-list")
@Tag(name = "Inspection List")
public class InspectionListController {

    @Autowired
    private InspectionListService inspectionListService;

    @PreAuthorize("hasRole('ROLE_KIB_USER') or hasRole('ROLE_KIB_ADMIN')")
    @GetMapping
    @Operation(
            summary = "Find all inspection lists",
            description = "Find all inspection lists",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found all inspection list"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<List<InspectionList>> findAll() {
        return ResponseEntity.ok(inspectionListService.findAll());
    }

    @PreAuthorize("hasRole('ROLE_KIB_USER') or hasRole('ROLE_KIB_ADMIN')")
    @GetMapping("/summaries")
    @Operation(
            summary = "Find all summaries inspection list",
            description = "Find all summaries inspection list",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found all summaries inspection list"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<List<SummaryInspectionList>> findAllSummaries() {
        return ResponseEntity.ok(inspectionListService.findAllSummaries());
    }

    @PreAuthorize("hasRole('ROLE_KIB_USER') or hasRole('ROLE_KIB_ADMIN')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Find an inspection list by id",
            description = "Find an inspection list by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found the inspection list"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Inspection list not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<InspectionList> findById(@PathVariable String id) {
        return inspectionListService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @PostMapping
    @Operation(
            summary = "Create an inspection list",
            description = "Create an inspection list",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created the inspection list"
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
    public ResponseEntity<InspectionList> create(@Valid @RequestBody CreateInspectionList createInspectionList) {
        return ResponseEntity.status(201).body(inspectionListService.create(createInspectionList));
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @PutMapping("/{id}")
    @Operation(
            summary = "Update an inspection list",
            description = "Update an inspection list",
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
                            description = "Inspection list not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<InspectionList> update(@Valid @RequestBody InspectionList inspectionList) {
        return inspectionListService.update(inspectionList)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an inspection list",
            description = "Delete an inspection list",
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
                            description = "Inspection list not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return inspectionListService.deleteById(id)
                .map(inspectionMethod -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @PostMapping("/copy/{id}")
    @Operation(
            summary = "Copy an inspection list",
            description = "Copy an existing inspection list. A new inspection list will be created with the same items",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Copied the inspection list"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Inspection list not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<InspectionList> copy(@PathVariable String id) {
        return inspectionListService.copy(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @PutMapping("/copy/{id}/item/{itemId}")
    @Operation(
            summary = "Copy an inspection list item",
            description = "Copy an inspection list item of an existing inspection list. The copied item will be added to the existing inspection list.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Copied the inspection list"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Inspection list item not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<InspectionList> copyItem(@PathVariable String id, @PathVariable String itemId) {
        return inspectionListService.copyItem(id, itemId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @PostMapping("/import")
    @Operation(
            summary = "Import an inspection list",
            description = "Import an inspection list",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Imported the inspection list"
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
    public ResponseEntity<ImportResult<InspectionList>> importInspectionList(@Valid @RequestBody ImportDocument inspectionList) throws IOException {
        return ResponseEntity.ok(inspectionListService.importInspectionList(inspectionList));
    }
}
