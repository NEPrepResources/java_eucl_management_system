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
import java.util.List;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PurchasedTokenRepository purchasedTokenRepository;

    public void checkExpiringTokens() {
        logger.info("Checking for expiring tokens");
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        List<PurchasedToken> expiringTokens = purchasedTokenRepository
                .findByTokenStatusAndPurchasedDateBefore(PurchasedToken.TokenStatus.NEW, threshold);

        for (PurchasedToken token : expiringTokens) {
            Notification notification = new Notification();
            notification.setMeterNumber(token.getMeterNumber());
            notification.setMessage("Token " + token.getToken() + " is nearing expiration.");
            notification.setIssuedDate(LocalDateTime.now());
            notification.setUser(token.getUser());
            notification.setEmailSent(false);
            notificationRepository.save(notification);
            logger.info("Created notification for expiring token: {}", token.getToken());
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