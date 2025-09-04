package com.edu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String code;
    
    @Enumerated(EnumType.STRING)
    private CouponType type;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal discountValue; // 折扣值
    
    @Column(precision = 10, scale = 2)
    private BigDecimal minAmount; // 最小使用金额
    
    private Integer totalCount; // 总数量
    
    private Integer usedCount = 0; // 已使用数量
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    @Column(columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean enabled = true;
    
    private LocalDateTime createTime = LocalDateTime.now();
    
    public enum CouponType {
        FIXED, // 固定金额
        PERCENT // 百分比折扣
    }
}