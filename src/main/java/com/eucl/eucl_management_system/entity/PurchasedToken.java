package com.eucl.eucl_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_tokens")
public class PurchasedToken {
    public enum TokenStatus{
        NEW, USED, EXPIRED
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 6)
    @Size(min = 6, max = 6, message = "Meter number should be exactly 6 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Meter number must be alphanumeric")
    private String meterNumber;
    @Column(nullable = false, length = 16, unique = true)
    @Size(min = 16, max = 16, message = "Token must be exactly 16 digits")
    @Pattern(regexp = "^[0-9]*$", message = "Token must contain only digits")
    private String token;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenStatus tokenStatus = TokenStatus.NEW;
    @Column(name = "token_value_days", nullable = false)
    @Min(value = 1, message = "Token must be worth at least one day")
    private int tokenValueDays;
    @Column(name = "purchased_date", nullable = false)
    private LocalDateTime purchaseDate;
    @Column(nullable = false)
    @Min(value = 100, message = "Minimum purchase amount is 100rwf")
    private int amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public PurchasedToken() {}
    public PurchasedToken(Long id, String meterNumber, String token, TokenStatus tokenStatus, int tokenValueDays, LocalDateTime purchaseDate, int amount, User user) {
        this.id = id;
        this.meterNumber = meterNumber;
        this.token = token;
        this.tokenStatus = tokenStatus;
        this.tokenValueDays = tokenValueDays;
        this.purchaseDate = purchaseDate;
        this.amount = amount;
        this.user = user;
    }
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getMeterNumber() {return meterNumber;}
    public void setMeterNumber(String meterNumber) {this.meterNumber = meterNumber;}
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}
    public TokenStatus getTokenStatus() {return tokenStatus;}
    public void setTokenStatus(TokenStatus tokenStatus) {this.tokenStatus = tokenStatus;}
    public int getTokenValueDays() {return tokenValueDays;}
    public void setTokenValueDays(int tokenValueDays) {this.tokenValueDays = tokenValueDays;}
    public LocalDateTime getPurchaseDate() {return purchaseDate;}
    public void setPurchaseDate(LocalDateTime purchaseDate) {this.purchaseDate = purchaseDate;}
    public int getAmount() {return amount;}
    public void setAmount(int amount) {this.amount = amount;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
}
