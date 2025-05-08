package com.eucl.eucl_management_system.repository;

import com.eucl.eucl_management_system.entity.User;
import com.eucl.eucl_management_system.entity.PurchasedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchasedTokenRepository extends JpaRepository<PurchasedToken, Long>{
    List<PurchasedToken> findByUser(User user);
    List<PurchasedToken> findByMeterNumber(String meterNumber);
    boolean existsByToken(String token);
}
