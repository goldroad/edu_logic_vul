package com.edu.controller;

import com.edu.service.SimpleCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {
    
    @Autowired
    private SimpleCaptchaService simpleCaptchaService;
    
    /**
     * 获取验证码图片
     */
    @GetMapping("/image")
    public void getCaptchaImage(HttpSession session, HttpServletResponse response) throws IOException {
        byte[] imageBytes = simpleCaptchaService.generateCaptchaImage(session.getId());
        
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        response.getOutputStream().write(imageBytes);
        response.getOutputStream().flush();
    }
}