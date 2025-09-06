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
     * 个人资料页面 - 支持通过ID查询用户信息
     */
    @GetMapping("/profile")
    public String profile(@RequestParam(required = false) Long id, 
                         HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/auth/login";
        }
        
        User targetUser;
        if (id != null) {
            // 根据ID从数据库查询用户信息
            targetUser = userService.findById(id);
            if (targetUser == null) {
                targetUser = currentUser; // 如果查询不到，显示当前用户信息
            }
        } else {
            targetUser = currentUser;
        }
        
        model.addAttribute("user", currentUser);
        model.addAttribute("targetUser", targetUser);
        model.addAttribute("isOwnProfile", targetUser.getId().equals(currentUser.getId()));
        return "student/profile";
    }
    
    /**
     * 更新个人资料
     */
    @PostMapping("/update-profile")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Map<String, Object> request,
                                                            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return ResponseEntity.ok(response);
        }
        
        try {
            // 从数据库获取最新用户信息
            User currentUser = userService.findById(user.getId());
            if (currentUser == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.ok(response);
            }
            
            // 更新用户信息
            if (request.containsKey("realName")) {
                currentUser.setRealName(request.get("realName").toString());
            }
            if (request.containsKey("email")) {
                currentUser.setEmail(request.get("email").toString());
            }
            if (request.containsKey("phone")) {
                currentUser.setPhone(request.get("phone").toString());
            }
            
            // 保存更新
            userService.save(currentUser);
            
            // 更新session中的用户信息
            session.setAttribute("user", currentUser);
            
            response.put("success", true);
            response.put("message", "资料更新成功");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, Object> request,
                                                             HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return ResponseEntity.ok(response);
        }
        
        try {
            String currentPassword = request.get("currentPassword").toString();
            String newPassword = request.get("newPassword").toString();
            String confirmPassword = request.get("confirmPassword").toString();
            
            // 从数据库获取最新用户信息
            User currentUser = userService.findById(user.getId());
            if (currentUser == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.ok(response);
            }
            
            // 验证当前密码
            if (!currentUser.getPassword().equals(currentPassword)) {
                response.put("success", false);
                response.put("message", "当前密码错误");
                return ResponseEntity.ok(response);
            }
            
            // 验证新密码确认
            if (!newPassword.equals(confirmPassword)) {
                response.put("success", false);
                response.put("message", "新密码与确认密码不匹配");
                return ResponseEntity.ok(response);
            }
            
            // 密码长度验证
            if (newPassword.length() < 6) {
                response.put("success", false);
                response.put("message", "新密码长度至少6位");
                return ResponseEntity.ok(response);
            }
            
            // 更新密码
            currentUser.setPassword(newPassword);
            userService.save(currentUser);
            
            // 更新session中的用户信息
            session.setAttribute("user", currentUser);
            
            response.put("success", true);
            response.put("message", "密码修改成功");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "修改失败: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
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
     * 确认支付 - 支持抓包修改金额的漏洞版本
     */
    @PostMapping("/confirm-payment")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> confirmPayment(@RequestBody Map<String, Object> request,
                                                             HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return ResponseEntity.ok(response);
        }
        
        try {
            String orderNo = request.get("orderNo").toString();
            String paymentMethod = request.get("paymentMethod").toString();
            
            // 漏洞：如果请求中包含金额，直接修改订单金额
            if (request.containsKey("amount")) {
                BigDecimal clientAmount = new BigDecimal(request.get("amount").toString());
                Order order = orderService.findByOrderNo(orderNo);
                if (order != null && order.getStatus() == Order.OrderStatus.PENDING) {
                    order.setFinalAmount(clientAmount);
                    orderService.save(order);
                }
            }
            
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