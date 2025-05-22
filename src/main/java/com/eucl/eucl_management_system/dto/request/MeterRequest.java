package com.eucl.eucl_management_system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for registering a meter")
public class MeterRequest {
    @NotBlank(message = "Meter number is required")
    @Size(min = 6, max = 6, message = "Meter number must be exactly 6 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Meter number must be alphanumeric")
    @Schema(description = "Meter number for registration", example = "ABC123", required = true)
    private String meterNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Email of the user associated with the meter", example = "user@example.com", required = true)
    private String userEmail;

    public String getMeterNumber() { return meterNumber; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}