package com.eucl.eucl_management_system.controller;

import com.eucl.eucl_management_system.dto.request.MeterRequest;
import com.eucl.eucl_management_system.dto.response.MeterResponse;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.service.MeterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/meters")
public class MeterController {

    @Autowired
    private MeterService meterService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerMeter(@Valid @RequestBody MeterRequest meterRequest) {
        try {
            MeterResponse response = meterService.registerMeter(meterRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterResponse>> getAllMeters() {
        return ResponseEntity.ok(meterService.getAllMeters());
    }

    @GetMapping("/user/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterResponse>> getMetersByUserEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(meterService.getMetersByUserEmail(email));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{meterNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeterResponse> getMeterByNumber(@PathVariable String meterNumber) {
        try {
            return ResponseEntity.ok(meterService.getMeterByNumber(meterNumber));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}