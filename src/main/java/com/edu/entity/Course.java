package com.edu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal originalPrice;
    
    private String coverImage;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private User teacher;
    
    @Enumerated(EnumType.STRING)
    private CourseStatus status = CourseStatus.DRAFT;
    
    private Integer studentCount = 0;
    
    private Integer duration; // 课程时长（分钟）
    
    private LocalDateTime createTime = LocalDateTime.now();
    
    private LocalDateTime updateTime = LocalDateTime.now();
    
    public enum CourseStatus {
        DRAFT, PUBLISHED, OFFLINE
    }
}