-- Drop the extra orders table and use existing order table
DROP TABLE IF EXISTS orders;

-- Insert test orders with different statuses into existing order table
INSERT INTO `order` (order_no, user_id, course_id, original_amount, discount_amount, shipping_fee, final_amount, quantity, status, create_time) VALUES
('ORD202409060101', 1, 1, 299.00, 100.00, 0.00, 199.00, 1, 'PENDING', '2024-09-05 10:30:00'),
('ORD202409060102', 1, 2, 399.00, 100.00, 0.00, 299.00, 1, 'PAID', '2024-09-04 14:20:00'),
('ORD202409060103', 1, 3, 199.00, 50.00, 0.00, 149.00, 1, 'CANCELLED', '2024-09-03 16:45:00'),
('ORD202409060104', 1, 4, 499.00, 100.00, 0.00, 399.00, 1, 'PENDING', '2024-09-02 09:15:00'),
('ORD202409060105', 1, 5, 299.00, 100.00, 0.00, 199.00, 1, 'PAID', '2024-09-01 11:30:00'),
('ORD202409060106', 2, 1, 299.00, 50.00, 0.00, 249.00, 1, 'PENDING', '2024-09-06 08:20:00'),
('ORD202409060107', 2, 2, 399.00, 40.00, 0.00, 359.00, 1, 'PAID', '2024-09-05 15:10:00'),
('ORD202409060108', 3, 3, 199.00, 20.00, 0.00, 179.00, 1, 'CANCELLED', '2024-09-04 12:40:00')
ON DUPLICATE KEY UPDATE status = VALUES(status);

-- Update payment info for paid orders
UPDATE `order` SET 
    payment_method = 'BALANCE',
    payment_transaction_id = CONCAT('TXN', id, DATE_FORMAT(NOW(), '%Y%m%d')),
    pay_time = DATE_ADD(create_time, INTERVAL 5 MINUTE)
WHERE status = 'PAID';

-- Show the updated data
SELECT id, order_no, user_id, course_id, status, final_amount, create_time FROM `order` WHERE user_id IN (1,2,3) ORDER BY create_time DESC;