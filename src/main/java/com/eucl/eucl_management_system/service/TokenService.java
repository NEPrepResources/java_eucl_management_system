package com.eucl.eucl_management_system.service;

import com.eucl.eucl_management_system.dto.request.PurchaseRequest;
import com.eucl.eucl_management_system.dto.response.TokenResponse;
import com.eucl.eucl_management_system.dto.response.TokenValidationResponse;
import com.eucl.eucl_management_system.entity.Meter;
import com.eucl.eucl_management_system.entity.PurchasedToken;
import com.eucl.eucl_management_system.entity.User;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.repository.MeterRepository;
import com.eucl.eucl_management_system.repository.PurchasedTokenRepository;
import com.eucl.eucl_management_system.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private PurchasedTokenRepository purchasedTokenRepository;

    @Autowired
    private MeterRepository meterRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public TokenResponse purchaseToken(PurchaseRequest purchaseRequest) {
        try {
            logger.info("Purchasing token for meter: {}", purchaseRequest.getMeterNumber());

            Meter meter = meterRepository.findMeterByMeterNumber(purchaseRequest.getMeterNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Meter not found with number: " + purchaseRequest.getMeterNumber()));
            User user = meter.getUser();
            if (user == null) {
                throw new ResourceNotFoundException("Meter has no associated user: " + purchaseRequest.getMeterNumber());
            }

            // Generate a unique 16-digit token
            String token = generateToken();
            while (purchasedTokenRepository.existsByToken(token)) {
                token = generateToken();
            }

            int tokenValueDays = calculateTokenValueDays(purchaseRequest.getAmount());
            if (tokenValueDays < 1) {
                throw new IllegalArgumentException("Amount must be sufficient to purchase at least 1 day (minimum 100 RWF)");
            }

            PurchasedToken purchasedToken = new PurchasedToken();
            purchasedToken.setMeterNumber(purchaseRequest.getMeterNumber());
            purchasedToken.setToken(token);
            purchasedToken.setTokenStatus(PurchasedToken.TokenStatus.NEW);
            purchasedToken.setTokenValueDays(tokenValueDays);
            purchasedToken.setPurchaseDate(LocalDateTime.now());
            purchasedToken.setAmount(purchaseRequest.getAmount());
            purchasedToken.setUser(user);
            purchasedToken = purchasedTokenRepository.save(purchasedToken);

            logger.info("Successfully purchased token for meter: {}, token ID: {}", purchaseRequest.getMeterNumber(), purchasedToken.getId());
            return new TokenResponse(
                    purchasedToken.getId(),
                    purchasedToken.getMeterNumber(),
                    purchasedToken.getToken(),
                    purchasedToken.getTokenStatus().name(),
                    purchasedToken.getTokenValueDays(),
                    purchasedToken.getPurchaseDate(),
                    purchasedToken.getAmount()
            );
        } catch (Exception e) {
            logger.error("Failed to purchase token for meter {}: {}", purchaseRequest.getMeterNumber(), e.getMessage(), e);
            throw e;
        }
    }

    public List<TokenResponse> getMeterTokens(String meterNumber) {
        logger.info("Fetching tokens for meter: {}", meterNumber);
        return purchasedTokenRepository.findByMeterNumber(meterNumber).stream()
                .map(token -> new TokenResponse(
                        token.getId(),
                        token.getMeterNumber(),
                        token.getToken(),
                        token.getTokenStatus().name(),
                        token.getTokenValueDays(),
                        token.getPurchaseDate(),
                        token.getAmount()))
                .collect(Collectors.toList());
    }

    public TokenValidationResponse validateToken(String token) {
        logger.info("Validating token: {}", token);
        PurchasedToken purchasedToken = purchasedTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found: " + token));

        String status = purchasedToken.getTokenStatus().name();
        String message = status.equals("NEW") ? "Token is valid" : "Token is " + status.toLowerCase();
        return new TokenValidationResponse(
                purchasedToken.getToken(),
                TokenValidationResponse.formatToken(purchasedToken.getToken()),
                purchasedToken.getMeterNumber(),
                purchasedToken.getTokenValueDays(),
                purchasedToken.getPurchaseDate(),
                status,
                message
        );
    }

    public List<TokenResponse> getAllTokens() {
        logger.info("Fetching all tokens");
        return purchasedTokenRepository.findAll().stream()
                .map(token -> new TokenResponse(
                        token.getId(),
                        token.getMeterNumber(),
                        token.getToken(),
                        token.getTokenStatus().name(),
                        token.getTokenValueDays(),
                        token.getPurchaseDate(),
                        token.getAmount()))
                .collect(Collectors.toList());
    }

    public TokenResponse updateToken(Long tokenId, PurchaseRequest updateRequest) {
        logger.info("Updating token with ID: {}", tokenId);
        PurchasedToken token = purchasedTokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found with ID: " + tokenId));

        Meter meter = meterRepository.findMeterByMeterNumber(updateRequest.getMeterNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Meter not found with number: " + updateRequest.getMeterNumber()));

        token.setMeterNumber(updateRequest.getMeterNumber());
        token.setAmount(updateRequest.getAmount());
        token.setTokenValueDays(calculateTokenValueDays(updateRequest.getAmount()));
        token.setUser(meter.getUser());
        purchasedTokenRepository.save(token);

        logger.info("Successfully updated token with ID: {}", tokenId);
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

    public void deleteToken(Long tokenId) {
        logger.info("Deleting token with ID: {}", tokenId);
        PurchasedToken token = purchasedTokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found with ID: " + tokenId));
        purchasedTokenRepository.delete(token);
        logger.info("Successfully deleted token with ID: {}", tokenId);
    }

    private String generateToken() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private int calculateTokenValueDays(int amount) {
        return Math.max(amount / 100, 1);
    }
}