-- Update order status to have different statuses for testing
UPDATE orders SET status = 'PENDING' WHERE id % 4 = 1;
UPDATE orders SET status = 'PAID' WHERE id % 4 = 2;
UPDATE orders SET status = 'CANCELLED' WHERE id % 4 = 3;
UPDATE orders SET status = 'PENDING' WHERE id % 4 = 0;

-- Check the updated data
SELECT id, order_no, status, final_amount FROM orders LIMIT 10;