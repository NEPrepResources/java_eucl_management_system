package com.eucl.eucl_management_system.controller;

import com.eucl.eucl_management_system.dto.request.PurchaseRequest;
import com.eucl.eucl_management_system.dto.response.TokenResponse;
import com.eucl.eucl_management_system.dto.response.TokenValidationResponse;
import com.eucl.eucl_management_system.service.TokenService;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins= "*", maxAge=3600)
@RestController
@RequestMapping("/api/tokens")
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @PostMapping("/purchase")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<TokenResponse> purchaseToken(@Valid @RequestBody PurchaseRequest purchaseRequest) {
        TokenResponse response = tokenService.purchaseToken(purchaseRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/meter/{meterNumber}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<List<TokenResponse>> getMeterTokens(
            @PathVariable String meterNumber) {
        List<TokenResponse> tokens = tokenService.getMeterTokens(meterNumber);
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/validate/{token}")
    public ResponseEntity<TokenValidationResponse> validateToken(@PathVariable String token) {
        TokenValidationResponse response = tokenService.validateToken(token);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<TokenResponse>> getUserTokenByMeterNumber(@PathVariable String meterNumber) {
        List<TokenResponse> tokens = tokenService.getUserTokensByMeterNumber(meterNumber);
        return ResponseEntity.ok(tokens);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TokenResponse>> getAllTokens() {
        List<TokenResponse> tokens = tokenService.getAllTokens();
        return ResponseEntity.ok(tokens);
    }
}
