package com.eucl.eucl_management_system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Request object for purchasing a token")
public class PurchaseRequest {
    @NotBlank(message = "Meter number is required")
    @Size(min = 6, max = 6, message = "Meter number must be exactly 6 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Meter number must be alphanumeric")
    @Schema(description = "Meter number for the purchase", example = "ABC123", required = true)
    private String meterNumber;

    @NotNull(message = "Amount is required")
    @Min(value = 100, message = "Minimum purchase amount is 100 RWF")
    @Schema(description = "Purchase amount in RWF", example = "1000", required = true)
    private Integer amount;

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}