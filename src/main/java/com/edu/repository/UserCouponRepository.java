package com.edu.repository;

import com.edu.entity.UserCoupon;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserCouponRepository {
    
    @Select("SELECT * FROM user_coupon WHERE id = #{id}")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "couponId", column = "coupon_id"),
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "user", column = "user_id", 
                one = @One(select = "com.edu.repository.UserRepository.findById")),
        @Result(property = "coupon", column = "coupon_id", 
                one = @One(select = "com.edu.repository.CouponRepository.findById")),
        @Result(property = "order", column = "order_id", 
                one = @One(select = "com.edu.repository.OrderRepository.findById"))
    })
    UserCoupon findById(Long id);
    
    @Select("SELECT * FROM user_coupon WHERE user_id = #{userId}")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "couponId", column = "coupon_id"),
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "user", column = "user_id", 
                one = @One(select = "com.edu.repository.UserRepository.findById")),
        @Result(property = "coupon", column = "coupon_id", 
                one = @One(select = "com.edu.repository.CouponRepository.findById")),
        @Result(property = "order", column = "order_id", 
                one = @One(select = "com.edu.repository.OrderRepository.findById"))
    })
    List<UserCoupon> findByUserId(Long userId);
    
    @Select("SELECT * FROM user_coupon WHERE user_id = #{userId} AND status = #{status}")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "couponId", column = "coupon_id"),
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "user", column = "user_id", 
                one = @One(select = "com.edu.repository.UserRepository.findById")),
        @Result(property = "coupon", column = "coupon_id", 
                one = @One(select = "com.edu.repository.CouponRepository.findById")),
        @Result(property = "order", column = "order_id", 
                one = @One(select = "com.edu.repository.OrderRepository.findById"))
    })
    List<UserCoupon> findByUserIdAndStatus(Long userId, String status);
    
    @Select("SELECT * FROM user_coupon WHERE user_id = #{userId} AND coupon_id = #{couponId}")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "couponId", column = "coupon_id"),
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "user", column = "user_id", 
                one = @One(select = "com.edu.repository.UserRepository.findById")),
        @Result(property = "coupon", column = "coupon_id", 
                one = @One(select = "com.edu.repository.CouponRepository.findById")),
        @Result(property = "order", column = "order_id", 
                one = @One(select = "com.edu.repository.OrderRepository.findById"))
    })
    UserCoupon findByUserIdAndCouponId(Long userId, Long couponId);
    
    @Select("SELECT COUNT(*) > 0 FROM user_coupon WHERE user_id = #{userId} AND coupon_id = #{couponId}")
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);
    
    @Select("SELECT * FROM user_coupon")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "couponId", column = "coupon_id"),
        @Result(property = "orderId", column = "order_id"),
        @Result(property = "user", column = "user_id", 
                one = @One(select = "com.edu.repository.UserRepository.findById")),
        @Result(property = "coupon", column = "coupon_id", 
                one = @One(select = "com.edu.repository.CouponRepository.findById")),
        @Result(property = "order", column = "order_id", 
                one = @One(select = "com.edu.repository.OrderRepository.findById"))
    })
    List<UserCoupon> findAll();
    
    @Insert("INSERT INTO user_coupon(user_id, coupon_id, status, receive_time, use_time, order_id) " +
            "VALUES(#{userId}, #{couponId}, #{status}, #{receiveTime}, #{useTime}, #{orderId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(UserCoupon userCoupon);
    
    @Update("UPDATE user_coupon SET user_id=#{userId}, coupon_id=#{couponId}, status=#{status}, " +
            "receive_time=#{receiveTime}, use_time=#{useTime}, order_id=#{orderId} WHERE id=#{id}")
    int update(UserCoupon userCoupon);
    
    @Delete("DELETE FROM user_coupon WHERE id = #{id}")
    int deleteById(Long id);
}