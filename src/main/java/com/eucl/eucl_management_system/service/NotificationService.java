package com.eucl.eucl_management_system.service;

import com.eucl.eucl_management_system.entity.Notification;
import com.eucl.eucl_management_system.entity.PurchasedToken;
import com.eucl.eucl_management_system.entity.User;
import com.eucl.eucl_management_system.repository.NotificationRepository;
import com.eucl.eucl_management_system.repository.PurchasedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.web.webauthn.api.PublicKeyCose;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private PurchasedTokenRepository purchasedTokenRepository;
    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 * * * *")
    public void checkExpiringTokens(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationThreshold = now.plusHours(5);
        List<PurchasedToken> expiringTokens = purchasedTokenRepository
                .findByTokenStatusAndPurchasedDateBefore(
                        PurchasedToken.TokenStatus.NEW,
                        expirationThreshold
                );
        for(PurchasedToken token : expiringTokens){
            User user = token.getUser();
            String message = String.format(
                    "Dear %s, REG is pleased to remind you that the token in the %s is going to expire in 5 hours. Please purchase a new token.",
                    user.getName(),
                    token.getMeterNumber()
            );
            Notification notification = new Notification(
                    token.getMeterNumber(),
                    message,
                    user
            );
            emailService.sendExpirationNotification(user.getEmail(), message);
            notification.setEmailSent(true);
            notificationRepository.save(notification);
        }
    }
}
