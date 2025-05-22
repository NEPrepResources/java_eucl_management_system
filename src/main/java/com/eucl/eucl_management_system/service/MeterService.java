package com.eucl.eucl_management_system.service;

import com.eucl.eucl_management_system.dto.request.MeterRequest;
import com.eucl.eucl_management_system.dto.response.MeterResponse;
import com.eucl.eucl_management_system.entity.Meter;
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
public class MeterService {
    private static final Logger logger = LoggerFactory.getLogger(MeterService.class);

    @Autowired
    private MeterRepository meterRepository;

    @Autowired
    private UserRepository userRepository;

    public MeterResponse registerMeter(MeterRequest meterRequest) {
        logger.info("Registering meter: {}", meterRequest.getMeterNumber());
        if (meterRepository.existsByMeterNumber(meterRequest.getMeterNumber())) {
            throw new IllegalArgumentException("Meter number already exists");
        }
        User user = userRepository.findByEmail(meterRequest.getUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + meterRequest.getUserEmail()));
        Meter meter = new Meter();
        meter.setMeterNumber(meterRequest.getMeterNumber());
        meter.setUser(user);
        meterRepository.save(meter);
        logger.info("Successfully registered meter: {}", meterRequest.getMeterNumber());
        return new MeterResponse(meter.getId(), meter.getMeterNumber(), user.getEmail(), user.getName());
    }

    public List<MeterResponse> getAllMeters() {
        logger.info("Fetching all meters");
        return meterRepository.findAll().stream()
                .map(meter -> new MeterResponse(
                        meter.getId(),
                        meter.getMeterNumber(),
                        meter.getUser().getEmail(),
                        meter.getUser().getName()))
                .collect(Collectors.toList());
    }

    public List<MeterResponse> getMetersByUserEmail(String email) {
        logger.info("Fetching meters for user email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return meterRepository.findByUser(user).stream()
                .map(meter -> new MeterResponse(
                        meter.getId(),
                        meter.getMeterNumber(),
                        user.getEmail(),
                        user.getName()))
                .collect(Collectors.toList());
    }

    public MeterResponse getMeterByNumber(String meterNumber) {
        logger.info("Fetching meter with number: {}", meterNumber);
        Meter meter = meterRepository.findMeterByMeterNumber(meterNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Meter not found with number: " + meterNumber));
        return new MeterResponse(
                meter.getId(),
                meter.getMeterNumber(),
                meter.getUser().getEmail(),
                meter.getUser().getName());
    }

    public MeterResponse updateMeter(String meterNumber, MeterRequest meterRequest) {
        logger.info("Updating meter: {}", meterNumber);
        Meter meter = meterRepository.findMeterByMeterNumber(meterNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Meter not found with number: " + meterNumber));

        // Check if new meter number is unique (if changed)
        if (!meter.getMeterNumber().equals(meterRequest.getMeterNumber()) &&
                meterRepository.existsByMeterNumber(meterRequest.getMeterNumber())) {
            throw new IllegalArgumentException("Meter number already exists");
        }

        User user = userRepository.findByEmail(meterRequest.getUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + meterRequest.getUserEmail()));

        meter.setMeterNumber(meterRequest.getMeterNumber());
        meter.setUser(user);
        meterRepository.save(meter);
        logger.info("Successfully updated meter: {}", meterNumber);
        return new MeterResponse(
                meter.getId(),
                meter.getMeterNumber(),
                user.getEmail(),
                user.getName());
    }

    public void deleteMeter(String meterNumber) {
        logger.info("Deleting meter: {}", meterNumber);
        Meter meter = meterRepository.findMeterByMeterNumber(meterNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Meter not found with number: " + meterNumber));
        meterRepository.delete(meter);
        logger.info("Successfully deleted meter: {}", meterNumber);
    }
}