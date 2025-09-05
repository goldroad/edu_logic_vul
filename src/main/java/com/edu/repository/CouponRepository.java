package com.edu.repository;

import com.edu.entity.Coupon;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Mapper
public interface CouponRepository {
    
    @Select("SELECT * FROM coupon WHERE id = #{id}")
    Coupon findById(Long id);
    
    @Select("SELECT * FROM coupon WHERE code = #{code}")
    Coupon findByCode(String code);
    
    @Select("SELECT * FROM coupon WHERE enabled = true AND start_time <= #{now} AND end_time >= #{now} AND used_count < total_count")
    List<Coupon> findAvailableCoupons(LocalDateTime now);
    
    @Select("SELECT * FROM coupon")
    List<Coupon> findAll();
    
    @Insert("INSERT INTO coupon(name, code, type, discount_value, min_amount, total_count, used_count, start_time, end_time, enabled, create_time) " +
            "VALUES(#{name}, #{code}, #{type}, #{discountValue}, #{minAmount}, #{totalCount}, #{usedCount}, #{startTime}, #{endTime}, #{enabled}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Coupon coupon);
    
    @Update("UPDATE coupon SET name=#{name}, code=#{code}, type=#{type}, discount_value=#{discountValue}, min_amount=#{minAmount}, " +
            "total_count=#{totalCount}, used_count=#{usedCount}, start_time=#{startTime}, end_time=#{endTime}, enabled=#{enabled} " +
            "WHERE id=#{id}")
    int update(Coupon coupon);
    
    @Delete("DELETE FROM coupon WHERE id = #{id}")
    int deleteById(Long id);
}