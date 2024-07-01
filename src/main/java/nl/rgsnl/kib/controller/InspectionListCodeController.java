package nl.rgsnl.kib.controller;

import nl.rgsnl.kib.model.list.InspectionListCode;
import nl.rgsnl.kib.service.InspectionListCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
