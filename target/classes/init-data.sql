-- 初始化数据脚本
-- 创建管理员用户
INSERT IGNORE INTO user (username, password, email, phone, real_name, role, balance, enabled, create_time, update_time) 
VALUES ('admin', 'admin123', 'admin@edu.com', '13800000001', '系统管理员', 'ADMIN', 10000.0, true, NOW(), NOW());

-- 创建教师用户
INSERT IGNORE INTO user (username, password, email, phone, real_name, role, balance, enabled, create_time, update_time) 
VALUES ('teacher', 'teacher123', 'teacher@edu.com', '13800000010', '张老师', 'TEACHER', 5000.0, true, NOW(), NOW());

-- 创建学生用户
INSERT IGNORE INTO user (username, password, email, phone, real_name, role, balance, enabled, create_time, update_time) 
VALUES ('student', 'student123', 'student@edu.com', '13800000020', '李同学', 'STUDENT', 1000.0, true, NOW(), NOW());

-- 创建弱口令用户
INSERT IGNORE INTO user (username, password, email, phone, real_name, role, balance, enabled, create_time, update_time) 
VALUES ('weakuser', '123456', 'weakuser@test.com', '13800001000', '弱口令用户', 'STUDENT', 500.0, true, NOW(), NOW());

-- 创建测试用户
INSERT IGNORE INTO user (username, password, email, phone, real_name, role, balance, enabled, create_time, update_time) 
VALUES 
('user1', '123456', 'user1@test.com', '13800000101', '测试用户1', 'STUDENT', 500.0, true, NOW(), NOW()),
('user2', '123456', 'user2@test.com', '13800000102', '测试用户2', 'STUDENT', 500.0, true, NOW(), NOW()),
('user3', '123456', 'user3@test.com', '13800000103', '测试用户3', 'STUDENT', 500.0, true, NOW(), NOW()),
('user4', '123456', 'user4@test.com', '13800000104', '测试用户4', 'STUDENT', 500.0, true, NOW(), NOW()),
('user5', '123456', 'user5@test.com', '13800000105', '测试用户5', 'STUDENT', 500.0, true, NOW(), NOW());

-- 创建测试课程
INSERT IGNORE INTO course (title, description, price, teacher_id, status, create_time, update_time)
SELECT 'Java基础编程', '从零开始学习Java编程语言，掌握面向对象编程思想', 199.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

INSERT IGNORE INTO course (title, description, price, teacher_id, status, create_time, update_time)
SELECT 'Spring Boot实战', '深入学习Spring Boot框架，快速构建企业级应用', 299.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

INSERT IGNORE INTO course (title, description, price, teacher_id, status, create_time, update_time)
SELECT '数据库设计与优化', '学习数据库设计原理，掌握SQL优化技巧', 259.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

INSERT IGNORE INTO course (title, description, price, teacher_id, status, create_time, update_time)
SELECT '前端开发入门', '学习HTML、CSS、JavaScript，成为前端开发工程师', 179.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

INSERT IGNORE INTO course (title, description, price, teacher_id, status, create_time, update_time)
SELECT '网络安全基础', '了解网络安全基础知识，学习常见攻击防护方法', 399.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

-- 创建测试优惠券
INSERT IGNORE INTO coupon (name, code, type, discount_value, min_amount, total_count, used_count, start_time, end_time, create_time)
VALUES 
('新用户专享', 'NEW50', 'FIXED', 50.00, 100.00, 100, 0, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NOW()),
('限时8折', 'DISCOUNT20', 'PERCENT', 0.2, 200.00, 50, 0, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NOW()),
('并发测试券', 'CONCURRENT', 'FIXED', 10.00, 50.00, 5, 0, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NOW());