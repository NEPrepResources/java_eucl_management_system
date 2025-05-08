package com.eucl.eucl_management_system.repository;

import com.eucl.eucl_management_system.entity.Meter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterRepository extends JpaRepository<Meter,Long> {
    Optional<Meter> findMeterByMeterNumber(Long meterNumber);
    boolean existsMeterByMeterNumber(String meterNumber);
    List<Meter> findByUser(String user);
}
