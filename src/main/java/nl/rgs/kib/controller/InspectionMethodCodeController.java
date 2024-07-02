package nl.rgs.kib.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.model.method.InspectionMethodCode;
import nl.rgs.kib.model.method.dto.CreateInspectionMethodCode;
import nl.rgs.kib.service.InspectionMethodCodeService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/inspection-method-code")
@Tag(name = "Inspection Method Code")
public class InspectionMethodCodeController {
    @Autowired
    private InspectionMethodCodeService inspectionMethodCodeService;

    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found all inspection method codes"
            )
    })
    public ResponseEntity<List<InspectionMethodCode>> findAll() {
        return ResponseEntity.ok(inspectionMethodCodeService.findAll());
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the inspection method code"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inspection method code not found",
                    content = @Content()
            )
    })
    public ResponseEntity<InspectionMethodCode> findById(@PathVariable() String id) {
        return inspectionMethodCodeService.findById(new ObjectId(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created the inspection method code"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content()
            )
    })
    public ResponseEntity<InspectionMethodCode> create(@Valid() @RequestBody() CreateInspectionMethodCode createInspectionMethodCode) {
        return ResponseEntity.status(201).body(inspectionMethodCodeService.create(createInspectionMethodCode));
    }

    @PutMapping("{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inspection method code not found",
                    content = @Content()
            ),
    })
    public ResponseEntity<InspectionMethodCode> update(@Valid() @RequestBody() InspectionMethodCode inspectionMethodCode) {
        return inspectionMethodCodeService.update(inspectionMethodCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inspection method code not found",
                    content = @Content()
            )
    })
    public ResponseEntity<Void> deleteById(@PathVariable() String id) {
        return inspectionMethodCodeService.deleteById(new ObjectId(id))
                .map(inspectionMethodCode -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}
