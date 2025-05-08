package com.eucl.eucl_management_system.dto.response;

import java.util.List;

public class UserWithMetersResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String nationalId;
    private List<String> roles;
    private List<String> meterNumbers;

    public UserWithMetersResponse(Long id, String name, String email, String phone,
                                  String nationalId, List<String> roles, List<String> meterNumbers) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.nationalId = nationalId;
        this.roles = roles;
        this.meterNumbers = meterNumbers;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getMeterNumbers() {
        return meterNumbers;
    }

    public void setMeterNumbers(List<String> meterNumbers) {
        this.meterNumbers = meterNumbers;
    }
}