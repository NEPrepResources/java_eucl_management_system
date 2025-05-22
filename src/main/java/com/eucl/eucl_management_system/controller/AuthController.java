package com.eucl.eucl_management_system.controller;

import com.eucl.eucl_management_system.dto.request.LoginRequest;
import com.eucl.eucl_management_system.dto.request.SignupRequest;
import com.eucl.eucl_management_system.dto.response.JwtResponse;
import com.eucl.eucl_management_system.dto.response.MessageResponse;
import com.eucl.eucl_management_system.entity.Role;
import com.eucl.eucl_management_system.entity.User;
import com.eucl.eucl_management_system.entity.Erole;
import com.eucl.eucl_management_system.repository.RoleRepository;
import com.eucl.eucl_management_system.repository.UserRepository;
import com.eucl.eucl_management_system.security.jwt.JwtUtils;
import com.eucl.eucl_management_system.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponse(responseCode = "200", description = "User successfully logged in")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Attempting login for: {}", loginRequest.getEmail());

            // First check if the user exists in the database
            if (!userRepository.existsByEmail(loginRequest.getEmail())) {
                logger.warn("Login attempt with non-existent email: {}", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not found with this email"));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            logger.info("User {} logged in successfully", loginRequest.getEmail());
            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    userDetails.getId(),
                    userDetails.getName(),
                    userDetails.getEmail(),
                    userDetails.getPhone(),
                    roles));

        } catch (BadCredentialsException e) {
            logger.warn("Bad credentials for: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        } catch (DisabledException e) {
            logger.warn("Disabled account attempt: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Account is disabled"));
        } catch (LockedException e) {
            logger.warn("Locked account attempt: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Account is locked"));
        } catch (Exception e) {
            // More detailed exception logging
            logger.error("Login error for {}: {}", loginRequest.getEmail(),
                    e.getMessage() != null ? e.getMessage() : "No message");
            logger.error("Exception type: {}", e.getClass().getName());
            if (e.getCause() != null) {
                logger.error("Caused by: {}", e.getCause().getMessage());
            }
            e.printStackTrace(); // Print full stack trace in console for debugging

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Login failed. Please try again."));
        }
    }

    @Operation(summary = "Register new user", description = "Create new user account")
    @ApiResponse(responseCode = "200", description = "User successfully registered")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "Passwords do not match"));
            }

            if (signUpRequest.getPassword().length() < 8) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "Password must be at least 8 characters long"));
            }

            // Check for existing users
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "Email is already in use"));
            }

            if (userRepository.existsByPhone(signUpRequest.getPhone())) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "Phone number is already in use"));
            }

            if (userRepository.existsByNationalId(signUpRequest.getNationalId())) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "National ID is already registered"));
            }

            // Create new user
            User user = new User(
                    signUpRequest.getName(),
                    signUpRequest.getEmail(),
                    signUpRequest.getPhone(),
                    signUpRequest.getNationalId(),
                    passwordEncoder.encode(signUpRequest.getPassword()));

            Set<Role> roles = new HashSet<>();
            Role customerRole = roleRepository.findByName(Erole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(customerRole);
            user.setRoles(roles);

            userRepository.save(user);

            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } catch (Exception e) {
            logger.error("Registration error: {}", e.getMessage());
            logger.error("Exception type: {}", e.getClass().getName());
            if (e.getCause() != null) {
                logger.error("Caused by: {}", e.getCause().getMessage());
            }
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred during registration: " + e.getMessage()));
        }
    }

    @Operation(summary = "Register admin", description = "Create admin, one time admin creation")
    @ApiResponse(responseCode = "200", description = "Admin successfully created")
    @PostMapping("/create-admin")
    public ResponseEntity <?> createAdminUser(){
        if(userRepository.existsByEmail("admin@eucl.rw")){
            return ResponseEntity.badRequest().body("Admin already exists");
        }
        User admin = new User(
                "Admin",
                "admin@eucl.rw",
                "0781234567",
                "ADMIN12345123456",
                passwordEncoder.encode("admin123")
        );
        Set<Role> roles = new HashSet<>();
        Role adminRole = roleRepository.findByName(Erole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roles.add(adminRole);
        admin.setRoles(roles);
        userRepository.save(admin);
        return ResponseEntity.ok(new MessageResponse("Admin created successfully"));
    }
}