package com.eucl.eucl_management_system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Response object containing token purchase details")
public class TokenResponse {
    @Schema(description = "Unique identifier of the token", example = "1", required = true)
    private Long id;

    @Schema(description = "Meter number associated with the token", example = "ABC123", required = true)
    private String meterNumber;

    @Schema(description = "Token value", example = "1234123490895678", required = true)
    private String token;

    @Schema(description = "Status of the token", example = "NEW", required = true)
    private String tokenStatus;

    @Schema(description = "Number of days the token is valid", example = "30", required = true)
    private int tokenValueDays;

    @Schema(description = "Date and time of purchase", example = "2025-05-22T10:17:00", required = true)
    private LocalDateTime purchaseDate;

    @Schema(description = "Purchase amount in RWF", example = "1000", required = true)
    private int amount;

    @Schema(description = "Formatted token with hyphens", example = "ABCD-1234-EFGH-5678")
    private String formattedToken;

    public TokenResponse(Long id, String meterNumber, String token, String tokenStatus, int tokenValueDays, LocalDateTime purchaseDate, int amount) {
        this.id = id;
        this.meterNumber = meterNumber;
        this.token = token;
        this.tokenStatus = tokenStatus;
        this.tokenValueDays = tokenValueDays;
        this.purchaseDate = purchaseDate;
        this.amount = amount;
        this.formattedToken = formatToken(token);
    }

    public Long getId() { return id; }
    public String getMeterNumber() { return meterNumber; }
    public String getToken() { return token; }
    public String getTokenStatus() { return tokenStatus; }
    public int getTokenValueDays() { return tokenValueDays; }
    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public int getAmount() { return amount; }
    public String getFormattedToken() { return formattedToken; }
    public void setId(Long id) { this.id = id; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }
    public void setToken(String token) { this.token = token; }
    public void setTokenStatus(String tokenStatus) { this.tokenStatus = tokenStatus; }
    public void setTokenValueDays(int tokenValueDays) { this.tokenValueDays = tokenValueDays; }
    public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }
    public void setAmount(int amount) { this.amount = amount; }
    public void setFormattedToken(String formattedToken) { this.formattedToken = formattedToken; }
    private String formatToken(String token) {
        if (token == null || token.length() != 16) { return token; }
        return token.substring(0, 4) + "-" + token.substring(4, 8) + "-" + token.substring(8, 12) + "-" + token.substring(12, 16);
    }
}