package nl.rgsnl.kib.controller;

import nl.rgsnl.kib.service.InspectionListCodeService;
import nl.rgsnl.kib.service.InspectionMethodCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/inspection-method-code")
public class InspectionMethodCodeController {
    @Autowired
    private InspectionMethodCodeService inspectionMethodCodeService;
}
