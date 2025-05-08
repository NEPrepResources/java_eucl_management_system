package com.eucl.eucl_management_system.controller;


import com.eucl.eucl_management_system.dto.request.MeterRequest;
import com.eucl.eucl_management_system.dto.response.MeterResponse;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.service.MeterService;
import jakarta.validation.Valid;
import org.hibernate.ResourceClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/meters")
public class MeterController {
    @Autowired
    private MeterService meterService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> register(@RequestBody @Valid MeterRequest meterRequest) {
        try {
            MeterResponse response = meterService.registerMeter(meterRequest);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (ResourceNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
