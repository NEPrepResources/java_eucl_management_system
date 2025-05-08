package com.eucl.eucl_management_system.dto.response;

import java.time.LocalDateTime;

public class NotificationResponse {
    private Long id;
    private String meterNumber;
    private String message;
    private LocalDateTime issuedDate;
    private String userName;
    private String userEmail;
    private boolean emailSent;

    public NotificationResponse(Long id, String meterNumber, String message, LocalDateTime issuedDate, String userName, String userEmail, boolean emailSent) {
        this.id = id;
        this.meterNumber = meterNumber;
        this.message = message;
        this.issuedDate = issuedDate;
        this.userName = userName;
        this.userEmail = userEmail;
        this.emailSent = emailSent;
    }
    public Long getId() {return id;}
    public String getMeterNumber() {return meterNumber;}
    public String getMessage() {return message;}
    public LocalDateTime getIssuedDate() {return issuedDate;}
    public String getUserName() {return userName;}
    public String getUserEmail() {return userEmail;}
    public boolean isEmailSent() {return emailSent;}
    public void setId(Long id) {this.id = id;}
    public void setMeterNumber(String meterNumber) {this.meterNumber = meterNumber;}
    public void setMessage(String message) {this.message = message;}
    public void setIssuedDate(LocalDateTime issuedDate) {this.issuedDate = issuedDate;}
    public void setUserName(String userName) {this.userName = userName;}
    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}
    public void setEmailSent(boolean emailSent) {this.emailSent = emailSent;}
}
