package com.eucl.eucl_management_system.service;

import com.eucl.eucl_management_system.dto.response.UserWithMetersResponse;
import com.eucl.eucl_management_system.entity.User;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserWithMetersResponse> getAllUsersWithMeters() {
        return userRepository.findAll().stream()
                .map(user -> new UserWithMetersResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getNationalId(),
                        user.getRoles().stream()
                                .map(role -> role.getName().name())
                                .collect(Collectors.toList()),
                        user.getMeters().stream()
                                .map(meter -> meter.getMeterNumber())
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public UserWithMetersResponse getUserWithMetersById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return new UserWithMetersResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getNationalId(),
                user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList()),
                user.getMeters().stream()
                        .map(meter -> meter.getMeterNumber())
                        .collect(Collectors.toList())
        );
    }
}