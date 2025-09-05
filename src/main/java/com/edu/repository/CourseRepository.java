package com.edu.repository;

import com.edu.entity.Course;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CourseRepository {
    
    @Select("SELECT * FROM course WHERE id = #{id}")
    @Results({
        @Result(property = "teacherId", column = "teacher_id"),
        @Result(property = "teacher", column = "teacher_id", 
                one = @One(select = "com.edu.repository.UserRepository.findById"))
    })
    Course findById(Long id);
    
    @Select("SELECT * FROM course WHERE status = #{status}")
    @Results({
        @Result(property = "teacherId", column = "teacher_id"),
        @Result(property = "teacher", column = "teacher_id", 
                one = @One(select = "com.edu.repository.UserRepository.findById"))
    })
    List<Course> findByStatus(String status);
    
    @Select("SELECT * FROM course WHERE teacher_id = #{teacherId}")
    @Results({
        @Result(property = "teacherId", column = "teacher_id"),
        @Result(property = "teacher", column = "teacher_id", 
                one = @One(select = "com.edu.repository.UserRepository.findById"))
    })
    List<Course> findByTeacherId(Long teacherId);
    
    @Select("SELECT * FROM course WHERE title LIKE CONCAT('%', #{title}, '%')")
    @Results({
        @Result(property = "teacherId", column = "teacher_id"),
        @Result(property = "teacher", column = "teacher_id", 
                one = @One(select = "com.edu.repository.UserRepository.findById"))
    })
    List<Course> findByTitleContaining(String title);
    
    @Select("SELECT * FROM course")
    @Results({
        @Result(property = "teacherId", column = "teacher_id"),
        @Result(property = "teacher", column = "teacher_id", 
                one = @One(select = "com.edu.repository.UserRepository.findById"))
    })
    List<Course> findAll();
    
    @Insert("INSERT INTO course(title, description, price, original_price, cover_image, teacher_id, status, student_count, duration, create_time, update_time) " +
            "VALUES(#{title}, #{description}, #{price}, #{originalPrice}, #{coverImage}, #{teacherId}, #{status}, #{studentCount}, #{duration}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Course course);
    
    @Update("UPDATE course SET title=#{title}, description=#{description}, price=#{price}, original_price=#{originalPrice}, " +
            "cover_image=#{coverImage}, teacher_id=#{teacherId}, status=#{status}, student_count=#{studentCount}, " +
            "duration=#{duration}, update_time=#{updateTime} WHERE id=#{id}")
    int update(Course course);
    
    @Delete("DELETE FROM course WHERE id = #{id}")
    int deleteById(Long id);
}