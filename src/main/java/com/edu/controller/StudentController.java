package com.edu.controller;

import com.edu.entity.Course;
import com.edu.entity.Order;
import com.edu.entity.User;
import com.edu.service.CourseService;
import com.edu.service.OrderService;
import com.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 购买课程 - 创建订单
     */
    @PostMapping("/buy-course")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> buyCourse(@RequestBody Map<String, Object> request,
                                                        HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return ResponseEntity.ok(response);
        }
        
        try {
            Long courseId = Long.valueOf(request.get("courseId").toString());
            Course course = courseService.findById(courseId);
            
            if (course == null) {
                response.put("success", false);
                response.put("message", "课程不存在");
                return ResponseEntity.ok(response);
            }
            
            // 创建订单（使用正确的价格，不允许客户端修改）
            Order order = orderService.createOrderSecure(user.getId(), courseId);
            
            response.put("success", true);
            response.put("message", "订单创建成功");
            response.put("orderNo", order.getOrderNo());
            response.put("amount", order.getFinalAmount());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建订单失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 支付页面
     */
    @GetMapping("/payment/{orderNo}")
    public String paymentPage(@PathVariable String orderNo, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        Order order = orderService.findByOrderNo(orderNo);
        if (order == null) {
            model.addAttribute("error", "订单不存在");
            return "student/orders";
        }
        
        // 检查订单是否属于当前用户
        if (!order.getUserId().equals(user.getId())) {
            model.addAttribute("error", "无权访问此订单");
            return "student/orders";
        }
        
        // 检查订单状态
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            model.addAttribute("error", "订单状态不正确");
            return "student/orders";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("order", order);
        return "student/payment";
    }
    
    /**
     * 确认支付
     */
    @PostMapping("/confirm-payment")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> confirmPayment(@RequestBody Map<String, String> request,
                                                             HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return ResponseEntity.ok(response);
        }
        
        try {
            String orderNo = request.get("orderNo");
            String paymentMethod = request.get("paymentMethod");
            
            Order.PaymentMethod method = Order.PaymentMethod.valueOf(paymentMethod.toUpperCase());
            boolean paymentResult = orderService.payOrder(orderNo, method);
            
            if (paymentResult) {
                response.put("success", true);
                response.put("message", "支付成功");
            } else {
                response.put("success", false);
                response.put("message", "支付失败，余额不足或订单状态异常");
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "支付失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}