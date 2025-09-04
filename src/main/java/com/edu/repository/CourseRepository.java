package com.edu.repository;

import com.edu.entity.Course;
import com.edu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByStatus(Course.CourseStatus status);
    
    List<Course> findByTeacher(User teacher);
    
    List<Course> findByTitleContaining(String title);
}