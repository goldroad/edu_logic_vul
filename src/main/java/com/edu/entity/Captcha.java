package com.edu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "captchas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Captcha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String sessionId;
    
    @Column(nullable = false)
    private String code;
    
    @Enumerated(EnumType.STRING)
    private CaptchaType type;
    
    @Column(length = 10000)
    private String target; // 手机号或邮箱或图片数据
    
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean used = false;
    
    private LocalDateTime createTime = LocalDateTime.now();
    
    private LocalDateTime expireTime;
    
    public enum CaptchaType {
        IMAGE, SMS, EMAIL
    }
}