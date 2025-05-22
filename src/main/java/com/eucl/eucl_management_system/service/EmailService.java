package com.eucl.eucl_management_system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.subject}")
    private String emailSubject;

    public void sendExpirationNotification(String toEmail, String message) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(fromEmail);
            msg.setTo(toEmail);
            msg.setSubject(emailSubject);
            msg.setText(message);
            mailSender.send(msg);
            logger.info("Email sent to: {}", toEmail);
        } catch (MailException e) {
            logger.error("Failed to send email to: {}. Error: {}", toEmail, e.getMessage());
            throw e;
        }
    }
}