package com.eucl.eucl_management_system.repository;

import com.eucl.eucl_management_system.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserEmail(String email);
    List<Notification> findByMeterNumber(String meterNumber);
    List<Notification> findByEmailSent(boolean meterSent);
}