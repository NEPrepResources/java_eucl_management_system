package com.eucl.eucl_management_system.dto.request;

import jakarta.validation.constraints.*;

public class SignupRequest {
    @NotBlank(message = "Name are required")
    @Size(max = 100)
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100)
    private String email;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone should be 10 - 15 digits")
    private String phone;
    @NotBlank(message = "National ID is required")
    @Size(min=16, max=16, message = "National id must be 16 characters")
    private String nationalId;
    @NotBlank(message = "Password is required")
    @Size(min=8, max=100, message = "Password should be at least 8 characters")
    private String password;
    @NotBlank(message = "Confirm password is required and must be the same as Password")
    @Size(min = 6, max = 100)
    private String confirmPassword;

    public @NotBlank(message = "Name are required") @Size(max = 100) String getName() {return name;}

    public void setName(@NotBlank(message = "Name are required") @Size(max = 100) String name) {this.name = name;}

    public @NotBlank(message = "Email is required") @Email(message = "Email must be valid") @Size(max = 100) String getEmail() {return email;}

    public void setEmail(@NotBlank(message = "Email is required") @Email(message = "Email must be valid") @Size(max = 100) String email) {this.email = email;}

    public @NotBlank(message = "Phone is required") @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone should be 10 - 15 digits") String getPhone() {return phone;}

    public void setPhone(@NotBlank(message = "Phone is required") @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone should be 10 - 15 digits") String phone) {this.phone = phone;}

    public @NotBlank(message = "National ID is required") @Size(min = 16, max = 16, message = "National id must be 16 characters") String getNationalId() {return nationalId;}

    public void setNationalId(@NotBlank(message = "National ID is required") @Size(min = 16, max = 16, message = "National id must be 16 characters") String nationalId) {this.nationalId = nationalId;}

    public @NotBlank(message = "Password is required") @Size(min = 8, max = 100, message = "Password should be at least 8 characters") String getPassword() {return password;}

    public void setPassword(@NotBlank(message = "Password is required") @Size(min = 8, max = 100, message = "Password should be at least 8 characters") String password) {this.password = password;}

    public @NotBlank(message = "Confirm password is required and must be the same as Password") @Size(min = 6, max = 100) String getConfirmPassword() {return confirmPassword;}

    public void setConfirmPassword(@NotBlank(message = "Confirm password is required and must be the same as Password") @Size(min = 6, max = 100) String confirmPassword) {this.confirmPassword = confirmPassword;}
}
