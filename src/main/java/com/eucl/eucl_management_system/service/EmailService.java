package com.eucl.eucl_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${app.email.from}")
    private String fromEmail;
    @Value("${app.email.subject}")
    private String emailSubject;

    public void sendExpirationNotification(String toEmail, String message){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(fromEmail);
        msg.setTo(toEmail);
        msg.setSubject(emailSubject);
        msg.setText(message);
        mailSender.send(msg);
    }

}
