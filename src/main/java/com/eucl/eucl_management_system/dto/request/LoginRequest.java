package com.eucl.eucl_management_system.dto.request;

import jakarta.validation.constraints.*;

public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    @NotNull
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number should be 10 - 15 digits")
    private String phone;
    @NotBlank(message = "Password is required")
    private String password;

    public LoginRequest() {
    }
    public LoginRequest(String email, String phone, String password) {
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email) {
        this.email = email;
    }

    public @NotNull @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number should be 10 - 15 digits") String getPhone() {
        return phone;
    }

    public void setPhone(@NotNull @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number should be 10 - 15 digits") String phone) {
        this.phone = phone;
    }

    public @NotBlank(message = "Password is required") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required") String password) {
        this.password = password;
    }
}
