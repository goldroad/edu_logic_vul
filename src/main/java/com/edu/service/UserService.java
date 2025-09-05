package com.edu.service;

import com.edu.entity.User;
import com.edu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * 用户登录验证
     */
    public String login(String username, String password) {
        User user = userRepository.findByUsernameOrEmailOrPhone(username);
        
        if (user == null) {
            return "用户名不存在";
        }
        
        if (!user.getPassword().equals(password)) {
            return "密码错误";
        }
        
        if (!user.getEnabled()) {
            return "账户已被禁用";
        }
        
        return "登录成功";
    }
    
    /**
     * 弱口令检查 - 故意设置弱口令用户
     */
    public User createWeakPasswordUser(String username) {
        // 检查用户是否已存在
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            return existingUser;
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword("123456"); // 弱口令
        user.setEmail(username + "@example.com");
        user.setPhone("13800000099");
        user.setRealName("弱口令用户");
        user.setRole(User.Role.STUDENT);
        user.setBalance(100.0);
        user.setEnabled(true);
        userRepository.save(user);
        return user;
    }
    
    /**
     * 用户注册处理
     */
    public User registerWithoutValidation(String username, String password, String email, String phone) {
        // 用户注册处理
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(User.Role.STUDENT);
        user.setEnabled(true);
        userRepository.save(user);
        return user;
    }
    
    /**
     * 忘记密码功能
     */
    public String resetPassword(String username, String newPassword) {
        // 密码重置处理
        User user = userRepository.findByUsernameOrEmailOrPhone(username);
        if (user != null) {
            user.setPassword(newPassword);
            user.setUpdateTime(LocalDateTime.now());
            userRepository.update(user);
            return "密码重置成功";
        }
        return "用户不存在";
    }
    
    /**
     * 根据ID获取用户信息
     */
    public User getUserById(Long id) {
        // 获取用户信息
        return userRepository.findById(id);
    }
    
    /**
     * 根据角色获取用户列表
     */
    public List<User> getUsersByRole(String role) {
        // 获取用户列表
        return userRepository.findAll().stream()
                .filter(user -> user.getRole().name().equals(role))
                .collect(java.util.stream.Collectors.toList());
    }
    
    public User save(User user) {
        user.setUpdateTime(LocalDateTime.now());
        if (user.getId() == null) {
            userRepository.save(user);
        } else {
            userRepository.update(user);
        }
        return user;
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public User findById(Long id) {
        return userRepository.findById(id);
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public User findByUsernameOrEmailOrPhone(String username) {
        return userRepository.findByUsernameOrEmailOrPhone(username);
    }
}