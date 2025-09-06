-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    original_amount DECIMAL(10,2) NOT NULL,
    discount_amount DECIMAL(10,2) DEFAULT 0.00,
    shipping_fee DECIMAL(10,2) DEFAULT 0.00,
    final_amount DECIMAL(10,2) NOT NULL,
    quantity INT DEFAULT 1,
    status ENUM('PENDING', 'PAID', 'CANCELLED', 'REFUNDED') DEFAULT 'PENDING',
    payment_method ENUM('ALIPAY', 'WECHAT', 'BALANCE') NULL,
    payment_transaction_id VARCHAR(100) NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    pay_time DATETIME NULL,
    remark TEXT NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_order_no (order_no),
    INDEX idx_status (status)
);

-- Insert test orders with different statuses
INSERT INTO orders (order_no, user_id, course_id, original_amount, final_amount, status, create_time) VALUES
('ORD202409060001', 1, 1, 299.00, 199.00, 'PENDING', '2024-09-05 10:30:00'),
('ORD202409060002', 1, 2, 399.00, 299.00, 'PAID', '2024-09-04 14:20:00'),
('ORD202409060003', 1, 3, 199.00, 149.00, 'CANCELLED', '2024-09-03 16:45:00'),
('ORD202409060004', 1, 4, 499.00, 399.00, 'PENDING', '2024-09-02 09:15:00'),
('ORD202409060005', 1, 5, 299.00, 199.00, 'PAID', '2024-09-01 11:30:00'),
('ORD202409060006', 2, 1, 299.00, 249.00, 'PENDING', '2024-09-06 08:20:00'),
('ORD202409060007', 2, 2, 399.00, 359.00, 'PAID', '2024-09-05 15:10:00'),
('ORD202409060008', 3, 3, 199.00, 179.00, 'CANCELLED', '2024-09-04 12:40:00');

-- Update payment info for paid orders
UPDATE orders SET 
    payment_method = 'BALANCE',
    payment_transaction_id = CONCAT('TXN', id, DATE_FORMAT(NOW(), '%Y%m%d')),
    pay_time = DATE_ADD(create_time, INTERVAL 5 MINUTE)
WHERE status = 'PAID';

-- Show the created data
SELECT id, order_no, user_id, course_id, status, final_amount, create_time FROM orders;