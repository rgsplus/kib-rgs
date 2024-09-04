package nl.rgs.kib.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.rgs.kib.model.list.InspectionList;
import nl.rgs.kib.service.InspectionListService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api/inspection-list")
@Tag(name = "Inspection List")
public class ApiInspectionListController {
    @Autowired
    private InspectionListService inspectionListService;

    @GetMapping()
    @Operation(
            summary = "Find all inspection lists",
            description = "Find all inspection lists",
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
                            description = "Found all inspection list"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
            }
    )
    public ResponseEntity<List<InspectionList>> findAll() {
        return ResponseEntity.ok(inspectionListService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find an inspection list by id",
            description = "Find an inspection list by id",
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
                            description = "Found the inspection list"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Inspection list not found",
                            content = @Content()
                    ),
            }
    )
    public ResponseEntity<InspectionList> findById(@PathVariable() String id) {
        return inspectionListService.findById(new ObjectId(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
