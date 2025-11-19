-- ========================================
-- TEST DATA SCRIPT FOR QUANLYBANSACH DB
-- ========================================
-- Chạy script này để nhập dữ liệu test vào database
-- Database: quanlybansach

-- 1. INSERT TEST USERS
-- Password: 1111 (bcrypt: $2a$10$slYQmyNdGzin7olVN3p5Be7DW5ysg8DCWcD7vPUQeJ8qB8S2K2Hm2)
-- Password: 2222 (bcrypt: $2a$10$QsfBhfJwvBGn/xQfIEsqn.rxMkl6n3KR2ydGRKNKxH8K3.8GbGkba)

INSERT INTO users (username, email, password, full_name, phone_number, address, role, is_active, created_at, updated_at) VALUES
('master', 'master@bookstore.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DW5ysg8DCWcD7vPUQeJ8qB8S2K2Hm2', 'Master Administrator', '0901111111', '123 Main St, Hanoi', 'MASTER', TRUE, NOW(), NOW()),
('admin1', 'admin1@bookstore.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DW5ysg8DCWcD7vPUQeJ8qB8S2K2Hm2', 'Admin User 1', '0902222222', '456 Second St, Hanoi', 'ADMIN', TRUE, NOW(), NOW()),
('admin2', 'admin2@bookstore.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DW5ysg8DCWcD7vPUQeJ8qB8S2K2Hm2', 'Admin User 2', '0903333333', '789 Third St, Hanoi', 'ADMIN', TRUE, NOW(), NOW()),
('user1', 'user1@bookstore.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DW5ysg8DCWcD7vPUQeJ8qB8S2K2Hm2', 'John Doe', '0904444444', '100 Fourth St, HCMC', 'USER', TRUE, NOW(), NOW()),
('user2', 'user2@bookstore.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DW5ysg8DCWcD7vPUQeJ8qB8S2K2Hm2', 'Jane Smith', '0905555555', '200 Fifth St, HCMC', 'USER', TRUE, NOW(), NOW()),
('user3', 'user3@bookstore.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DW5ysg8DCWcD7vPUQeJ8qB8S2K2Hm2', 'Bob Johnson', '0906666666', '300 Sixth St, Da Nang', 'USER', TRUE, NOW(), NOW());

-- 2. INSERT TEST CATEGORIES
INSERT INTO categories (name, description) VALUES
('Công nghệ', 'Sách về công nghệ thông tin'),
('Kinh doanh', 'Sách về kinh doanh và quản lý'),
('Tâm lý học', 'Sách về tâm lý và phát triển bản thân'),
('Văn học', 'Sách văn học và tiểu thuyết'),
('Giáo dục', 'Sách về giáo dục và học tập');

-- 3. INSERT TEST PRODUCTS
INSERT INTO products (title, author, description, price, quantity, category_id, created_at, updated_at) VALUES
-- Công nghệ
('Java Programming Complete', 'Herbert Schildt', 'Hướng dẫn lập trình Java từ cơ bản đến nâng cao', 250000, 15, 1, NOW(), NOW()),
('Python for Data Science', 'Jake VanderPlas', 'Khoa học dữ liệu với Python', 280000, 20, 1, NOW(), NOW()),
('Web Development with Spring Boot', 'Craig Walls', 'Phát triển web hiện đại với Spring Boot', 320000, 12, 1, NOW(), NOW()),
('Clean Code', 'Robert C. Martin', 'Viết code sạch và dễ bảo trì', 200000, 25, 1, NOW(), NOW()),
('Design Patterns', 'Gang of Four', 'Các mẫu thiết kế phần mềm', 280000, 18, 1, NOW(), NOW()),

-- Kinh doanh
('Lean Startup', 'Eric Ries', 'Khởi động kinh doanh với phương pháp Lean', 240000, 30, 2, NOW(), NOW()),
('Good to Great', 'Jim Collins', 'Từ tốt đến tuyệt vời', 260000, 22, 2, NOW(), NOW()),
('The Goal', 'Eliyahu Goldratt', 'Mục tiêu - Quản lý sản xuất hiệu quả', 220000, 16, 2, NOW(), NOW()),

-- Tâm lý học
('Thinking, Fast and Slow', 'Daniel Kahneman', 'Suy nghĩ nhanh và chậm', 300000, 20, 3, NOW(), NOW()),
('Atomic Habits', 'James Clear', 'Những thói quen nhỏ, những thay đổi lớn', 270000, 28, 3, NOW(), NOW()),
('Mindset', 'Carol Dweck', 'Tư duy cố định vs tư duy phát triển', 240000, 19, 3, NOW(), NOW()),

-- Văn học
('Tắt Đèn', 'Ngô Tất Tố', 'Tiểu thuyết kinh điển Việt Nam', 85000, 40, 4, NOW(), NOW()),
('Những Đứa Con Trong Gia Đình', 'Dương Tuấn', 'Tác phẩm văn học Việt Nam', 95000, 35, 4, NOW(), NOW()),

-- Giáo dục
('Học để Thành Công', 'Dale Carnegie', 'Kỹ năng giao tiếp và thuyết trình', 210000, 25, 5, NOW(), NOW()),
('Kỹ năng Lãnh đạo', 'John Maxwell', 'Phát triển kỹ năng lãnh đạo', 250000, 20, 5, NOW(), NOW());

-- 4. INSERT TEST ORDERS
-- Order 1: user1 (User order)
INSERT INTO orders (user_id, order_id, total_price, status, payment_type, order_date) VALUES
(4, 'ORD001', 750000, 'PENDING', 'BANK_TRANSFER', NOW());

INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(1, 1, 2, 250000),
(1, 6, 1, 250000);

INSERT INTO order_addresses (order_id, first_name, last_name, email, mobile_no, address, city, state, pincode) VALUES
(1, 'John', 'Doe', 'john@example.com', '0904444444', '100 Fourth St', 'HCMC', 'HCMC', '70000');

-- Order 2: user2 (User order)
INSERT INTO orders (user_id, order_id, total_price, status, payment_type, order_date) VALUES
(5, 'ORD002', 540000, 'PROCESSING', 'COD', NOW() - INTERVAL 1 DAY);

INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(2, 3, 1, 320000),
(2, 9, 1, 220000);

INSERT INTO order_addresses (order_id, first_name, last_name, email, mobile_no, address, city, state, pincode) VALUES
(2, 'Jane', 'Smith', 'jane@example.com', '0905555555', '200 Fifth St', 'HCMC', 'HCMC', '70000');

-- Order 3: user3 (User order)
INSERT INTO orders (user_id, order_id, total_price, status, payment_type, order_date) VALUES
(6, 'ORD003', 570000, 'SHIPPED', 'CREDIT_CARD', NOW() - INTERVAL 3 DAY);

INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(3, 10, 2, 270000),
(3, 13, 1, 30000);

INSERT INTO order_addresses (order_id, first_name, last_name, email, mobile_no, address, city, state, pincode) VALUES
(3, 'Bob', 'Johnson', 'bob@example.com', '0906666666', '300 Sixth St', 'Da Nang', 'DN', '50000');

-- 5. INSERT MORE ORDERS FOR TESTING
-- Order 4: user1 (Another order)
INSERT INTO orders (user_id, order_id, total_price, status, payment_type, order_date) VALUES
(4, 'ORD004', 300000, 'DELIVERED', 'PAYPAL', NOW() - INTERVAL 7 DAY);

INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(4, 10, 1, 270000),
(4, 14, 1, 30000);

INSERT INTO order_addresses (order_id, first_name, last_name, email, mobile_no, address, city, state, pincode) VALUES
(4, 'John', 'Doe', 'john@example.com', '0904444444', '100 Fourth St', 'HCMC', 'HCMC', '70000');

-- Order 5: user2 (Cancelled order)
INSERT INTO orders (user_id, order_id, total_price, status, payment_type, order_date) VALUES
(5, 'ORD005', 200000, 'CANCELLED', 'COD', NOW() - INTERVAL 10 DAY);

INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(5, 4, 1, 200000);

INSERT INTO order_addresses (order_id, first_name, last_name, email, mobile_no, address, city, state, pincode) VALUES
(5, 'Jane', 'Smith', 'jane@example.com', '0905555555', '200 Fifth St', 'HCMC', 'HCMC', '70000');

-- ========================================
-- VERIFY DATA (RUN THESE TO CHECK)
-- ========================================
-- SELECT COUNT(*) as total_users FROM users;
-- SELECT COUNT(*) as total_categories FROM categories;
-- SELECT COUNT(*) as total_products FROM products;
-- SELECT COUNT(*) as total_orders FROM orders;
-- SELECT * FROM users;
-- SELECT * FROM categories;
-- SELECT * FROM products;
-- SELECT * FROM orders;
