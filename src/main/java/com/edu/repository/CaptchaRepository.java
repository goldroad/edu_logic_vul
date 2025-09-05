package com.edu.repository;

import com.edu.entity.Captcha;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Mapper
public interface CaptchaRepository {
    
    @Select("SELECT * FROM captchas WHERE id = #{id}")
    Captcha findById(Long id);
    
    @Select("SELECT * FROM captchas WHERE session_id = #{sessionId} AND code = #{code} AND used = false")
    Captcha findBySessionIdAndCodeAndUsedFalse(String sessionId, String code);
    
    @Select("SELECT * FROM captchas WHERE session_id = #{sessionId} AND used = false")
    Captcha findBySessionIdAndUsedFalse(String sessionId);
    
    @Select("SELECT * FROM captchas")
    List<Captcha> findAll();
    
    @Insert("INSERT INTO captchas(session_id, code, type, target, used, create_time, expire_time) " +
            "VALUES(#{sessionId}, #{code}, #{type}, #{target}, #{used}, #{createTime}, #{expireTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Captcha captcha);
    
    @Update("UPDATE captchas SET session_id=#{sessionId}, code=#{code}, type=#{type}, target=#{target}, used=#{used}, " +
            "create_time=#{createTime}, expire_time=#{expireTime} WHERE id=#{id}")
    int update(Captcha captcha);
    
    @Delete("DELETE FROM captchas WHERE id = #{id}")
    int deleteById(Long id);
    
    @Delete("DELETE FROM captchas WHERE expire_time < #{expireTime}")
    int deleteByExpireTimeBefore(LocalDateTime expireTime);
}