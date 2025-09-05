-- 插入测试用户数据
INSERT INTO users (username, password, email, role) VALUES
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'admin@edu.com', 'ADMIN'),
('teacher1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'teacher1@edu.com', 'TEACHER'),
('student1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'student1@edu.com', 'STUDENT'),
('student2', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'student2@edu.com', 'STUDENT');

-- 插入测试课程数据
INSERT INTO courses (title, description, instructor, price, duration, level, category, image_url) VALUES
('Java基础编程', 'Java编程语言基础课程，适合初学者学习面向对象编程', '张老师', 299.00, 40, 'BEGINNER', '编程语言', '/images/java-basic.jpg'),
('Spring Boot实战', '深入学习Spring Boot框架，构建企业级Web应用', '李老师', 599.00, 60, 'INTERMEDIATE', '后端开发', '/images/springboot.jpg'),
('前端React开发', 'React框架从入门到精通，构建现代化前端应用', '王老师', 499.00, 50, 'INTERMEDIATE', '前端开发', '/images/react.jpg'),
('数据库设计与优化', 'MySQL数据库设计原理与性能优化实践', '陈老师', 399.00, 35, 'INTERMEDIATE', '数据库', '/images/mysql.jpg'),
('Python数据分析', 'Python在数据科学领域的应用，包含pandas、numpy等库', '刘老师', 449.00, 45, 'BEGINNER', '数据科学', '/images/python-data.jpg'),
('微服务架构设计', '分布式系统与微服务架构设计模式', '赵老师', 799.00, 80, 'ADVANCED', '系统架构', '/images/microservice.jpg'),
('Vue.js全栈开发', 'Vue.js框架全栈开发，包含前后端分离项目实战', '孙老师', 549.00, 55, 'INTERMEDIATE', '前端开发', '/images/vue.jpg'),
('Docker容器技术', 'Docker容器化技术与Kubernetes集群管理', '周老师', 699.00, 65, 'ADVANCED', '运维技术', '/images/docker.jpg'),
('算法与数据结构', '计算机算法基础与常用数据结构详解', '吴老师', 359.00, 42, 'BEGINNER', '算法', '/images/algorithm.jpg'),
('网络安全基础', 'Web安全漏洞分析与防护技术', '郑老师', 659.00, 48, 'INTERMEDIATE', '网络安全', '/images/security.jpg');

-- 插入测试订单数据
INSERT INTO orders (user_id, course_id, amount, quantity, discount, original_amount, status) VALUES
(3, 1, 299.00, 1, 0, 299.00, 'COMPLETED'),
(3, 2, 539.10, 1, 59.90, 599.00, 'COMPLETED'),
(3, 5, 404.10, 1, 44.90, 449.00, 'PENDING'),
(4, 1, 299.00, 1, 0, 299.00, 'COMPLETED'),
(4, 3, 449.10, 1, 49.90, 499.00, 'COMPLETED'),
(4, 7, 494.10, 1, 54.90, 549.00, 'PROCESSING'),
(3, 4, 359.10, 1, 39.90, 399.00, 'CANCELLED'),
(4, 6, 719.10, 1, 79.90, 799.00, 'PENDING');