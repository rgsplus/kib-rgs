package nl.rgs.kib.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.rgs.kib.model.list.InspectionListCode;
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

    @GetMapping("/findAll")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found all inspection list codes"
            )
    })
    public ResponseEntity<List<InspectionListCode>> findAll() {
        return ResponseEntity.ok(inspectionListCodeService.findAll());
    }

    @GetMapping("/findById/{id}")
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
}
