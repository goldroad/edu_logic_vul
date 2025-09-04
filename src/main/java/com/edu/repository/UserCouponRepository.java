package com.edu.repository;

import com.edu.entity.User;
import com.edu.entity.UserCoupon;
import com.edu.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    
    List<UserCoupon> findByUser(User user);
    
    List<UserCoupon> findByUserAndStatus(User user, UserCoupon.CouponStatus status);
    
    Optional<UserCoupon> findByUserAndCoupon(User user, Coupon coupon);
    
    boolean existsByUserAndCoupon(User user, Coupon coupon);
}