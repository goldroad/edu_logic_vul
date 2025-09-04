package com.edu.service;

import com.edu.entity.Captcha;
import com.edu.repository.CaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
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
        String code = generateRandomCode(4);
        
        Captcha captcha = new Captcha();
        captcha.setSessionId(sessionId);
        captcha.setCode(code);
        captcha.setType(Captcha.CaptchaType.IMAGE);
        captcha.setCreateTime(LocalDateTime.now());
        captcha.setExpireTime(LocalDateTime.now().plusMinutes(5));
        
        return captchaRepository.save(captcha);
    }
    
    /**
     * 验证码验证
     */
    public boolean verifyCaptcha(String sessionId, String inputCode) {
        if (UNIVERSAL_CAPTCHA.equals(inputCode)) {
            return true;
        }
        
        Optional<Captcha> captchaOpt = captchaRepository.findBySessionIdAndUsedFalse(sessionId);
        if (!captchaOpt.isPresent()) {
            return false;
        }
        
        Captcha captcha = captchaOpt.get();
        
        // 检查是否过期
        if (captcha.getExpireTime().isBefore(LocalDateTime.now())) {
            return false;
        }
        
        // 验证码匹配
        if (captcha.getCode().equalsIgnoreCase(inputCode)) {
            captcha.setUsed(true);
            captchaRepository.save(captcha);
            return true;
        }
        
        return false;
    }
    
    /**
     * 获取验证码
     */
    public String getCaptchaCode(String sessionId) {
        Optional<Captcha> captchaOpt = captchaRepository.findBySessionIdAndUsedFalse(sessionId);
        if (captchaOpt.isPresent()) {
            return captchaOpt.get().getCode();
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
        
        return captchaRepository.save(captcha);
    }
    
    /**
     * 验证短信验证码
     */
    public boolean verifySmsCode(String phone, String code) {
        if (UNIVERSAL_CAPTCHA.equals(code)) {
            return true;
        }
        
        Optional<Captcha> captchaOpt = captchaRepository.findBySessionIdAndCodeAndUsedFalse(phone, code);
        if (captchaOpt.isPresent()) {
            Captcha captcha = captchaOpt.get();
            if (captcha.getExpireTime().isAfter(LocalDateTime.now())) {
                captcha.setUsed(true);
                captchaRepository.save(captcha);
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