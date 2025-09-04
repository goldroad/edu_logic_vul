package com.edu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "`order`")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String orderNo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal originalAmount; // 原价
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO; // 折扣金额
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingFee = BigDecimal.ZERO; // 运费
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal finalAmount; // 最终金额
    
    private Integer quantity = 1; // 数量
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    private String paymentTransactionId;
    
    private LocalDateTime createTime = LocalDateTime.now();
    
    private LocalDateTime payTime;
    
    private String remark;
    
    public enum OrderStatus {
        PENDING, PAID, CANCELLED, REFUNDED
    }
    
    public enum PaymentMethod {
        ALIPAY, WECHAT, BALANCE
    }
}