package com.eucl.eucl_management_system.service;

import com.eucl.eucl_management_system.dto.response.MeterResponse;
import com.eucl.eucl_management_system.dto.request.MeterRequest;
import com.eucl.eucl_management_system.entity.User;
import com.eucl.eucl_management_system.entity.Meter;
import com.eucl.eucl_management_system.exception.ResourceNotFoundException;
import com.eucl.eucl_management_system.repository.MeterRepository;
import com.eucl.eucl_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MeterService {
    @Autowired
    private MeterRepository meterRepository;
    @Autowired
    private UserRepository userRepository;

    public MeterResponse registerMeter(MeterRequest meterRequest) {
        if(meterRepository.existsMeterByMeterNumber(meterRequest.getMeterNumber())){
            throw new IllegalArgumentException("Meter number already exists");
        }

        User user = userRepository.findByEmail(meterRequest.getUserEmail())
                .orElseThrow(()-> new ResourceNotFoundException("User not found with email: " + meterRequest.getUserEmail()));
        Meter meter = new Meter(meterRequest.getMeterNumber(), user);
        Meter savedMeter = meterRepository.save(meter);

        return  new MeterResponse(
                savedMeter.getId(),
                savedMeter.getMeterNumber(),
                user.getEmail(),
                user.getName()
        );

    }
}
