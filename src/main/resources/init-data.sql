-- 初始化数据脚本

-- 删除多余的表（如果存在）
DROP TABLE IF EXISTS user_coupon;
DROP TABLE IF EXISTS coupon;

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

-- 创建更多测试课程
INSERT IGNORE INTO course (title, description, price, original_price, teacher_id, status, create_time, update_time)
SELECT 'Java基础编程', '从零开始学习Java编程语言，掌握面向对象编程思想', 199.00, 299.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

INSERT IGNORE INTO course (title, description, price, original_price, teacher_id, status, create_time, update_time)
SELECT 'Spring Boot实战', '深入学习Spring Boot框架，快速构建企业级应用', 299.00, 399.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

INSERT IGNORE INTO course (title, description, price, original_price, teacher_id, status, create_time, update_time)
SELECT '数据库设计与优化', '学习数据库设计原理，掌握SQL优化技巧', 259.00, 359.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

INSERT IGNORE INTO course (title, description, price, original_price, teacher_id, status, create_time, update_time)
SELECT '前端开发入门', '学习HTML、CSS、JavaScript，成为前端开发工程师', 179.00, 279.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

INSERT IGNORE INTO course (title, description, price, original_price, teacher_id, status, create_time, update_time)
SELECT '网络安全基础', '了解网络安全基础知识，学习常见攻击防护方法', 399.00, 499.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

INSERT IGNORE INTO course (title, description, price, original_price, teacher_id, status, create_time, update_time)
SELECT 'Python数据分析', 'Python在数据科学领域的应用，包含pandas、numpy等库', 449.00, 549.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

INSERT IGNORE INTO course (title, description, price, original_price, teacher_id, status, create_time, update_time)
SELECT '微服务架构设计', '分布式系统与微服务架构设计模式', 799.00, 999.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

INSERT IGNORE INTO course (title, description, price, original_price, teacher_id, status, create_time, update_time)
SELECT 'Vue.js全栈开发', 'Vue.js框架全栈开发，包含前后端分离项目实战', 549.00, 649.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

INSERT IGNORE INTO course (title, description, price, original_price, teacher_id, status, create_time, update_time)
SELECT 'Docker容器技术', 'Docker容器化技术与Kubernetes集群管理', 699.00, 799.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

INSERT IGNORE INTO course (title, description, price, original_price, teacher_id, status, create_time, update_time)
SELECT '算法与数据结构', '计算机算法基础与常用数据结构详解', 359.00, 459.00, u.id, 'PUBLISHED', NOW(), NOW()
FROM user u WHERE u.username = 'teacher';

-- 创建测试订单数据
INSERT IGNORE INTO `order` (order_no, user_id, course_id, original_amount, discount_amount, shipping_fee, final_amount, quantity, status, create_time)
SELECT 
    CONCAT('ORDER', UNIX_TIMESTAMP(), '001'),
    u.id,
    c.id,
    c.price,
    50.00,
    0.00,
    c.price - 50.00,
    1,
    'PAID',
    NOW()
FROM user u, course c 
WHERE u.username = 'student' AND c.title = 'Java基础编程';

INSERT IGNORE INTO `order` (order_no, user_id, course_id, original_amount, discount_amount, shipping_fee, final_amount, quantity, status, create_time)
SELECT 
    CONCAT('ORDER', UNIX_TIMESTAMP(), '002'),
    u.id,
    c.id,
    c.price,
    0.00,
    0.00,
    c.price,
    1,
    'PENDING',
    NOW()
FROM user u, course c 
WHERE u.username = 'student' AND c.title = 'Spring Boot实战';

INSERT IGNORE INTO `order` (order_no, user_id, course_id, original_amount, discount_amount, shipping_fee, final_amount, quantity, status, create_time)
SELECT 
    CONCAT('ORDER', UNIX_TIMESTAMP(), '003'),
    u.id,
    c.id,
    c.price,
    30.00,
    -10.00,
    c.price - 30.00 - 10.00,
    2,
    'PAID',
    NOW()
FROM user u, course c 
WHERE u.username = 'user1' AND c.title = '前端开发入门';

INSERT IGNORE INTO `order` (order_no, user_id, course_id, original_amount, discount_amount, shipping_fee, final_amount, quantity, status, create_time)
SELECT 
    CONCAT('ORDER', UNIX_TIMESTAMP(), '004'),
    u.id,
    c.id,
    c.price,
    100.00,
    0.00,
    c.price - 100.00,
    1,
    'CANCELLED',
    NOW()
FROM user u, course c 
WHERE u.username = 'user2' AND c.title = '网络安全基础';

INSERT IGNORE INTO `order` (order_no, user_id, course_id, original_amount, discount_amount, shipping_fee, final_amount, quantity, status, create_time)
SELECT 
    CONCAT('ORDER', UNIX_TIMESTAMP(), '005'),
    u.id,
    c.id,
    1.00,
    0.00,
    -5.00,
    -4.00,
    -1,
    'PAID',
    NOW()
FROM user u, course c 
WHERE u.username = 'user3' AND c.title = 'Python数据分析';