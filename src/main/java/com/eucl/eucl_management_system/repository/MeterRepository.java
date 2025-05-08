package com.eucl.eucl_management_system.repository;

import com.eucl.eucl_management_system.entity.Meter;
import com.eucl.eucl_management_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterRepository extends JpaRepository<Meter, Long> {
    Optional<Meter> findMeterByMeterNumber(String meterNumber);
    boolean existsByMeterNumber(String meterNumber);
    List<Meter> findByUser(User user);
    List<Meter> findByUserId(Long userId);

}
