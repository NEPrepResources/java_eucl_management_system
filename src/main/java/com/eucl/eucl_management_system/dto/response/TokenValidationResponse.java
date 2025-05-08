package com.eucl.eucl_management_system.dto.response;

import java.time.LocalDateTime;

public class TokenValidationResponse {
    private String token;
    private String formattedToken;
    private String meterNumber;
    private int tokenValueDays;
    private LocalDateTime purchaseDate;
    private String status;
    private String message;

    public TokenValidationResponse(String token, String formattedToken, String meterNumber,int tokenValueDays, LocalDateTime purchaseDate, String status, String message) {
        this.token = token;
        this.formattedToken = formattedToken;
        this.meterNumber = meterNumber;
        this.tokenValueDays = tokenValueDays;
        this.purchaseDate = purchaseDate;
        this.status = status;
        this.message = message;
    }
    public String getToken() {return token;}
    public String getFormattedToken() {return formattedToken;}
    public String getMeterNumber() {return meterNumber;}
    public int getTokenValueDays() {return tokenValueDays;}
    public LocalDateTime getPurchaseDate() {return purchaseDate;}
    public String getStatus() {return status;}
    public String getMessage() {return message;}
    public void setToken(String token) {this.token = token;}
    public void setFormattedToken(String formattedToken) {this.formattedToken = formattedToken;}
    public void setMeterNumber(String meterNumber) {this.meterNumber = meterNumber;}
    public void setTokenValueDays(int tokenValueDays) {this.tokenValueDays = tokenValueDays;}
    public void setPurchaseDate(LocalDateTime purchaseDate) {this.purchaseDate = purchaseDate;}
    public void setStatus(String status) {this.status = status;}
    public void setMessage(String message) {this.message = message;}

    public static String formatToken(String token) {
        if(token==null || token.length() != 16){
            return token;
        }
        return token.substring(0,4)+" - "+ token.substring(4,8)+" - "+ token.substring(8,12)+" - "+ token.substring(12,16);

    }
}
