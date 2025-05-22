package com.eucl.eucl_management_system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for updating a notification")
public class NotificationUpdateRequest {
    @NotBlank(message = "Message is required")
    @Size(max = 255, message = "Message must not exceed 255 characters")
    @Schema(description = "Updated notification message", example = "Updated token status", required = true)
    private String message;

    @NotNull(message = "Email sent status is required")
    @Schema(description = "Indicates if the email has been sent", example = "false", required = true)
    private Boolean emailSent;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(Boolean emailSent) {
        this.emailSent = emailSent;
    }
}