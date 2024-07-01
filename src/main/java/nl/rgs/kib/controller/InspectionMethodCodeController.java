package nl.rgs.kib.controller;

import nl.rgs.kib.model.list.InspectionListCode;
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
public class InspectionMethodCodeController {
    @Autowired
    private InspectionMethodCodeService inspectionMethodCodeService;

    @GetMapping()
    public ResponseEntity<List<InspectionListCode>> findAll() {
        return ResponseEntity.ok(inspectionMethodCodeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspectionListCode> findById(@PathVariable() String id) {
        return inspectionMethodCodeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
