package nl.rgs.kib.controller;

import nl.rgs.kib.model.list.InspectionListCode;
import nl.rgs.kib.service.InspectionListCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/inspection-list-code")
public class InspectionListCodeController {
    @Autowired
    private InspectionListCodeService inspectionListCodeService;

    @GetMapping()
    public ResponseEntity<List<InspectionListCode>> findAll() {
        return ResponseEntity.ok(inspectionListCodeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspectionListCode> findById(@PathVariable() String id) {
        return inspectionListCodeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
