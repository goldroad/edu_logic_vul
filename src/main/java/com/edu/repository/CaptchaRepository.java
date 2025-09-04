package com.edu.repository;

import com.edu.entity.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CaptchaRepository extends JpaRepository<Captcha, Long> {
    
    Optional<Captcha> findBySessionIdAndCodeAndUsedFalse(String sessionId, String code);
    
    Optional<Captcha> findBySessionIdAndUsedFalse(String sessionId);
    
    void deleteByExpireTimeBefore(LocalDateTime expireTime);
}