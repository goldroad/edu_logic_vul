package com.edu.controller;

import com.edu.entity.Coupon;
import com.edu.entity.User;
import com.edu.entity.UserCoupon;
import com.edu.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {
    
    @Autowired
    private CouponService couponService;
    
    /**
     * 获取可用优惠券列表
     */
    @GetMapping("/available")
    public Map<String, Object> getAvailableCoupons() {
        Map<String, Object> response = new HashMap<>();
        
        List<Coupon> coupons = couponService.getAvailableCoupons();
        
        response.put("success", true);
        response.put("coupons", coupons);
        
        return response;
    }
    
    /**
     * 领取优惠券
     */
    @PostMapping("/{couponId}/receive")
    public Map<String, Object> receiveCoupon(@PathVariable Long couponId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return response;
        }
        
        // 领取优惠券处理
        boolean success = couponService.receiveCoupon(couponId, currentUser);
        
        if (success) {
            response.put("success", true);
            response.put("message", "优惠券领取成功");
        } else {
            response.put("success", false);
            response.put("message", "优惠券领取失败，可能已被领完或您已领取过");
        }
        
        return response;
    }
    
    /**
     * 安全领取优惠券
     */
    @PostMapping("/{couponId}/receive-safe")
    public Map<String, Object> receiveCouponSafe(@PathVariable Long couponId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return response;
        }
        
        // 使用安全的方法
        boolean success = couponService.receiveCouponSafe(couponId, currentUser);
        
        if (success) {
            response.put("success", true);
            response.put("message", "优惠券领取成功");
        } else {
            response.put("success", false);
            response.put("message", "优惠券领取失败，可能已被领完或您已领取过");
        }
        
        return response;
    }
    
    /**
     * 获取用户的优惠券
     */
    @GetMapping("/my")
    public Map<String, Object> getMyCoupons(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return response;
        }
        
        List<UserCoupon> userCoupons = couponService.getUserCoupons(currentUser);
        
        response.put("success", true);
        response.put("coupons", userCoupons);
        
        return response;
    }
    
    /**
     * 创建优惠券 - 未授权访问
     */
    @PostMapping("/create")
    public Map<String, Object> createCoupon(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        // 漏洞：接口未鉴权，任何人都可以创建优惠券
        try {
            String name = (String) request.get("name");
            String code = (String) request.get("code");
            Coupon.CouponType type = Coupon.CouponType.valueOf((String) request.get("type"));
            BigDecimal discountValue = new BigDecimal(request.get("discountValue").toString());
            BigDecimal minAmount = request.containsKey("minAmount") ? 
                new BigDecimal(request.get("minAmount").toString()) : BigDecimal.ZERO;
            Integer totalCount = ((Number) request.get("totalCount")).intValue();
            
            LocalDateTime startTime = LocalDateTime.now();
            LocalDateTime endTime = LocalDateTime.now().plusDays(30);
            
            Coupon coupon = couponService.createCoupon(name, code, type, discountValue, 
                minAmount, totalCount, startTime, endTime);
            
            response.put("success", true);
            response.put("message", "优惠券创建成功");
            response.put("coupon", coupon);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取所有优惠券 - 未授权访问
     */
    @GetMapping("/all")
    public Map<String, Object> getAllCoupons() {
        Map<String, Object> response = new HashMap<>();
        
        // 漏洞：接口未鉴权，任何人都可以查看所有优惠券
        List<Coupon> coupons = couponService.findAll();
        
        response.put("success", true);
        response.put("coupons", coupons);
        
        return response;
    }
    
    /**
     * 批量领取优惠券
     */
    @PostMapping("/{couponId}/batch-receive")
    public Map<String, Object> batchReceiveCoupon(@PathVariable Long couponId, 
                                                 @RequestParam(defaultValue = "10") int count,
                                                 HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return response;
        }
        
        // 批量领取处理
        int successCount = 0;
        for (int i = 0; i < count; i++) {
            if (couponService.receiveCoupon(couponId, currentUser)) {
                successCount++;
            }
        }
        
        response.put("success", true);
        response.put("message", String.format("尝试领取 %d 次，成功 %d 次", count, successCount));
        response.put("successCount", successCount);
        
        return response;
    }
    
    /**
     * 兑换优惠券（通过兑换码）
     */
    @PostMapping("/exchange")
    public Map<String, Object> exchangeCoupon(@RequestBody Map<String, String> request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return response;
        }
        
        String code = request.get("code");
        if (code == null || code.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "兑换码不能为空");
            return response;
        }
        
        try {
            // 通过兑换码兑换优惠券
            // 存在并发漏洞
            boolean success = couponService.exchangeCouponByCode(code.trim(), currentUser);
            
            if (success) {
                response.put("success", true);
                response.put("message", "优惠券兑换成功");
            } else {
                response.put("success", false);
                response.put("message", "兑换失败，兑换码无效或已被使用");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "兑换失败：" + e.getMessage());
        }
        
        return response;
    }
}