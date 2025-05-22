package com.eucl.eucl_management_system.controller;

import com.eucl.eucl_management_system.dto.request.UserUpdateRequest;
import com.eucl.eucl_management_system.dto.response.UserWithMetersResponse;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "AdminUser", description = "Endpoints for managing user profiles")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users", description = "Retrieve all users with their associated meters")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all users",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserWithMetersResponse.class))))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class, example = "{\"error\": \"Internal server error\"}")))
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserWithMetersResponse>> getAllUsersWithMeters() {
        try {
            logger.info("Fetching all users with meters");
            List<UserWithMetersResponse> users = userService.getAllUsersWithMeters();
            logger.info("Successfully retrieved {} users", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error fetching all users: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a specific user with their meters by ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserWithMetersResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class, example = "{\"error\": \"User not found\"}")))
    @ApiResponse(responseCode = "500", description = "Failed to retrieve user",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class, example = "{\"error\": \"Failed to retrieve user\"}")))
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserWithMeters(@PathVariable Long userId) {
        try {
            logger.info("Fetching user with ID: {}", userId);
            UserWithMetersResponse user = userService.getUserWithMetersById(userId);
            logger.info("Successfully retrieved user with ID: {}", userId);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException e) {
            logger.warn("User not found with ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        } catch (Exception e) {
            logger.error("Error fetching user with ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve user"));
        }
    }

    @Operation(summary = "Update user", description = "Update user details by ID")
    @ApiResponse(responseCode = "200", description = "User successfully updated",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserWithMetersResponse.class)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class, example = "{\"error\": \"User not found\"}")))
    @ApiResponse(responseCode = "500", description = "Failed to update user",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class, example = "{\"error\": \"Failed to update user\"}")))
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @Valid @RequestBody UserUpdateRequest updateRequest) {
        try {
            logger.info("Attempting to update user with ID: {}", userId);
            UserWithMetersResponse updatedUser = userService.updateUser(userId, updateRequest);
            logger.info("Successfully updated user with ID: {}", userId);
            return ResponseEntity.ok(updatedUser);
        } catch (ResourceNotFoundException e) {
            logger.warn("User not found for update with ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        } catch (Exception e) {
            logger.error("Error updating user with ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update user"));
        }
    }

    @Operation(summary = "Delete user", description = "Delete a user by ID")
    @ApiResponse(responseCode = "200", description = "User successfully deleted",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class, example = "{\"message\": \"User deleted successfully\"}")))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class, example = "{\"error\": \"User not found\"}")))
    @ApiResponse(responseCode = "500", description = "Failed to delete user",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class, example = "{\"error\": \"Failed to delete user\"}")))
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            logger.info("Attempting to delete user with ID: {}", userId);
            userService.deleteUser(userId);
            logger.info("Successfully deleted user with ID: {}", userId);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (ResourceNotFoundException e) {
            logger.warn("User not found for deletion with ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        } catch (Exception e) {
            logger.error("Error deleting user with ID {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete user"));
        }
    }
}