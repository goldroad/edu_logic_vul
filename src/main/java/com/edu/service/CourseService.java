package com.edu.service;

import com.edu.entity.Course;
import com.edu.entity.User;
import com.edu.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    public Course createCourse(String title, String description, BigDecimal price, User teacher) {
        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setPrice(price);
        course.setOriginalPrice(price);
        course.setTeacherId(teacher.getId());
        course.setTeacher(teacher);
        course.setStatus(Course.CourseStatus.DRAFT);
        course.setStudentCount(0);
        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());
        
        courseRepository.save(course);
        return course;
    }
    
    public List<Course> getPublishedCourses() {
        return courseRepository.findByStatus("PUBLISHED");
    }
    
    public List<Course> getCoursesByTeacher(User teacher) {
        return courseRepository.findByTeacherId(teacher.getId());
    }
    
    public List<Course> searchCourses(String keyword) {
        return courseRepository.findByTitleContaining(keyword);
    }
    
    public Course findById(Long id) {
        return courseRepository.findById(id);
    }
    
    public Course save(Course course) {
        course.setUpdateTime(LocalDateTime.now());
        if (course.getId() == null) {
            courseRepository.save(course);
        } else {
            courseRepository.update(course);
        }
        return course;
    }
    
    public List<Course> findAll() {
        return courseRepository.findAll();
    }
    
    public void publishCourse(Long courseId) {
        Course course = courseRepository.findById(courseId);
        if (course != null) {
            course.setStatus(Course.CourseStatus.PUBLISHED);
            courseRepository.update(course);
        }
    }
}