package com.eucl.eucl_management_system.service;

import com.eucl.eucl_management_system.dto.request.NotificationUpdateRequest;
import com.eucl.eucl_management_system.dto.response.NotificationResponse;
import com.eucl.eucl_management_system.entity.Notification;
import com.eucl.eucl_management_system.entity.PurchasedToken;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.repository.NotificationRepository;
import com.eucl.eucl_management_system.repository.PurchasedTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PurchasedTokenRepository purchasedTokenRepository;

    @Autowired
    private EmailService emailService;

    public void checkExpiringTokens() {
        logger.info("Checking for expiring tokens");
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        List<PurchasedToken> expiringTokens = purchasedTokenRepository
                .findByTokenStatusAndPurchasedDateBefore(PurchasedToken.TokenStatus.NEW, threshold);

        for (PurchasedToken token : expiringTokens) {
            if (token.getUser() == null || token.getUser().getEmail() == null) {
                logger.warn("No user or email found for token: {}", token.getToken());
                continue;
            }

            Notification notification = new Notification();
            notification.setMeterNumber(token.getMeterNumber());

            // Create detailed plain-text message
            String userName = token.getUser().getName() != null ? token.getUser().getName() : "Customer";
            LocalDateTime expirationDate = token.getPurchaseDate().plusDays(token.getTokenValueDays());
            String formattedDate = expirationDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm"));
            String message = String.format(
                    "Dear %s,\n\n" +
                            "We would like to inform you that your token is nearing expiration. Below are the details:\n\n" +
                            "--------------------------------------------------\n" +
                            "Token Number: %s\n" +
                            "Meter Number: %s\n" +
                            "Expiration Date: %s\n" +
                            "--------------------------------------------------\n\n" +
                            "Please purchase a new token to ensure uninterrupted service. You can do so via our website or contact our support team.\n\n" +
                            "For assistance, reach out to us at support@eucl.com or call (250)798-384-666.\n\n" +
                            "Thank you,\nEUCL Management Team",
                    userName, token.getToken(), token.getMeterNumber(), formattedDate
            );

            notification.setMessage(message);
            notification.setIssuedDate(LocalDateTime.now());
            notification.setUser(token.getUser());
            notification.setEmailSent(false);
            notificationRepository.save(notification);
            logger.info("Created notification for expiring token: {}", token.getToken());

            // Send email notification
            try {
                emailService.sendExpirationNotification(token.getUser().getEmail(), message);
                notification.setEmailSent(true);
                notificationRepository.save(notification);
                logger.info("Email sent successfully for token: {}", token.getToken());
            } catch (Exception e) {
                logger.error("Failed to send email for token: {}. Error: {}", token.getToken(), e.getMessage());
            }
        }
    }

    public NotificationResponse updateNotification(Long notificationId, NotificationUpdateRequest updateRequest) {
        logger.info("Updating notification with ID: {}", notificationId);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + notificationId));

        notification.setMessage(updateRequest.getMessage());
        notification.setEmailSent(updateRequest.getEmailSent());
        notificationRepository.save(notification);
        logger.info("Successfully updated notification with ID: {}", notificationId);

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

    public void deleteNotification(Long notificationId) {
        logger.info("Deleting notification with ID: {}", notificationId);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + notificationId));
        notificationRepository.delete(notification);
        logger.info("Successfully deleted notification with ID: {}", notificationId);
    }
}