package nl.rgs.kib.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.model.list.dto.CreateInspectionList;
import nl.rgs.kib.service.InspectionListService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/inspection-list")
@Tag(name = "Inspection List")
public class InspectionListController {
    @Autowired
    private InspectionListService inspectionListService;

    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found all inspection list codes"
            )
    })
    public ResponseEntity<List<InspectionList>> findAll() {
        return ResponseEntity.ok(inspectionListService.findAll());
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
    public ResponseEntity<InspectionList> findById(@PathVariable() String id) {
        return inspectionListService.findById(new ObjectId(id))
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
    public ResponseEntity<InspectionList> create(@Valid() @RequestBody() CreateInspectionList createInspectionList) {
        return ResponseEntity.status(201).body(inspectionListService.create(createInspectionList));
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
    public ResponseEntity<InspectionList> update(@Valid() @RequestBody() InspectionList inspectionList) {
        return inspectionListService.update(inspectionList)
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
        return inspectionListService.deleteById(new ObjectId(id))
                .map(inspectionMethod -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}
