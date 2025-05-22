package com.eucl.eucl_management_system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for user signup")
public class SignupRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 100)
    @Schema(description = "User's full name", example = "John Doe", required = true)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100)
    @Schema(description = "User's email address", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone should be 10 - 15 digits")
    @Schema(description = "User's phone number", example = "12345678901", required = true)
    private String phone;

    @NotBlank(message = "National ID is required")
    @Size(min = 16, max = 16, message = "National ID must be 16 characters")
    @Schema(description = "User's national ID", example = "1234567890123456", required = true)
    private String nationalId;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password should be at least 8 characters")
    @Schema(description = "User's password", example = "password123", required = true)
    private String password;

    @NotBlank(message = "Confirm password is required and must be the same as Password")
    @Size(min = 6, max = 100)
    @Schema(description = "Confirmation of the user's password", example = "password123", required = true)
    private String confirmPassword;

    public @NotBlank(message = "Name is required") @Size(max = 100) String getName() { return name; }
    public void setName(@NotBlank(message = "Name is required") @Size(max = 100) String name) { this.name = name; }
    public @NotBlank(message = "Email is required") @Email(message = "Email must be valid") @Size(max = 100) String getEmail() { return email; }
    public void setEmail(@NotBlank(message = "Email is required") @Email(message = "Email must be valid") @Size(max = 100) String email) { this.email = email; }
    public @NotBlank(message = "Phone is required") @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone should be 10 - 15 digits") String getPhone() { return phone; }
    public void setPhone(@NotBlank(message = "Phone is required") @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone should be 10 - 15 digits") String phone) { this.phone = phone; }
    public @NotBlank(message = "National ID is required") @Size(min = 16, max = 16, message = "National ID must be 16 characters") String getNationalId() { return nationalId; }
    public void setNationalId(@NotBlank(message = "National ID is required") @Size(min = 16, max = 16, message = "National ID must be 16 characters") String nationalId) { this.nationalId = nationalId; }
    public @NotBlank(message = "Password is required") @Size(min = 8, max = 100, message = "Password should be at least 8 characters") String getPassword() { return password; }
    public void setPassword(@NotBlank(message = "Password is required") @Size(min = 8, max = 100, message = "Password should be at least 8 characters") String password) { this.password = password; }
    public @NotBlank(message = "Confirm password is required and must be the same as Password") @Size(min = 6, max = 100) String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(@NotBlank(message = "Confirm password is required and must be the same as Password") @Size(min = 6, max = 100) String confirmPassword) { this.confirmPassword = confirmPassword; }
}