package com.eucl.eucl_management_system.controller;

import com.eucl.eucl_management_system.dto.request.NotificationUpdateRequest;
import com.eucl.eucl_management_system.dto.response.NotificationResponse;
import com.eucl.eucl_management_system.entity.Notification;
import com.eucl.eucl_management_system.entity.PurchasedToken;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.repository.NotificationRepository;
import com.eucl.eucl_management_system.repository.PurchasedTokenRepository;
import com.eucl.eucl_management_system.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Notification", description = "Endpoints for managing notifications")
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

    @Operation(summary = "Get all notifications", description = "Retrieve all notifications (admin only)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all notifications")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        try {
            logger.info("Fetching all notifications");
            List<Notification> notifications = notificationRepository.findAll();
            List<NotificationResponse> responses = notifications.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            logger.info("Successfully retrieved {} notifications", responses.size());
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error fetching all notifications: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(summary = "Get user notifications", description = "Retrieve notifications for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user notifications")
    @GetMapping("/my-notifications")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            logger.info("Fetching notifications for user: {}", username);
            List<Notification> notifications = notificationRepository.findByUserEmail(username);
            List<NotificationResponse> responses = notifications.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            logger.info("Successfully retrieved {} notifications for user: {}", responses.size(), username);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error fetching notifications for user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(summary = "Get notifications by meter number", description = "Retrieve notifications for a specific meter")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved meter notifications")
    @GetMapping("/meter/{meterNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<NotificationResponse>> getNotificationByMeterNumber(@PathVariable String meterNumber) {
        try {
            logger.info("Fetching notifications for meter: {}", meterNumber);
            List<Notification> notifications = notificationRepository.findByMeterNumber(meterNumber);
            List<NotificationResponse> responses = notifications.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
            logger.info("Successfully retrieved {} notifications for meter: {}", responses.size(), meterNumber);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            logger.error("Error fetching notifications for meter {}: {}", meterNumber, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
// Test Controller
    @PostMapping("/test-expiration/{tokenId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> testTokenExpiration(@PathVariable Long tokenId) {
        try {
            logger.info("Testing token expiration for token ID: {}", tokenId);
            PurchasedToken token = purchasedTokenRepository.findById(tokenId)
                    .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
            token.setPurchaseDate(LocalDateTime.now().minusDays(token.getTokenValueDays()).plusHours(5));
            purchasedTokenRepository.save(token);
            notificationService.checkExpiringTokens();
            logger.info("Successfully triggered test notification for token ID: {}", tokenId);
            return ResponseEntity.ok(Map.of("message", "Test notification triggered"));
        } catch (ResourceNotFoundException e) {
            logger.warn("Token not found with ID: {}", tokenId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Token not found"));
        } catch (Exception e) {
            logger.error("Error testing token expiration for token ID {}: {}", tokenId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to test token expiration"));
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