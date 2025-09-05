package com.edu.controller;

import com.edu.entity.Course;
import com.edu.entity.User;
import com.edu.service.CourseService;
import com.edu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private UserService userService;
    
    /**
     * 获取已发布的课程列表
     */
    @GetMapping("/published")
    public Map<String, Object> getPublishedCourses() {
        Map<String, Object> response = new HashMap<>();
        
        List<Course> courses = courseService.getPublishedCourses();
        
        response.put("success", true);
        response.put("courses", courses);
        
        return response;
    }
    
    /**
     * 获取课程详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> getCourseDetail(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Course course = courseService.findById(id);
        
        if (course != null) {
            response.put("success", true);
            response.put("course", course);
        } else {
            response.put("success", false);
            response.put("message", "课程不存在");
        }
        
        return response;
    }
    
    /**
     * 创建课程
     */
    @PostMapping("/create")
    public Map<String, Object> createCourse(@RequestBody Map<String, Object> request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return response;
        }
        
        // 简单权限检查（仍有漏洞）
        if (currentUser.getRole() == User.Role.STUDENT) {
            response.put("success", false);
            response.put("message", "学生无权创建课程");
            return response;
        }
        
        try {
            String title = (String) request.get("title");
            String description = (String) request.get("description");
            BigDecimal price = new BigDecimal(request.get("price").toString());
            
            Course course = courseService.createCourse(title, description, price, currentUser);
            
            response.put("success", true);
            response.put("message", "课程创建成功");
            response.put("course", course);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建失败: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * 获取我的课程
     */
    @GetMapping("/my")
    public Map<String, Object> getMyCourses(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return response;
        }
        
        List<Course> courses = courseService.getCoursesByTeacher(currentUser);
        
        response.put("success", true);
        response.put("courses", courses);
        
        return response;
    }
    
    /**
     * 发布课程
     */
    @PostMapping("/{id}/publish")
    public Map<String, Object> publishCourse(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "请先登录");
            return response;
        }
        
        Course course = courseService.findById(id);
        if (course == null) {
            response.put("success", false);
            response.put("message", "课程不存在");
            return response;
        }
        
        // 漏洞：不验证课程是否属于当前用户
        courseService.publishCourse(id);
        
        response.put("success", true);
        response.put("message", "课程发布成功");
        
        return response;
    }
    
    /**
     * 搜索课程
     */
    @GetMapping("/search")
    public Map<String, Object> searchCourses(@RequestParam String keyword) {
        Map<String, Object> response = new HashMap<>();
        
        List<Course> courses = courseService.searchCourses(keyword);
        
        response.put("success", true);
        response.put("courses", courses);
        response.put("keyword", keyword);
        
        return response;
    }
    
    /**
     * 获取所有课程 - 未授权访问
     */
    @GetMapping("/all")
    public Map<String, Object> getAllCourses() {
        Map<String, Object> response = new HashMap<>();
        
        // 漏洞：接口未鉴权，可以查看所有课程（包括草稿状态）
        List<Course> courses = courseService.findAll();
        
        response.put("success", true);
        response.put("courses", courses);
        
        return response;
    }
}