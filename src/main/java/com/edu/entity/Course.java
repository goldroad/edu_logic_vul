package com.edu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String coverImage;
    private Long teacherId; // 改为teacherId，避免复杂的关联查询
    private User teacher; // 保留teacher对象用于业务逻辑
    private CourseStatus status = CourseStatus.DRAFT;
    private Integer studentCount = 0;
    private Integer duration; // 课程时长（分钟）
    private LocalDateTime createTime = LocalDateTime.now();
    private LocalDateTime updateTime = LocalDateTime.now();
    
    public enum CourseStatus {
        DRAFT, PUBLISHED, OFFLINE
    }
}