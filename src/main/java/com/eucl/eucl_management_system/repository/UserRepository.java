package com.eucl.eucl_management_system.repository;

import com.eucl.eucl_management_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByNationalId(String nationalId);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    Boolean existsByNationalId(String nationalId);
}
