package com.eucl.eucl_management_system.controller;

import com.eucl.eucl_management_system.dto.response.NotificationResponse;
import com.eucl.eucl_management_system.entity.Notification;
import com.eucl.eucl_management_system.entity.PurchasedToken;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.repository.NotificationRepository;
import com.eucl.eucl_management_system.repository.PurchasedTokenRepository;
import com.eucl.eucl_management_system.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Notifications", description = "Endpoints for managing notifications")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private PurchasedTokenRepository purchasedTokenRepository;
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return ResponseEntity.ok(notifications.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/my-notifications")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Notification> notifications = notificationRepository.findByUserEmail(username);
        return ResponseEntity.ok(notifications.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/meter/{meterNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<NotificationResponse>> getNotificationById(@PathVariable String meterNumber) {
        List<Notification> notifications = notificationRepository.findByMeterNumber(meterNumber);
        return ResponseEntity.ok(notifications.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList()));
    }

    @PostMapping("/test-expiration/{tokenId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> testTokenExpiration(@PathVariable Long tokenId) {
        try {
            PurchasedToken token = purchasedTokenRepository.findById(tokenId)
                    .orElseThrow(() -> new ResourceNotFoundException("Token not found with ID: " + tokenId));

            if (token.getTokenValueDays() <= 0) {
                logger.error("Invalid token value days ({}) for token ID: {}", token.getTokenValueDays(), tokenId);
                throw new IllegalStateException("Token value days is invalid for token ID: " + tokenId);
            }

            token.setPurchaseDate(LocalDateTime.now().minusDays(token.getTokenValueDays()).plusHours(5));
            purchasedTokenRepository.save(token);
            logger.info("Updated purchase date for token ID: {}", tokenId);

            notificationService.checkExpiringTokens();
            logger.info("Successfully triggered notification check for token ID: {}", tokenId);

            return ResponseEntity.ok("Test notification triggered");
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            logger.error("Failed to test token expiration for token ID: {}. Error: {}", tokenId, e.getMessage(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to test token expiration: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private NotificationResponse convertToResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getMeterNumber(),
                notification.getMessage(),
                notification.getIssuedDate(),
                notification.getUser().getName(),
                notification.getUser().getEmail(),
                notification.isEmailSent()
        );
    }
}
