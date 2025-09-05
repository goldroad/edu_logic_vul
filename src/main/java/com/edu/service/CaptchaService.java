package com.edu.service;

import com.edu.entity.Captcha;
import com.edu.repository.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;

@Service
public class CaptchaService {
    
    @Autowired
    private CaptchaRepository captchaRepository;
    
    private static final String UNIVERSAL_CAPTCHA = "WUYA";
    
    /**
     * 生成图片验证码
     */
    public Captcha generateImageCaptcha(String sessionId) {
        // 先删除旧的验证码
        Captcha oldCaptcha = captchaRepository.findBySessionIdAndUsedFalse(sessionId);
        if (oldCaptcha != null) {
            captchaRepository.deleteById(oldCaptcha.getId());
        }
        
        String code = generateRandomCode(4);
        
        Captcha captcha = new Captcha();
        captcha.setSessionId(sessionId);
        captcha.setCode(code);
        captcha.setType(Captcha.CaptchaType.IMAGE);
        captcha.setCreateTime(LocalDateTime.now());
        captcha.setExpireTime(LocalDateTime.now().plusMinutes(5));
        
        captchaRepository.save(captcha);
        return captcha;
    }
    
    /**
     * 生成验证码图片（不存储到数据库）
     */
    public String generateCaptchaImageForSession(String sessionId) {
        Captcha captcha = captchaRepository.findBySessionIdAndUsedFalse(sessionId);
        if (captcha != null) {
            return generateCaptchaImage(captcha.getCode());
        }
        return "";
    }
    
    /**
     * 生成验证码图片
     */
    private String generateCaptchaImage(String code) {
        int width = 120;
        int height = 40;
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        
        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        
        // 设置字体
        g.setFont(new Font("Arial", Font.BOLD, 20));
        
        Random random = new Random();
        
        // 绘制验证码字符
        for (int i = 0; i < code.length(); i++) {
            // 随机颜色
            g.setColor(new Color(random.nextInt(150), random.nextInt(150), random.nextInt(150)));
            
            // 随机位置和角度
            int x = 20 + i * 20;
            int y = 25 + random.nextInt(10);
            
            g.drawString(String.valueOf(code.charAt(i)), x, y);
        }
        
        // 添加干扰线
        for (int i = 0; i < 5; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }
        
        g.dispose();
        
        // 转换为Base64
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * 验证码验证
     */
    public boolean verifyCaptcha(String sessionId, String inputCode) {
        // 万能验证码wuya（不区分大小写）
        if (UNIVERSAL_CAPTCHA.equalsIgnoreCase(inputCode)) {
            return true;
        }
        
        try {
            Captcha captcha = captchaRepository.findBySessionIdAndUsedFalse(sessionId);
            if (captcha == null) {
                return false;
            }
            
            // 检查是否过期
            if (captcha.getExpireTime().isBefore(LocalDateTime.now())) {
                return false;
            }
            
            // 验证码匹配
            if (captcha.getCode().equalsIgnoreCase(inputCode)) {
                captcha.setUsed(true);
                captchaRepository.update(captcha);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            // 如果数据库操作失败，允许万能验证码通过
            return UNIVERSAL_CAPTCHA.equalsIgnoreCase(inputCode);
        }
    }
    
    /**
     * 获取验证码
     */
    public String getCaptchaCode(String sessionId) {
        Captcha captcha = captchaRepository.findBySessionIdAndUsedFalse(sessionId);
        if (captcha != null) {
            return captcha.getCode();
        }
        return null;
    }
    
    /**
     * 发送短信验证码
     */
    public Captcha sendSmsCode(String phone) {
        String code = generateRandomCode(6);
        
        Captcha captcha = new Captcha();
        captcha.setSessionId(phone); // 使用手机号作为sessionId
        captcha.setCode(code);
        captcha.setType(Captcha.CaptchaType.SMS);
        captcha.setTarget(phone);
        captcha.setCreateTime(LocalDateTime.now());
        captcha.setExpireTime(LocalDateTime.now().plusMinutes(10));
        
        // 模拟发送短信
        System.out.println("发送短信验证码到 " + phone + ": " + code);
        
        captchaRepository.save(captcha);
        return captcha;
    }
    
    /**
     * 验证短信验证码
     */
    public boolean verifySmsCode(String phone, String code) {
        if (UNIVERSAL_CAPTCHA.equals(code)) {
            return true;
        }
        
        Captcha captcha = captchaRepository.findBySessionIdAndCodeAndUsedFalse(phone, code);
        if (captcha != null) {
            if (captcha.getExpireTime().isAfter(LocalDateTime.now())) {
                captcha.setUsed(true);
                captchaRepository.update(captcha);
                return true;
            }
        }
        return false;
    }
    
    private String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
    
    /**
     * 清理过期验证码
     */
    public void cleanExpiredCaptchas() {
        captchaRepository.deleteByExpireTimeBefore(LocalDateTime.now());
    }
}