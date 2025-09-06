-- Redesign user_coupon table structure
DROP TABLE IF EXISTS user_coupon;

CREATE TABLE user_coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    coupon_id BIGINT,
    
    -- Basic coupon information
    coupon_name VARCHAR(100) NOT NULL,
    coupon_code VARCHAR(50) NOT NULL,
    description TEXT,
    type ENUM('FIXED', 'PERCENT') NOT NULL,
    discount_value DECIMAL(10,2) NOT NULL,
    min_amount DECIMAL(10,2) DEFAULT 0,
    
    -- Status and time
    status ENUM('UNUSED', 'USED', 'EXPIRED') DEFAULT 'UNUSED',
    receive_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    use_time DATETIME,
    expire_time DATETIME,
    
    -- Usage restrictions
    usage_restriction VARCHAR(200),
    applicable_category VARCHAR(100),
    
    -- Order information
    order_id BIGINT,
    
    -- Indexes
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_expire_time (expire_time),
    INDEX idx_coupon_code (coupon_code)
);

-- Insert test data
INSERT INTO user_coupon (
    user_id, coupon_id, coupon_name, coupon_code, description, type, 
    discount_value, min_amount, status, receive_time, use_time, expire_time,
    usage_restriction, applicable_category, order_id
) VALUES 
-- Available coupons for user 1
(1, 1, 'New User Discount', 'NEW50', 'For all courses, first purchase only', 'PERCENT', 50.00, 0.00, 'UNUSED', '2024-09-01 10:00:00', NULL, '2024-12-31 23:59:59', 'First purchase only', 'All courses', NULL),
(1, 2, 'Save 30 on 200', 'SAVE30', 'Save 30 when spend over 200', 'FIXED', 30.00, 200.00, 'UNUSED', '2024-09-02 14:30:00', NULL, '2024-10-15 23:59:59', 'Min spend 200', 'All courses', NULL),
(1, 3, 'Programming Course Special', 'CODE20', 'For programming courses only', 'PERCENT', 20.00, 0.00, 'UNUSED', '2024-09-03 09:15:00', NULL, '2024-11-30 23:59:59', 'Programming courses only', 'Programming', NULL),
(1, 4, 'Save 50 on 300', 'BIG50', 'Save 50 when spend over 300', 'FIXED', 50.00, 300.00, 'UNUSED', '2024-09-04 16:20:00', NULL, '2024-09-30 23:59:59', 'Min spend 300', 'All courses', NULL),
(1, 5, 'Weekend Learning', 'WEEKEND15', 'Weekend purchase special', 'PERCENT', 15.00, 0.00, 'UNUSED', '2024-09-05 11:45:00', NULL, '2024-10-31 23:59:59', 'Weekend purchase only', 'All courses', NULL),

-- Used coupons for user 1
(1, 6, 'Save 20 on 100', 'FIRST20', 'Used on 2024-09-01', 'FIXED', 20.00, 100.00, 'USED', '2024-08-25 10:00:00', '2024-09-01 15:30:00', '2024-12-31 23:59:59', 'Min spend 100', 'All courses', 1001),
(1, 7, 'Student Discount', 'STUDENT10', 'Used on 2024-08-28', 'PERCENT', 10.00, 0.00, 'USED', '2024-08-20 14:00:00', '2024-08-28 10:15:00', '2024-12-31 23:59:59', 'Student only', 'All courses', 1002),

-- Expired coupons for user 1
(1, 8, 'Summer Special', 'SUMMER25', 'Expired on 2024-08-31', 'PERCENT', 25.00, 0.00, 'EXPIRED', '2024-07-01 10:00:00', NULL, '2024-08-31 23:59:59', 'Summer special', 'All courses', NULL),

-- Coupons for user 2
(2, 1, 'New User Discount', 'NEW50_U2', 'For all courses, first purchase only', 'PERCENT', 50.00, 0.00, 'UNUSED', '2024-09-01 10:00:00', NULL, '2024-12-31 23:59:59', 'First purchase only', 'All courses', NULL),
(2, 2, 'Save 30 on 200', 'SAVE30_U2', 'Save 30 when spend over 200', 'FIXED', 30.00, 200.00, 'USED', '2024-09-02 14:30:00', '2024-09-05 16:20:00', '2024-10-15 23:59:59', 'Min spend 200', 'All courses', 1003),

-- Coupons for user 3
(3, 1, 'Design Course Special', 'DESIGN30', 'For design courses only', 'PERCENT', 30.00, 0.00, 'UNUSED', '2024-09-01 10:00:00', NULL, '2024-11-30 23:59:59', 'Design courses only', 'Design', NULL),
(3, 2, 'Save 25 on 150', 'SAVE25', 'Save 25 when spend over 150', 'FIXED', 25.00, 150.00, 'UNUSED', '2024-09-02 14:30:00', NULL, '2024-10-20 23:59:59', 'Min spend 150', 'All courses', NULL),
(3, 3, 'Early Bird', 'EARLY20', 'Early purchase special', 'PERCENT', 20.00, 0.00, 'EXPIRED', '2024-07-01 10:00:00', NULL, '2024-08-15 23:59:59', 'Early purchase only', 'All courses', NULL);