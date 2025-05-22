package com.eucl.eucl_management_system.controller;

import com.eucl.eucl_management_system.dto.request.PurchaseRequest;
import com.eucl.eucl_management_system.dto.response.TokenResponse;
import com.eucl.eucl_management_system.dto.response.TokenValidationResponse;
import com.eucl.eucl_management_system.service.TokenService;
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
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;

@Tag(name = "Token", description = "Endpoints for managing tokens")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tokens")
public class TokenController {
    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Purchase token", description = "Purchase a new token for a meter")
    @ApiResponse(responseCode = "200", description = "Token successfully purchased")
    @PostMapping("/purchase")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> purchaseToken(@Valid @RequestBody PurchaseRequest purchaseRequest) {
        try {
            logger.info("Attempting to purchase token for meter: {}", purchaseRequest.getMeterNumber());
            TokenResponse response = tokenService.purchaseToken(purchaseRequest);
            logger.info("Successfully purchased token for meter: {}", purchaseRequest.getMeterNumber());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error purchasing token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to purchase token"));
        }
    }

    @Operation(summary = "Get tokens by meter number", description = "Retrieve all tokens for a specific meter")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved tokens")
    @GetMapping("/meter/{meterNumber}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<?> getMeterTokens(@PathVariable String meterNumber) {
        try {
            logger.info("Fetching tokens for meter: {}", meterNumber);
            List<TokenResponse> tokens = tokenService.getMeterTokens(meterNumber);
            logger.info("Successfully retrieved {} tokens for meter: {}", tokens.size(), meterNumber);
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            logger.error("Error fetching tokens for meter {}: {}", meterNumber, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve tokens"));
        }
    }

    @Operation(summary = "Validate token", description = "Validate a token by its value")
    @ApiResponse(responseCode = "200", description = "Token validation result")
    @GetMapping("/validate/{token}")
    public ResponseEntity<?> validateToken(@PathVariable String token) {
        try {
            logger.info("Validating token: {}", token);
            TokenValidationResponse response = tokenService.validateToken(token);
            logger.info("Successfully validated token: {}", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error validating token {}: {}", token, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to validate token"));
        }
    }

    @Operation(summary = "Get all tokens", description = "Retrieve all tokens (admin only)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all tokens")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TokenResponse>> getAllTokens() {
        try {
            logger.info("Fetching all tokens");
            List<TokenResponse> tokens = tokenService.getAllTokens();
            logger.info("Successfully retrieved {} tokens", tokens.size());
            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            logger.error("Error fetching all tokens: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(summary = "Update token", description = "Update a token by ID (admin only)")
    @ApiResponse(responseCode = "200", description = "Token successfully updated")
    @ApiResponse(responseCode = "404", description = "Token not found")
    @PutMapping("/{tokenId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateToken(@PathVariable Long tokenId, @Valid @RequestBody PurchaseRequest updateRequest) {
        try {
            logger.info("Attempting to update token with ID: {}", tokenId);
            TokenResponse updatedToken = tokenService.updateToken(tokenId, updateRequest);
            logger.info("Successfully updated token with ID: {}", tokenId);
            return ResponseEntity.ok(updatedToken);
        } catch (ResourceNotFoundException e) {
            logger.warn("Token not found for update with ID: {}", tokenId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Token not found"));
        } catch (Exception e) {
            logger.error("Error updating token with ID {}: {}", tokenId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update token"));
        }
    }

    @Operation(summary = "Delete token", description = "Delete a token by ID (admin only)")
    @ApiResponse(responseCode = "200", description = "Token successfully deleted")
    @ApiResponse(responseCode = "404", description = "Token not found")
    @DeleteMapping("/{tokenId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteToken(@PathVariable Long tokenId) {
        try {
            logger.info("Attempting to delete token with ID: {}", tokenId);
            tokenService.deleteToken(tokenId);
            logger.info("Successfully deleted token with ID: {}", tokenId);
            return ResponseEntity.ok(Map.of("message", "Token deleted successfully"));
        } catch (ResourceNotFoundException e) {
            logger.warn("Token not found for deletion with ID: {}", tokenId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Token not found"));
        } catch (Exception e) {
            logger.error("Error deleting token with ID {}: {}", tokenId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete token"));
        }
    }
}