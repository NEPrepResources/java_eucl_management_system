package com.eucl.eucl_management_system.controller;

import com.eucl.eucl_management_system.dto.response.UserWithMetersResponse;
import com.eucl.eucl_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserWithMetersResponse>> getAllUsersWithMeters() {
        List<UserWithMetersResponse> users = userService.getAllUsersWithMeters();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserWithMetersResponse> getUserWithMeters(@PathVariable Long userId) {
        UserWithMetersResponse user = userService.getUserWithMetersById(userId);
        return ResponseEntity.ok(user);
    }
}