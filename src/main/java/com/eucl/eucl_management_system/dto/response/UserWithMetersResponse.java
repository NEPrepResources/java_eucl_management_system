package com.eucl.eucl_management_system.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Response object containing user details with associated meters")
public class UserWithMetersResponse {
    @Schema(description = "Unique identifier of the user", example = "1", required = true)
    private Long id;

    @Schema(description = "User's full name", example = "John Doe")
    private String name;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "User's phone number", example = "12345678901")
    private String phone;

    @Schema(description = "User's national ID", example = "1234567890123456")
    private String nationalId;

    @ArraySchema(schema = @Schema(description = "User's roles", example = "ROLE_USER", type = "string"))
    private List<String> roles;

    @ArraySchema(schema = @Schema(description = "Meter numbers associated with the user", example = "ABC123", type = "string"))
    private List<String> meterNumbers;

    public UserWithMetersResponse(Long id, String name, String email, String phone, String nationalId, List<String> roles, List<String> meterNumbers) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.nationalId = nationalId;
        this.roles = roles;
        this.meterNumbers = meterNumbers;
    }

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