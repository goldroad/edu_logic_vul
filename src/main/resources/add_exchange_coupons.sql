-- Add exchangeable coupons with codes
INSERT INTO coupon (name, code, type, discount_value, min_amount, total_count, used_count, enabled, start_time, end_time, create_time) VALUES
('New User Coupon', 'NEWUSER2024', 'FIXED', 20.00, 100.00, 100, 0, 1, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), NOW()),
('Discount Coupon', 'DISCOUNT15', 'PERCENT', 15.00, 200.00, 50, 0, 1, NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY), NOW()),
('Programming Course Coupon', 'CODING50', 'FIXED', 50.00, 300.00, 30, 0, 1, NOW(), DATE_ADD(NOW(), INTERVAL 60 DAY), NOW()),
('Weekend Special', 'WEEKEND20', 'PERCENT', 20.00, 150.00, 80, 0, 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), NOW()),
('VIP Exclusive', 'VIP100', 'FIXED', 100.00, 500.00, 20, 0, 1, NOW(), DATE_ADD(NOW(), INTERVAL 90 DAY), NOW());