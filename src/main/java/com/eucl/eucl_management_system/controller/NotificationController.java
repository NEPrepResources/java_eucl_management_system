package com.eucl.eucl_management_system.controller;

import com.eucl.eucl_management_system.dto.response.NotificationResponse;
import com.eucl.eucl_management_system.entity.Notification;
import com.eucl.eucl_management_system.entity.PurchasedToken;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.repository.NotificationRepository;
import com.eucl.eucl_management_system.repository.PurchasedTokenRepository;
import com.eucl.eucl_management_system.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
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
                .collect(Collectors.toList())
        );
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
                .collect(Collectors.toList())
        );
    }
// Test Controller
    @PostMapping("/test-expiration/{tokenId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> testTokenExpiration(@PathVariable Long tokenId) {
        PurchasedToken token = purchasedTokenRepository.findById(tokenId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        token.setPurchaseDate(LocalDateTime.now().minusDays(token.getTokenValueDays()).plusHours(5));
        purchasedTokenRepository.save(token);

        notificationService.checkExpiringTokens();

        return ResponseEntity.ok("Test notification triggered");
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
