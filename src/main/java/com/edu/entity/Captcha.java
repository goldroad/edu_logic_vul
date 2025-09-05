package com.edu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Captcha {
    private Long id;
    private String sessionId;
    private String code;
    private CaptchaType type;
    private String target; // 手机号或邮箱或图片数据
    private Boolean used = false;
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime expireTime;
    
    public enum CaptchaType {
        IMAGE, SMS, EMAIL
    }
}