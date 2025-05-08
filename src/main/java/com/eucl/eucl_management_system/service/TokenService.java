package com.eucl.eucl_management_system.service;

import com.eucl.eucl_management_system.dto.request.PurchaseRequest;
import com.eucl.eucl_management_system.dto.response.TokenResponse;
import com.eucl.eucl_management_system.entity.Meter;
import com.eucl.eucl_management_system.entity.PurchasedToken;
import com.eucl.eucl_management_system.entity.User;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.repository.PurchasedTokenRepository;
import com.eucl.eucl_management_system.repository.UserRepository;
import com.eucl.eucl_management_system.repository.MeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TokenService {
    private static final int TOKEN_LENGTH = 16;

    @Autowired
    private PurchasedTokenRepository purchasedTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeterRepository meterRepository;

    @Value("${app.max-token-days}")
    private int maxTokenDays;

    @Value("${app.token-value-per-day}")
    private int tokenValuePerDay;

    @Transactional
    public TokenResponse purchaseToken(PurchaseRequest request) {
        // Validate amount
        if (request.getAmount() < tokenValuePerDay) {
            throw new IllegalArgumentException("Minimum purchase amount is " + tokenValuePerDay + " RWF");
        }

        if (request.getAmount() % tokenValuePerDay != 0) {
            throw new IllegalArgumentException("Amount must be a multiple of " + tokenValuePerDay);
        }

        // Calculate token value in days
        int tokenValueDays = request.getAmount() / tokenValuePerDay;

        if (tokenValueDays > maxTokenDays) {
            throw new IllegalArgumentException("Cannot purchase more than " + maxTokenDays + " days");
        }

        // Get current user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Verify meter exists and belongs to user
        Meter meter = meterRepository.findMeterByMeterNumber(request.getMeterNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Meter not found"));

        if (!meter.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Meter does not belong to this user");
        }

        // Generate and validate token
        String token = generateUniqueToken();

        // Create token entity
        PurchasedToken purchasedToken = new PurchasedToken();
        purchasedToken.setMeterNumber(request.getMeterNumber());
        purchasedToken.setToken(token);
        purchasedToken.setTokenStatus(PurchasedToken.TokenStatus.NEW);
        purchasedToken.setTokenValueDays(tokenValueDays);
        purchasedToken.setPurchaseDate(LocalDateTime.now());
        purchasedToken.setAmount(request.getAmount());
        purchasedToken.setUser(user);

        // Save token
        PurchasedToken savedToken = purchasedTokenRepository.save(purchasedToken);

        return mapToTokenResponse(savedToken);
    }

    private String generateUniqueToken() {
        Random random = new Random();
        String token;
        int attempts = 0;
        final int MAX_ATTEMPTS = 10;

        do {
            if (attempts++ >= MAX_ATTEMPTS) {
                throw new IllegalStateException("Failed to generate unique token after " + MAX_ATTEMPTS + " attempts");
            }

            // Generate 16-digit numeric token
            StringBuilder sb = new StringBuilder(TOKEN_LENGTH);
            for (int i = 0; i < TOKEN_LENGTH; i++) {
                sb.append(random.nextInt(10));
            }
            token = sb.toString();
        } while (purchasedTokenRepository.existsByToken(token));

        return token;
    }

    public List<TokenResponse> getUserTokens() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return purchasedTokenRepository.findByUser(user).stream()
                .map(this::mapToTokenResponse)
                .collect(Collectors.toList());
    }

    public List<TokenResponse> getMeterTokens(String meterNumber) {
        return purchasedTokenRepository.findByMeterNumber(meterNumber).stream()
                .map(this::mapToTokenResponse)
                .collect(Collectors.toList());
    }

    public List<TokenResponse> getAllTokens() {
        return purchasedTokenRepository.findAll().stream()
                .map(this::mapToTokenResponse)
                .collect(Collectors.toList());
    }

    private TokenResponse mapToTokenResponse(PurchasedToken token) {
        return new TokenResponse(
                token.getId(),
                token.getMeterNumber(),
                token.getToken(),
                token.getTokenStatus().name(),
                token.getTokenValueDays(),
                token.getPurchaseDate(),
                token.getAmount()
        );
    }
}