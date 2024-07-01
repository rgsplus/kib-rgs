package nl.rgs.kib.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.model.method.InspectionMethodCode;
import nl.rgs.kib.service.InspectionMethodCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return inspectionMethodCodeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
