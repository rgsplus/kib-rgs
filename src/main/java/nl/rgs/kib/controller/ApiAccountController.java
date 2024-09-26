package nl.rgs.kib.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.rgs.kib.model.account.ApiAccount;
import nl.rgs.kib.model.account.dto.CreateApiAccount;
import nl.rgs.kib.service.ApiAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-account")
@Tag(name = "Api Account")
public class ApiAccountController {
    @Autowired
    private ApiAccountService apiAccountService;

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @GetMapping
    @Operation(
            summary = "Find all api accounts",
            description = "Find all api accounts",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found all api accounts"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<List<ApiAccount>> findAll() {
        return ResponseEntity.ok(apiAccountService.findAll());
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Find an api account by id",
            description = "Find an api account by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Found the api account"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Api account not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<ApiAccount> findById(@PathVariable String id) {
        return apiAccountService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @PostMapping
    @Operation(
            summary = "Create an api account",
            description = "Create an api account",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created the api account"
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
    public ResponseEntity<ApiAccount> create(@Valid @RequestBody CreateApiAccount createApiAccount) {
        return ResponseEntity.status(201).body(apiAccountService.create(createApiAccount));
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @PutMapping("/{id}")
    @Operation(
            summary = "Update an api account",
            description = "Update an api account",
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
                            description = "Api account not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<ApiAccount> update(@Valid @RequestBody ApiAccount apiAccount) {
        return apiAccountService.update(apiAccount)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_KIB_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an api account by id",
            description = "Delete an api account by id",
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
                            description = "Api account not found",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        return apiAccountService.deleteById(id)
                .map(inspectionMethod -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}
