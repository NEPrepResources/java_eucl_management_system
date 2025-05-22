package com.eucl.eucl_management_system.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object containing meter details")
public class MeterResponse {
    @Schema(description = "Unique identifier of the meter", example = "1", required = true)
    private Long id;

    @Schema(description = "Meter number", example = "ABC123", required = true)
    private String meterNumber;

    @Schema(description = "Email of the associated user", example = "user@example.com")
    private String userEmail;

    @Schema(description = "Name of the associated user", example = "John Doe")
    private String userName;

    public MeterResponse(Long id, String meterNumber, String userEmail, String userName) {
        this.id = id;
        this.meterNumber = meterNumber;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMeterNumber() { return meterNumber; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}