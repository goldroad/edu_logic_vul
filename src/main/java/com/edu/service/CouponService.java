package com.edu.service;

import com.edu.entity.Coupon;
import com.edu.entity.User;
import com.edu.entity.UserCoupon;
import com.edu.repository.CouponRepository;
import com.edu.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponService {
    
    @Autowired
    private CouponRepository couponRepository;
    
    @Autowired
    private UserCouponRepository userCouponRepository;
    
    /**
     * 创建优惠券
     */
    public Coupon createCoupon(String name, String code, Coupon.CouponType type, 
                              BigDecimal discountValue, BigDecimal minAmount, 
                              Integer totalCount, LocalDateTime startTime, LocalDateTime endTime) {
        Coupon coupon = new Coupon();
        coupon.setName(name);
        coupon.setCode(code);
        coupon.setType(type);
        coupon.setDiscountValue(discountValue);
        coupon.setMinAmount(minAmount);
        coupon.setTotalCount(totalCount);
        coupon.setUsedCount(0);
        coupon.setStartTime(startTime);
        coupon.setEndTime(endTime);
        coupon.setEnabled(true);
        coupon.setCreateTime(LocalDateTime.now());
        
        couponRepository.save(coupon);
        return coupon;
    }
    
    /**
     * 领取优惠券
     */
    public boolean receiveCoupon(Long couponId, User user) {
        Coupon coupon = couponRepository.findById(couponId);
        if (coupon == null) {
            return false;
        }
        
        // 检查优惠券是否有效
        LocalDateTime now = LocalDateTime.now();
        if (!coupon.getEnabled() || 
            coupon.getStartTime().isAfter(now) || 
            coupon.getEndTime().isBefore(now)) {
            return false;
        }
        
        // 检查用户是否已经领取过
        if (userCouponRepository.existsByUserIdAndCouponId(user.getId(), coupon.getId())) {
            return false;
        }
        
        // 检查优惠券数量
        if (coupon.getUsedCount() >= coupon.getTotalCount()) {
            return false;
        }
        
        // 处理时间模拟
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 创建用户优惠券记录
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(user.getId());
        userCoupon.setCouponId(coupon.getId());
        userCoupon.setUser(user);
        userCoupon.setCoupon(coupon);
        userCoupon.setStatus(UserCoupon.CouponStatus.UNUSED);
        userCoupon.setReceiveTime(LocalDateTime.now());
        userCouponRepository.save(userCoupon);
        
        // 更新已使用数量
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.update(coupon);
        
        return true;
    }
    
    /**
     * 安全的领取优惠券方法（使用synchronized）
     */
    public synchronized boolean receiveCouponSafe(Long couponId, User user) {
        Coupon coupon = couponRepository.findById(couponId);
        if (coupon == null) {
            return false;
        }
        
        // 检查优惠券是否有效
        LocalDateTime now = LocalDateTime.now();
        if (!coupon.getEnabled() || 
            coupon.getStartTime().isAfter(now) || 
            coupon.getEndTime().isBefore(now)) {
            return false;
        }
        
        // 检查用户是否已经领取过
        if (userCouponRepository.existsByUserIdAndCouponId(user.getId(), coupon.getId())) {
            return false;
        }
        
        // 检查数量
        if (coupon.getUsedCount() >= coupon.getTotalCount()) {
            return false;
        }
        
        // 创建用户优惠券记录
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(user.getId());
        userCoupon.setCouponId(coupon.getId());
        userCoupon.setUser(user);
        userCoupon.setCoupon(coupon);
        userCoupon.setStatus(UserCoupon.CouponStatus.UNUSED);
        userCoupon.setReceiveTime(LocalDateTime.now());
        userCouponRepository.save(userCoupon);
        
        // 更新已使用数量
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.update(coupon);
        
        return true;
    }
    
    /**
     * 获取可用优惠券列表
     */
    public List<Coupon> getAvailableCoupons() {
        return couponRepository.findAvailableCoupons(LocalDateTime.now());
    }
    
    /**
     * 获取用户的优惠券
     */
    public List<UserCoupon> getUserCoupons(User user) {
        return userCouponRepository.findByUserId(user.getId());
    }
    
    /**
     * 获取用户未使用的优惠券
     */
    public List<UserCoupon> getUserUnusedCoupons(User user) {
        return userCouponRepository.findByUserIdAndStatus(user.getId(), "UNUSED");
    }
    
    public Coupon findById(Long id) {
        return couponRepository.findById(id);
    }
    
    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }
    
    public Coupon findByCode(String code) {
        return couponRepository.findByCode(code);
    }
}