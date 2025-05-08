package com.eucl.eucl_management_system.repository;

import com.eucl.eucl_management_system.entity.User;
import com.eucl.eucl_management_system.entity.PurchasedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchasedTokenRepository extends JpaRepository<PurchasedToken, Long>{
    List<PurchasedToken> findByUser(User user);
    List<PurchasedToken> findByMeterNumber(String meterNumber);
    boolean existsByToken(String token);
    Optional<PurchasedToken> findByToken(String token);
    @Query("SELECT t FROM PurchasedToken t WHERE t.tokenStatus = 'NEW' AND t.purchaseDate < :expirationThreshold")
    List<PurchasedToken> findByTokenStatusAndPurchasedDateBefore(
            PurchasedToken.TokenStatus aNew, @Param("expirationThreshold") LocalDateTime expirationThreshold);
}
