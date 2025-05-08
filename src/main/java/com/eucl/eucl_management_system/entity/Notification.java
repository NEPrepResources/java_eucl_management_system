package com.eucl.eucl_management_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "meter_number", nullable = false, length = 6)
    private String meterNumber;
    @Column(nullable = false)
    private String message;
    @Column(name = "issued_date", nullable = false)
    private LocalDateTime issuedDate = LocalDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private boolean emailSent = false;

    public Notification() {}
    public Notification(String meterNumber, String message, User user) {
        this.meterNumber = meterNumber;
        this.message = message;
        this.user = user;
    }
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getMeterNumber() {return meterNumber;}
    public void setMeterNumber(String meterNumber) {this.meterNumber = meterNumber;}
    public String getMessage() {return message;}
    public void setMessage(String message) {this.message = message;}
    public LocalDateTime getIssuedDate() {return issuedDate;}
    public void setIssuedDate(LocalDateTime issuedDate) {this.issuedDate = issuedDate;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public boolean isEmailSent() {return emailSent;}
    public void setEmailSent(boolean emailSent) {this.emailSent = emailSent;}
}
