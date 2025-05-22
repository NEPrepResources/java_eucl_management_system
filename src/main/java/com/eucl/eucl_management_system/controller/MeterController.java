package com.eucl.eucl_management_system.controller;

import com.eucl.eucl_management_system.dto.request.MeterRequest;
import com.eucl.eucl_management_system.dto.response.MeterResponse;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.service.MeterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Meter", description = "Endpoints for managing meters")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/meters")
public class MeterController {
    private static final Logger logger = LoggerFactory.getLogger(MeterController.class);

    @Autowired
    private MeterService meterService;

    @Operation(summary = "Register new meter", description = "Register a new meter for a user")
    @ApiResponse(responseCode = "200", description = "Meter successfully registered")
    @ApiResponse(responseCode = "400", description = "Invalid meter data")
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerMeter(@Valid @RequestBody MeterRequest meterRequest) {
        try {
            logger.info("Attempting to register new meter: {}", meterRequest.getMeterNumber());
            MeterResponse response = meterService.registerMeter(meterRequest);
            logger.info("Successfully registered meter: {}", meterRequest.getMeterNumber());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid meter data: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (ResourceNotFoundException e) {
            logger.warn("Resource not found for meter registration: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error registering meter: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to register meter"));
        }
    }

    @Operation(summary = "Get all meters", description = "Retrieve all registered meters")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all meters")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MeterResponse>> getAllMeters() {
        try {
            logger.info("Fetching all meters");
            List<MeterResponse> meters = meterService.getAllMeters();
            logger.info("Successfully retrieved {} meters", meters.size());
            return ResponseEntity.ok(meters);
        } catch (Exception e) {
            logger.error("Error fetching all meters: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(summary = "Get meters by user email", description = "Retrieve all meters for a specific user by email")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved meters")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/user/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getMetersByUserEmail(@PathVariable String email) {
        try {
            logger.info("Fetching meters for user email: {}", email);
            List<MeterResponse> meters = meterService.getMetersByUserEmail(email);
            logger.info("Successfully retrieved {} meters for user: {}", meters.size(), email);
            return ResponseEntity.ok(meters);
        } catch (ResourceNotFoundException e) {
            logger.warn("User not found with email: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        } catch (Exception e) {
            logger.error("Error fetching meters for user {}: {}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve meters"));
        }
    }

    @Operation(summary = "Get meter by number", description = "Retrieve a specific meter by meter number")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved meter")
    @ApiResponse(responseCode = "404", description = "Meter not found")
    @GetMapping("/{meterNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getMeterByNumber(@PathVariable String meterNumber) {
        try {
            logger.info("Fetching meter with number: {}", meterNumber);
            MeterResponse meter = meterService.getMeterByNumber(meterNumber);
            logger.info("Successfully retrieved meter: {}", meterNumber);
            return ResponseEntity.ok(meter);
        } catch (ResourceNotFoundException e) {
            logger.warn("Meter not found with number: {}", meterNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Meter not found"));
        } catch (Exception e) {
            logger.error("Error fetching meter {}: {}", meterNumber, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve meter"));
        }
    }

    @Operation(summary = "Update meter", description = "Update meter details by meter number")
    @ApiResponse(responseCode = "200", description = "Meter successfully updated")
    @ApiResponse(responseCode = "404", description = "Meter not found")
    @PutMapping("/{meterNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateMeter(@PathVariable String meterNumber, @Valid @RequestBody MeterRequest meterRequest) {
        try {
            logger.info("Attempting to update meter: {}", meterNumber);
            MeterResponse updatedMeter = meterService.updateMeter(meterNumber, meterRequest);
            logger.info("Successfully updated meter: {}", meterNumber);
            return ResponseEntity.ok(updatedMeter);
        } catch (ResourceNotFoundException e) {
            logger.warn("Meter not found for update: {}", meterNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Meter not found"));
        } catch (Exception e) {
            logger.error("Error updating meter {}: {}", meterNumber, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update meter"));
        }
    }

    @Operation(summary = "Delete meter", description = "Delete a meter by meter number")
    @ApiResponse(responseCode = "200", description = "Meter successfully deleted")
    @ApiResponse(responseCode = "404", description = "Meter not found")
    @DeleteMapping("/{meterNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMeter(@PathVariable String meterNumber) {
        try {
            logger.info("Attempting to delete meter: {}", meterNumber);
            meterService.deleteMeter(meterNumber);
            logger.info("Successfully deleted meter: {}", meterNumber);
            return ResponseEntity.ok(Map.of("message", "Meter deleted successfully"));
        } catch (ResourceNotFoundException e) {
            logger.warn("Meter not found for deletion: {}", meterNumber);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Meter not found"));
        } catch (Exception e) {
            logger.error("Error deleting meter {}: {}", meterNumber, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete meter"));
        }
    }
}