package com.eucl.eucl_management_system.service;

import com.eucl.eucl_management_system.dto.request.MeterRequest;
import com.eucl.eucl_management_system.dto.response.MeterResponse;
import com.eucl.eucl_management_system.entity.Meter;
import com.eucl.eucl_management_system.entity.User;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.repository.MeterRepository;
import com.eucl.eucl_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeterService {

    @Autowired
    private MeterRepository meterRepository;

    @Autowired
    private UserRepository userRepository;

    public MeterResponse registerMeter(MeterRequest meterRequest) {
        // Check if meter number already exists
        if (meterRepository.existsByMeterNumber(meterRequest.getMeterNumber())) {
            throw new IllegalArgumentException("Meter number already exists");
        }

        // Find user by email
        User user = userRepository.findByEmail(meterRequest.getUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + meterRequest.getUserEmail()));

        // Create and save new meter
        Meter meter = new Meter(meterRequest.getMeterNumber(), user);
        Meter savedMeter = meterRepository.save(meter);

        return new MeterResponse(
                savedMeter.getId(),
                savedMeter.getMeterNumber(),
                user.getEmail(),
                user.getName());
    }

    public List<MeterResponse> getAllMeters() {
        List<Meter> meters = meterRepository.findAll();
        return meters.stream()
                .map(meter -> new MeterResponse(
                        meter.getId(),
                        meter.getMeterNumber(),
                        meter.getUser().getEmail(),
                        meter.getUser().getName()))
                .collect(Collectors.toList());
    }

    public List<MeterResponse> getMetersByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        List<Meter> meters = meterRepository.findByUser(user);
        return meters.stream()
                .map(meter -> new MeterResponse(
                        meter.getId(),
                        meter.getMeterNumber(),
                        user.getEmail(),
                        user.getName()))
                .collect(Collectors.toList());
    }

    public MeterResponse getMeterByNumber(String meterNumber) {
        Meter meter = meterRepository.findMeterByMeterNumber(meterNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Meter not found with number: " + meterNumber));

        return new MeterResponse(
                meter.getId(),
                meter.getMeterNumber(),
                meter.getUser().getEmail(),
                meter.getUser().getName());
    }
}