package com.edu.controller;

import com.edu.entity.User;
import com.edu.entity.Course;
import com.edu.entity.Order;
import com.edu.service.UserService;
import com.edu.service.CourseService;
import com.edu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PageController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 首页
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    /**
     * 显示登录页面
     */
    @GetMapping("/auth/login")
    public String loginPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // 已登录用户根据角色跳转到对应的dashboard
            switch (user.getRole()) {
                case ADMIN:
                    return "redirect:/admin/dashboard";
                case TEACHER:
                    return "redirect:/teacher/dashboard";
                case STUDENT:
                    return "redirect:/student/dashboard";
                default:
                    return "redirect:/student/dashboard";
            }
        }
        return "login";
    }
    
    /**
     * 显示注册页面
     */
    @GetMapping("/auth/register")
    public String registerPage() {
        return "register";
    }
    
    /**
     * 显示忘记密码页面
     */
    @GetMapping("/auth/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }
    
    /**
     * 学生仪表板
     */
    @GetMapping("/student/dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/auth/login";
        }
        
        // 从数据库获取最新的用户信息，确保余额是最新的
        User user = userService.findById(sessionUser.getId());
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        // 获取推荐课程
        List<Course> courses = courseService.getPublishedCourses();
        
        model.addAttribute("user", user);
        model.addAttribute("courses", courses);
        return "student/dashboard";
    }
    

    
    /**
     * 管理员仪表板
     */
    @GetMapping("/admin/dashboard")
    public String adminDashboard(@RequestParam(required = false) String role,
                                HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        // 用户角色验证
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        // 如果URL参数中包含role=ADMIN，就允许访问（漏洞）
        if ("ADMIN".equals(role) || (user.getRole() == User.Role.ADMIN)) {
            model.addAttribute("user", user);
            return "admin/dashboard";
        }
        
        // 显示管理页面
        model.addAttribute("user", user);
        return "admin/dashboard";
    }
    
    /**
     * 教师仪表板
     */
    @GetMapping("/teacher/dashboard")
    public String teacherDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "teacher/dashboard";
    }
    
    /**
     * 学生课程页面
     */
    @GetMapping("/student/courses")
    public String studentCourses(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        List<Course> courses = courseService.getPublishedCourses();
        model.addAttribute("user", user);
        model.addAttribute("courses", courses);
        return "student/courses";
    }
    
    /**
     * 学生订单页面
     */
    @GetMapping("/student/orders")
    public String studentOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        // 获取用户的订单列表
        List<Order> orders = orderService.findByUser(user);
        
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        return "student/orders";
    }
    
    /**
     * 学生优惠券页面
     */
    @GetMapping("/student/coupons")
    public String studentCoupons(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "student/coupons";
    }
    
    /**
     * 学生支付页面
     */
    @GetMapping("/student/payment")
    public String studentPayment(@RequestParam String orderNo, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        // 获取订单信息
        Order order = orderService.findByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(user.getId())) {
            model.addAttribute("error", "订单不存在或无权访问");
            return "student/orders";
        }
        
        // 检查订单状态
        if (order.getStatus() != Order.OrderStatus.PENDING) {
            model.addAttribute("error", "订单状态不正确，无法支付");
            return "student/orders";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("order", order);
        return "student/payment";
    }
    
    /**
     * 学生文件管理页面
     */
    @GetMapping("/student/files")
    public String studentFiles(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "student/files";
    }
    
    // ==================== 管理员后台页面 ====================
    
    /**
     * 管理员资料页面
     */
    @GetMapping("/admin/profile")
    public String adminProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "admin/profile";
    }
    
    /**
     * 系统设置页面
     */
    @GetMapping("/admin/settings")
    public String adminSettings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "admin/settings";
    }
    
    /**
     * 课程管理页面
     */
    @GetMapping("/admin/courses")
    public String adminCourses(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        List<Course> courses = courseService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("courses", courses);
        return "admin/courses";
    }
    
    /**
     * 订单管理页面
     */
    @GetMapping("/admin/orders")
    public String adminOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "admin/orders";
    }
    
    /**
     * 优惠券管理页面
     */
    @GetMapping("/admin/coupons")
    public String adminCoupons(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "admin/coupons";
    }
    
    /**
     * 财务报表页面（机密）
     */
    @GetMapping("/admin/financial")
    public String adminFinancial(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "admin/financial";
    }
    
    /**
     * 系统监控页面（高危）
     */
    @GetMapping("/admin/system")
    public String adminSystem(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "admin/system";
    }
    
    /**
     * 系统日志页面（敏感）
     */
    @GetMapping("/admin/logs")
    public String adminLogs(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "admin/logs";
    }
    
    // ==================== 教师工作台页面 ====================
    
    /**
     * 教师课程管理页面
     */
    @GetMapping("/teacher/courses")
    public String teacherCourses(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        List<Course> courses = courseService.getCoursesByTeacher(user);
        model.addAttribute("user", user);
        model.addAttribute("courses", courses);
        return "teacher/courses";
    }
    
    /**
     * 学生管理页面
     */
    @GetMapping("/teacher/students")
    public String teacherStudents(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "teacher/students";
    }
    
    /**
     * 作业管理页面
     */
    @GetMapping("/teacher/assignments")
    public String teacherAssignments(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "teacher/assignments";
    }
    
    /**
     * 成绩管理页面
     */
    @GetMapping("/teacher/grades")
    public String teacherGrades(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "teacher/grades";
    }
    
    /**
     * 教学资源页面
     */
    @GetMapping("/teacher/resources")
    public String teacherResources(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "teacher/resources";
    }
    
    /**
     * 课程安排页面
     */
    @GetMapping("/teacher/schedule")
    public String teacherSchedule(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "teacher/schedule";
    }
    
    /**
     * 教学分析页面
     */
    @GetMapping("/teacher/analytics")
    public String teacherAnalytics(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("user", user);
        return "teacher/analytics";
    }
    
    // ==================== 管理员高级功能页面 ====================
    
    /**
     * 用户管理页面
     */
    @GetMapping("/admin/users")
    public String adminUsers(@RequestParam(required = false) String role,
                           @RequestParam(required = false) String status,
                           HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        
        // 获取用户统计信息
        UserService.UserStatistics statistics = userService.getUserStatistics();
        
        // 根据筛选条件获取用户列表
        Boolean enabled = null;
        if ("active".equals(status)) {
            enabled = true;
        } else if ("inactive".equals(status)) {
            enabled = false;
        }
        
        List<User> users = userService.findUsersByRoleAndStatus(role, enabled);
        
        model.addAttribute("user", user);
        model.addAttribute("statistics", statistics);
        model.addAttribute("users", users);
        model.addAttribute("selectedRole", role);
        model.addAttribute("selectedStatus", status);
        return "admin/users";
    }
}