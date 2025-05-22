package com.eucl.eucl_management_system.service;

import com.eucl.eucl_management_system.dto.request.UserUpdateRequest;
import com.eucl.eucl_management_system.dto.response.UserWithMetersResponse;
import com.eucl.eucl_management_system.entity.Meter;
import com.eucl.eucl_management_system.entity.Role;
import com.eucl.eucl_management_system.entity.User;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.repository.MeterRepository;
import com.eucl.eucl_management_system.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeterRepository meterRepository;

    public List<UserWithMetersResponse> getAllUsersWithMeters() {
        logger.info("Fetching all users with their meters");
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToUserWithMetersResponse).collect(Collectors.toList());
    }

    public UserWithMetersResponse getUserWithMetersById(Long userId) {
        logger.info("Fetching user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return convertToUserWithMetersResponse(user);
    }

    public UserWithMetersResponse updateUser(Long userId, UserUpdateRequest updateRequest) {
        logger.info("Updating user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Check for unique constraints
        if (!user.getEmail().equals(updateRequest.getEmail()) && userRepository.existsByEmail(updateRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        if (!user.getPhone().equals(updateRequest.getPhone()) && userRepository.existsByPhone(updateRequest.getPhone())) {
            throw new IllegalArgumentException("Phone number is already in use");
        }
        if (!user.getNationalId().equals(updateRequest.getNationalId()) && userRepository.existsByNationalId(updateRequest.getNationalId())) {
            throw new IllegalArgumentException("National ID is already registered");
        }

        // Update user fields
        user.setName(updateRequest.getName());
        user.setEmail(updateRequest.getEmail());
        user.setPhone(updateRequest.getPhone());
        user.setNationalId(updateRequest.getNationalId());

        userRepository.save(user);
        logger.info("Successfully updated user with ID: {}", userId);
        return convertToUserWithMetersResponse(user);
    }

    public void deleteUser(Long userId) {
        logger.info("Deleting user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        userRepository.delete(user);
        logger.info("Successfully deleted user with ID: {}", userId);
    }

    private UserWithMetersResponse convertToUserWithMetersResponse(User user) {
        List<String> meterNumbers = meterRepository.findByUser(user).stream()
                .map(Meter::getMeterNumber)
                .collect(Collectors.toList());
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .map(Enum::name)
                .collect(Collectors.toList());
        return new UserWithMetersResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getNationalId(),
                roles,
                meterNumbers
        );
    }
}