package com.edu.service;

import com.edu.entity.Course;
import com.edu.entity.Order;
import com.edu.entity.User;
import com.edu.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CourseService courseService;
    
    /**
     * 创建订单 - 包含支付逻辑漏洞
     */
    public Order createOrder(Long userId, Long courseId, BigDecimal clientPrice, 
                           Integer quantity, BigDecimal discount, BigDecimal shippingFee) {
        
        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        Course course = courseService.findById(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(user.getId());
        order.setCourseId(course.getId());
        order.setUser(user);
        order.setCourse(course);
        
        // 漏洞1：直接使用客户端传来的价格，不验证
        order.setOriginalAmount(clientPrice);
        
        // 漏洞2：允许负数数量
        order.setQuantity(quantity);
        
        // 漏洞3：允许任意折扣
        order.setDiscountAmount(discount != null ? discount : BigDecimal.ZERO);
        
        // 漏洞4：允许修改运费（在线课程不应该有运费）
        order.setShippingFee(shippingFee != null ? shippingFee : BigDecimal.ZERO);
        
        // 计算最终金额（存在漏洞的计算逻辑）
        BigDecimal finalAmount = clientPrice
                .multiply(BigDecimal.valueOf(quantity))
                .subtract(order.getDiscountAmount())
                .add(order.getShippingFee());
        
        order.setFinalAmount(finalAmount);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreateTime(LocalDateTime.now());
        
        orderRepository.save(order);
        return order;
    }
    
    /**
     * 正确的订单创建方法（用于对比）
     */
    public Order createOrderSecure(Long userId, Long courseId) {
        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        Course course = courseService.findById(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(user.getId());
        order.setCourseId(course.getId());
        order.setUser(user);
        order.setCourse(course);
        order.setOriginalAmount(course.getPrice()); // 使用服务端价格
        order.setQuantity(1); // 固定数量
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setShippingFee(BigDecimal.ZERO); // 在线课程无运费
        order.setFinalAmount(course.getPrice());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreateTime(LocalDateTime.now());
        
        orderRepository.save(order);
        return order;
    }
    
    /**
     * 支付订单 - 支持抓包修改金额的漏洞版本
     */
    public boolean payOrder(String orderNo, Order.PaymentMethod paymentMethod) {
        Order order = orderRepository.findByOrderNo(orderNo);
        if (order == null) {
            return false;
        }
        
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            return false;
        }
        
        // 重新获取最新的用户信息
        User user = userService.findById(order.getUserId());
        if (user == null) {
            return false;
        }
        
        // 模拟支付逻辑
        if (paymentMethod == Order.PaymentMethod.BALANCE) {
            // 漏洞：直接使用订单中的finalAmount，不验证是否被篡改
            double paymentAmount = order.getFinalAmount().doubleValue();
            
            if (user.getBalance() >= paymentAmount) {
                // 扣除用户余额
                user.setBalance(user.getBalance() - paymentAmount);
                userService.save(user);
                
                // 更新订单状态
                order.setStatus(Order.OrderStatus.PAID);
                order.setPaymentMethod(paymentMethod);
                order.setPayTime(LocalDateTime.now());
                order.setPaymentTransactionId(UUID.randomUUID().toString());
                
                // 确保订单数据更新到数据库
                orderRepository.update(order);
                
                return true;
            }
            return false;
        }
        
        // 其他支付方式模拟成功
        order.setStatus(Order.OrderStatus.PAID);
        order.setPaymentMethod(paymentMethod);
        order.setPayTime(LocalDateTime.now());
        order.setPaymentTransactionId(UUID.randomUUID().toString());
        
        // 确保订单数据更新到数据库
        orderRepository.update(order);
        
        return true;
    }
    
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserId(user.getId());
    }
    
    public Order findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }
    
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
    
    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + String.format("%04d", (int)(Math.random() * 10000));
    }
    
    public List<Order> findByUser(User user) {
        return orderRepository.findByUserId(user.getId());
    }
    
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    
    public List<Order> findByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status.name());
    }
    
    public Order save(Order order) {
        if (order.getId() == null) {
            orderRepository.save(order);
        } else {
            orderRepository.update(order);
        }
        return order;
    }
    
    /**
     * 获取用户已购买的课程ID列表
     */
    public List<Long> getPaidCourseIdsByUserId(Long userId) {
        return orderRepository.findPaidCourseIdsByUserId(userId);
    }
}