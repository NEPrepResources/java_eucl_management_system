package com.eucl.eucl_management_system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Response object for token validation details")
public class TokenValidationResponse {
    @Schema(description = "Original token value", example = "1234123490895678", required = true)
    private String token;

    @Schema(description = "Formatted token with hyphens", example = "1234-1234-9089-5678", required = true)
    private String formattedToken;

    @Schema(description = "Meter number associated with the token", example = "ABC123", required = true)
    private String meterNumber;

    @Schema(description = "Number of days the token is valid", example = "30", required = true)
    private int tokenValueDays;

    @Schema(description = "Date and time of purchase", example = "2025-05-22T10:17:00", required = true)
    private LocalDateTime purchaseDate;

    @Schema(description = "Validation status of the token", example = "VALID", required = true)
    private String status;

    @Schema(description = "Validation message", example = "Token is valid", required = true)
    private String message;

    public TokenValidationResponse(String token, String formattedToken, String meterNumber, int tokenValueDays, LocalDateTime purchaseDate, String status, String message) {
        this.token = token;
        this.formattedToken = formattedToken;
        this.meterNumber = meterNumber;
        this.tokenValueDays = tokenValueDays;
        this.purchaseDate = purchaseDate;
        this.status = status;
        this.message = message;
    }

    public String getToken() { return token; }
    public String getFormattedToken() { return formattedToken; }
    public String getMeterNumber() { return meterNumber; }
    public int getTokenValueDays() { return tokenValueDays; }
    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public void setToken(String token) { this.token = token; }
    public void setFormattedToken(String formattedToken) { this.formattedToken = formattedToken; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }
    public void setTokenValueDays(int tokenValueDays) { this.tokenValueDays = tokenValueDays; }
    public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }
    public void setStatus(String status) { this.status = status; }
    public void setMessage(String message) { this.message = message; }

    public static String formatToken(String token) {
        if (token == null || token.length() != 16) {
            return token;
        }
        return token.substring(0, 4) + " - " + token.substring(4, 8) + " - " + token.substring(8, 12) + " - " + token.substring(12, 16);
    }
}