package com.eucl.eucl_management_system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;@Schema(description = "Response object for notification details")
public class NotificationResponse {
    @Schema(description = "Unique identifier of the notification", example = "1")
    private Long id;
    @Schema(description = "Meter number associated with the notification", example = "ABC123")
    private String meterNumber;

    @Schema(description = "Notification message", example = "Token ABC123 is nearing expiration.")
    private String message;
    @Schema(description = "Date and time the notification was issued", example = "2025-05-22T09:51:00")
    private LocalDateTime issuedDate;
    @Schema(description = "Name of the user associated with the notification", example = "John Doe")
    private String userName;

    @Schema(description = "Email of the user associated with the notification", example = "john.doe@example.com")
    private String userEmail;
    @Schema(description = "Indicates if the notification email was sent", example = "false")
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
