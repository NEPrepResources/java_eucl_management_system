package com.eucl.eucl_management_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import  jakarta.validation.constraints.Pattern;

public class MeterRequest {
    @NotBlank(message = "Meter number is required")
    @Size(min = 6, max = 6, message = "Meter number must be exactly 6 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Meter number must be alphanumeric")
    private String meterNumber;
    @NotBlank(message = "Email is required")
    @Email(message = "Emaill must be valid")
    private String userEmail;

    public String getMeterNumber() {return meterNumber;}
    public void setMeterNumber(String meterNumber) {this.meterNumber = meterNumber;}
    public String getUserEmail() {return userEmail;}
    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}

}
