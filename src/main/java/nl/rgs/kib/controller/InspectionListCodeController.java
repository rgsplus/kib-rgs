package nl.rgs.kib.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.model.list.dto.CreateInspectionListCode;
import nl.rgs.kib.model.method.InspectionMethodCode;
import nl.rgs.kib.model.method.dto.CreateInspectionMethodCode;
import nl.rgs.kib.service.InspectionListCodeService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/inspection-list-code")
@Tag(name = "Inspection List Code")
public class InspectionListCodeController {
    @Autowired
    private InspectionListCodeService inspectionListCodeService;

    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found all inspection list codes"
            )
    })
    public ResponseEntity<List<InspectionListCode>> findAll() {
        return ResponseEntity.ok(inspectionListCodeService.findAll());
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the inspection list code"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inspection list code not found",
                    content = @Content()
            )
    })
    public ResponseEntity<InspectionListCode> findById(@PathVariable() String id) {
        return inspectionListCodeService.findById(new ObjectId(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created the inspection list code"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content()
            )
    })
    public ResponseEntity<InspectionListCode> create(@Valid() @RequestBody() CreateInspectionListCode createInspectionListCode) {
        return ResponseEntity.status(201).body(inspectionListCodeService.create(createInspectionListCode));
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
                    description = "Inspection list code not found",
                    content = @Content()
            ),
    })
    public ResponseEntity<InspectionListCode> update(@Valid() @RequestBody() InspectionListCode inspectionListCode) {
        return inspectionListCodeService.update(inspectionListCode)
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
                    description = "Inspection list code not found",
                    content = @Content()
            )
    })
    public ResponseEntity<Void> deleteById(@PathVariable() String id) {
        return inspectionListCodeService.deleteById(new ObjectId(id))
                .map(inspectionMethodCode -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}
