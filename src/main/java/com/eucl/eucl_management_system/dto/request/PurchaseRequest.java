package com.eucl.eucl_management_system.dto.request;

import jakarta.validation.constraints.*;

public class PurchaseRequest {
    @NotBlank(message = "Meter number is required")
    @Size(min = 6, max = 6, message = "Meter number must be exactly 6 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Meter number must be alphanumeric")
    private String meterNumber;
    @NotNull(message = "Amount is required")
    @Min(value = 100, message = "Minimum purchase amount is 100 RWF")
    private Integer amount;
    public String getMeterNumber() {return meterNumber;}
    public void setMeterNumber(String meterNumber) {this.meterNumber = meterNumber;}
    public Integer getAmount() {return amount;}
    public void setAmount(Integer amount) {this.amount = amount;}

}
