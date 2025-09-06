-- 重新设计user_coupon表结构
DROP TABLE IF EXISTS user_coupon;

CREATE TABLE user_coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    coupon_id BIGINT COMMENT '优惠券ID（可为空，用于兑换码生成的优惠券）',
    
    -- 优惠券基本信息
    coupon_name VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    coupon_code VARCHAR(50) NOT NULL COMMENT '优惠券代码',
    description TEXT COMMENT '优惠券描述',
    type ENUM('FIXED', 'PERCENT') NOT NULL COMMENT '优惠券类型：FIXED-满减券，PERCENT-折扣券',
    discount_value DECIMAL(10,2) NOT NULL COMMENT '折扣值或减免金额',
    min_amount DECIMAL(10,2) DEFAULT 0 COMMENT '最小使用金额',
    
    -- 状态和时间
    status ENUM('UNUSED', 'USED', 'EXPIRED') DEFAULT 'UNUSED' COMMENT '状态：UNUSED-可使用，USED-已使用，EXPIRED-已过期',
    receive_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
    use_time DATETIME COMMENT '使用时间',
    expire_time DATETIME COMMENT '过期时间',
    
    -- 使用限制
    usage_restriction VARCHAR(200) COMMENT '使用限制描述',
    applicable_category VARCHAR(100) COMMENT '适用分类',
    
    -- 订单信息
    order_id BIGINT COMMENT '使用的订单ID',
    
    -- 索引
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_expire_time (expire_time),
    INDEX idx_coupon_code (coupon_code)
) COMMENT='用户优惠券表';

-- 插入测试数据
INSERT INTO user_coupon (
    user_id, coupon_id, coupon_name, coupon_code, description, type, 
    discount_value, min_amount, status, receive_time, use_time, expire_time,
    usage_restriction, applicable_category, order_id
) VALUES 
-- 用户ID为1的优惠券（可使用）
(1, 1, '新人专享折扣券', 'NEW50', '适用于所有课程，首次购买专享', 'PERCENT', 50.00, 0.00, 'UNUSED', '2024-09-01 10:00:00', NULL, '2024-12-31 23:59:59', '首次购买专享', '所有课程', NULL),
(1, 2, '满200减30优惠券', 'SAVE30', '购买课程满200元可用', 'FIXED', 30.00, 200.00, 'UNUSED', '2024-09-02 14:30:00', NULL, '2024-10-15 23:59:59', '满200元可用', '所有课程', NULL),
(1, 3, '编程课程专享券', 'CODE20', '仅限编程类课程使用', 'PERCENT', 20.00, 0.00, 'UNUSED', '2024-09-03 09:15:00', NULL, '2024-11-30 23:59:59', '仅限编程类课程', '编程课程', NULL),
(1, 4, '满300减50优惠券', 'BIG50', '购买课程满300元可用', 'FIXED', 50.00, 300.00, 'UNUSED', '2024-09-04 16:20:00', NULL, '2024-09-30 23:59:59', '满300元可用', '所有课程', NULL),
(1, 5, '周末学习优惠券', 'WEEKEND15', '周末购买课程专享', 'PERCENT', 15.00, 0.00, 'UNUSED', '2024-09-05 11:45:00', NULL, '2024-10-31 23:59:59', '周末购买专享', '所有课程', NULL),

-- 用户ID为1的优惠券（已使用）
(1, 6, '满100减20优惠券', 'FIRST20', '已于2024-09-01使用', 'FIXED', 20.00, 100.00, 'USED', '2024-08-25 10:00:00', '2024-09-01 15:30:00', '2024-12-31 23:59:59', '满100元可用', '所有课程', 1001),
(1, 7, '学生专享折扣券', 'STUDENT10', '已于2024-08-28使用', 'PERCENT', 10.00, 0.00, 'USED', '2024-08-20 14:00:00', '2024-08-28 10:15:00', '2024-12-31 23:59:59', '学生专享', '所有课程', 1002),

-- 用户ID为1的优惠券（已过期）
(1, 8, '夏季特惠折扣券', 'SUMMER25', '已于2024-08-31过期', 'PERCENT', 25.00, 0.00, 'EXPIRED', '2024-07-01 10:00:00', NULL, '2024-08-31 23:59:59', '夏季特惠', '所有课程', NULL),

-- 用户ID为2的优惠券（测试其他用户）
(2, 1, '新人专享折扣券', 'NEW50_U2', '适用于所有课程，首次购买专享', 'PERCENT', 50.00, 0.00, 'UNUSED', '2024-09-01 10:00:00', NULL, '2024-12-31 23:59:59', '首次购买专享', '所有课程', NULL),
(2, 2, '满200减30优惠券', 'SAVE30_U2', '购买课程满200元可用', 'FIXED', 30.00, 200.00, 'USED', '2024-09-02 14:30:00', '2024-09-05 16:20:00', '2024-10-15 23:59:59', '满200元可用', '所有课程', 1003),

-- 用户ID为3的优惠券
(3, 1, '设计课程专享券', 'DESIGN30', '仅限设计类课程使用', 'PERCENT', 30.00, 0.00, 'UNUSED', '2024-09-01 10:00:00', NULL, '2024-11-30 23:59:59', '仅限设计类课程', '设计课程', NULL),
(3, 2, '满150减25优惠券', 'SAVE25', '购买课程满150元可用', 'FIXED', 25.00, 150.00, 'UNUSED', '2024-09-02 14:30:00', NULL, '2024-10-20 23:59:59', '满150元可用', '所有课程', NULL),
(3, 3, '早鸟优惠券', 'EARLY20', '早期购买专享', 'PERCENT', 20.00, 0.00, 'EXPIRED', '2024-07-01 10:00:00', NULL, '2024-08-15 23:59:59', '早期购买专享', '所有课程', NULL);

-- 查询验证数据
SELECT 
    u.real_name AS '用户姓名',
    uc.coupon_name AS '优惠券名称',
    uc.coupon_code AS '优惠券代码',
    uc.type AS '类型',
    uc.discount_value AS '折扣值',
    uc.min_amount AS '最小金额',
    uc.status AS '状态',
    uc.receive_time AS '领取时间',
    uc.expire_time AS '过期时间'
FROM user_coupon uc
LEFT JOIN user u ON uc.user_id = u.id
ORDER BY uc.user_id, uc.receive_time DESC;