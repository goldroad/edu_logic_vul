package com.edu.repository;

import com.edu.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    
    Optional<Coupon> findByCode(String code);
    
    @Query("SELECT c FROM Coupon c WHERE c.enabled = true AND c.startTime <= :now AND c.endTime >= :now AND c.usedCount < c.totalCount")
    List<Coupon> findAvailableCoupons(LocalDateTime now);
}