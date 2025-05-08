package com.eucl.eucl_management_system.dto.response;
public class MeterResponse {
    private Long id;
    private String meterNumber;
    private String userEmail;
    private String userName;

    public MeterResponse(Long id, String meterNumber, String userEmail, String userName) {
        this.id = id;
        this.meterNumber = meterNumber;
        this.userEmail = userEmail;
        this.userName = userName;
    }
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getMeterNumber() {return meterNumber;}
    public void setMeterNumber(String meterNumber) {this.meterNumber = meterNumber;}
    public String getUserEmail() {return userEmail;}
    public void setUserEmail(String userEmail) {this.userEmail = userEmail;}
    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}
}
