package com.edu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon {
    private Long id;
    private Long userId; // 改为userId
    private Long couponId; // 改为couponId
    private User user; // 保留用于业务逻辑
    private Coupon coupon; // 保留用于业务逻辑
    private CouponStatus status = CouponStatus.UNUSED;
    private LocalDateTime receiveTime = LocalDateTime.now();
    private LocalDateTime useTime;
    private Long orderId; // 改为orderId
    private Order order; // 保留用于业务逻辑
    
    public enum CouponStatus {
        UNUSED, USED, EXPIRED
    }
}