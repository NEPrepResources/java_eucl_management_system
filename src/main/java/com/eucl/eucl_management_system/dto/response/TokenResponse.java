package com.eucl.eucl_management_system.dto.response;

import java.time.LocalDateTime;

public class TokenResponse {
    private Long id;
    private String meterNumber;
    private String token;
    private String tokenStatus;
    private int tokenValueDays;
    private LocalDateTime purchaseDate;
    private int amount;
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
    public Long getId() {return id;}
    public String getMeterNumber() {return meterNumber;}
    public String getToken() {return token;}
    public String getTokenStatus() {return tokenStatus;}
    public int getTokenValueDays() {return tokenValueDays;}
    public LocalDateTime getPurchaseDate() {return purchaseDate;}
    public int getAmount() {return amount;}
    public String getFormattedToken() {return formattedToken;}
    public void setId(Long id) {this.id = id;}
    public void setMeterNumber(String meterNumber) {this.meterNumber = meterNumber;}
    public void setToken(String token) {this.token = token;}
    public void setTokenStatus(String tokenStatus) {this.tokenStatus = tokenStatus;}
    public void setTokenValueDays(int tokenValueDays) {this.tokenValueDays = tokenValueDays;}
    public void setPurchaseDate(LocalDateTime purchaseDate) {this.purchaseDate = purchaseDate;}
    public void setAmount(int amount) {this.amount = amount;}
    public void setFormattedToken(String formattedToken) {this.formattedToken = formattedToken;}
    private String formatToken(String token){
        if(token == null || token.length() != 16){ return token;}
        return  token.substring(0, 4) + "-" + token.substring(4, 8) + "-" + token.substring(8, 12) + "-" + token.substring(12, 16);
    }
}
