-- =====================================================
-- DATABASE INITIALIZATION SCRIPT FOR QUẢN LÝ BÁN SÁCH
-- Database: quanlybansach
-- Created: 19/11/2025
-- =====================================================

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS quanlybansach;
USE quanlybansach;

-- =====================================================
-- XÓA CÁC TABLE CŨ NẾU TỒN TẠI
-- =====================================================
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS order_addresses;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- TABLE: users
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone_number VARCHAR(15),
    address TEXT,
    role VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    INDEX idx_username (username),
    INDEX idx_email (email)
);

-- =====================================================
-- TABLE: categories
-- =====================================================
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    image_name VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT true,
    INDEX idx_name (name)
);

-- =====================================================
-- TABLE: products
-- =====================================================
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    price DOUBLE,
    discount_price DOUBLE,
    discount INT,
    stock INT,
    image_names LONGTEXT COMMENT 'JSON array of image names',
    is_active BOOLEAN NOT NULL DEFAULT true,
    category_id INT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    INDEX idx_title (title),
    INDEX idx_category_id (category_id)
);

-- =====================================================
-- TABLE: order_addresses
-- =====================================================
CREATE TABLE IF NOT EXISTS order_addresses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    mobile_no VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(10) NOT NULL
);

-- =====================================================
-- TABLE: orders
-- =====================================================
CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(100) NOT NULL UNIQUE,
    order_date DATETIME NOT NULL,
    status VARCHAR(30) NOT NULL,
    payment_type VARCHAR(50) NOT NULL,
    total_price DOUBLE NOT NULL,
    order_address_id INT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (order_address_id) REFERENCES order_addresses(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_order_id (order_id),
    INDEX idx_user_id (user_id)
);

-- =====================================================
-- TABLE: order_items
-- =====================================================
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
);

-- =====================================================
-- TABLE: carts
-- =====================================================
CREATE TABLE IF NOT EXISTS carts (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     user_id BIGINT NOT NULL UNIQUE,
                                     total_amount DOUBLE NOT NULL DEFAULT 0.0,
                                     created_at DATETIME NOT NULL,
                                     updated_at DATETIME NOT NULL,
                                     FOREIGN KEY (user_id) REFERENCES users(id),
                                     INDEX idx_user_id (user_id)
    );

-- =====================================================
-- TABLE: cart_items
-- =====================================================
CREATE TABLE IF NOT EXISTS cart_items (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          cart_id BIGINT NOT NULL,
                                          product_id INT NOT NULL,
                                          quantity INT NOT NULL,
                                          unit_price DOUBLE NOT NULL,
                                          FOREIGN KEY (cart_id) REFERENCES carts(id),
                                          FOREIGN KEY (product_id) REFERENCES products(id),
                                          UNIQUE KEY uk_cart_product (cart_id, product_id)
    );

-- =====================================================
-- 1. INSERT DATA VÀO BẢNG USERS
-- =====================================================
-- NOTE: All passwords are hashed using BCrypt (Spring Security)
-- Default password for all accounts: 1111
-- You can login using these credentials:
--   Admin: admin01 / admin01@bookstore.com (password: 1111)
--   Customer: customer01 / customer01@gmail.com (password: 1111)
-- =====================================================
INSERT INTO users (username, email, password, full_name, phone_number, address, role, is_active, created_at, updated_at) VALUES
-- Admin users (Password: 1111)
('admin01', 'admin01@bookstore.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Quản Trị Viên 1', '0123456789', '123 Đường Lê Lợi, Quận 1, TP HCM', 'ADMIN', true, NOW(), NOW()),
('admin02', 'admin02@bookstore.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Quản Trị Viên 2', '0123456788', '124 Đường Nguyễn Huệ, Quận 1, TP HCM', 'ADMIN', true, NOW(), NOW()),

-- Customer users (Password: 1111)
('customer01', 'customer01@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Nguyễn Văn A', '0987654321', '456 Đường Nguyễn Huệ, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer02', 'customer02@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Trần Thị B', '0912345678', '789 Đường Trần Hưng Đạo, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer03', 'customer03@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Lê Văn C', '0903456789', '321 Đường Phan Bội Châu, Quận 3, TP HCM', 'USER', true, NOW(), NOW()),
('customer04', 'customer04@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Phạm Thị D', '0934567890', '654 Đường Võ Văn Kiệt, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer05', 'customer05@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Hoàng Văn E', '0945678901', '987 Đường Nguyễn Chí Thanh, Quận 2, TP HCM', 'USER', true, NOW(), NOW()),
('customer06', 'customer06@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Võ Thị F', '0956789012', '111 Đường Lý Tự Trọng, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer07', 'customer07@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Dương Văn G', '0967890123', '222 Đường Ngô Đức Kế, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer08', 'customer08@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Bùi Thị H', '0978901234', '333 Đường Bạch Đằng, Quận 3, TP HCM', 'USER', true, NOW(), NOW()),
('customer09', 'customer09@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Hồ Văn I', '0989012345', '444 Đường Ký Con, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer10', 'customer10@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Tạ Thị J', '0990123456', '555 Đường Hàm Nghi, Quận 1, TP HCM', 'USER', true, NOW(), NOW());

-- =====================================================
-- 2. INSERT DATA VÀO BẢNG CATEGORIES
-- =====================================================
INSERT INTO categories (name, is_active) VALUES
('Sách Văn Học', true),
('Sách Kỹ Thuật', true),
('Sách Kinh Tế', true),
('Sách Tâm Lý', true),
('Sách Lịch Sử', true),
('Sách Khoa Học', true),
('Sách Ngoại Ngữ', true),
('Sách Trẻ Em', true);

-- =====================================================
-- 3. INSERT DATA VÀO BẢNG PRODUCTS
-- =====================================================
INSERT INTO products (title, description, price, discount_price, discount, stock, image_names, category_id, is_active) VALUES
-- Sách Văn Học (category_id = 1)
('Cô Gái Đến Từ Hàn Quốc', 'Tác phẩm lãng mạn về tình yêu giữa hai nước', 85000, 68000, 20, 50, '["co-gai-han-quoc.jpg"]', 1, true),
('Tâm Trạng Một Con Người', 'Tiểu thuyết tâm lý về đời sống con người', 95000, 76000, 20, 35, '["tam-trang-con-nguoi.jpg"]', 1, true),
('Chuyện Tình Mùa Đông', 'Câu chuyện lãng mạn trong mùa tuyết trắng', 78000, 62400, 20, 45, '["chuyen-tinh-mua-dong.jpg"]', 1, true),
('Người Thay Đổi Thế Giới', 'Tiểu thuyết kỳ lạ về những người bất thường', 125000, 100000, 20, 25, '["nguoi-thay-doi-the-gioi.jpg"]', 1, true),

-- Sách Kỹ Thuật (category_id = 2)
('Java Programming Advanced', 'Hướng dẫn lập trình Java nâng cao từ cơ bản đến chuyên sâu', 320000, 256000, 20, 60, '["java-advanced.jpg"]', 2, true),
('Spring Boot in Practice', 'Thực hành Spring Boot từ A đến Z', 285000, 228000, 20, 40, '["spring-boot-practice.jpg"]', 2, true),
('Database Design Patterns', 'Thiết kế cơ sở dữ liệu hiệu quả', 295000, 236000, 20, 30, '["database-design.jpg"]', 2, true),
('Web Development Mastery', 'Thành thạo phát triển web hiện đại', 310000, 248000, 20, 50, '["web-dev-mastery.jpg"]', 2, true),

-- Sách Kinh Tế (category_id = 3)
('Thay Đổi Tư Duy Kinh Doanh', 'Cách thay đổi cách suy nghĩ để thành công', 150000, 120000, 20, 70, '["tu-duy-kinh-doanh.jpg"]', 3, true),
('Chiến Lược Marketing Hiệu Quả', 'Các kỹ thuật marketing hiện đại', 165000, 132000, 20, 55, '["marketing-hieu-qua.jpg"]', 3, true),
('Lãnh Đạo Và Quản Lý', 'Nghệ thuật lãnh đạo đội ngũ', 180000, 144000, 20, 40, '["lanh-dao-quan-ly.jpg"]', 3, true),
('Tài Chính Cá Nhân', 'Quản lý tài chính để giàu có', 145000, 116000, 20, 65, '["tai-chinh-ca-nhan.jpg"]', 3, true),

-- Sách Tâm Lý (category_id = 4)
('Tâm Lý Học Hành Vi', 'Hiểu rõ hành vi con người', 165000, 132000, 20, 45, '["tam-ly-hanh-vi.jpg"]', 4, true),
('Sức Khỏe Tâm Thần', 'Chăm sóc sức khỏe tâm lý', 155000, 124000, 20, 50, '["suc-khoe-tam-than.jpg"]', 4, true),
('Tự Tin Và Tự Yêu Bản Thân', 'Xây dựng lòng tự tin mạnh mẽ', 135000, 108000, 20, 60, '["tu-tin-tu-yeu.jpg"]', 4, true),
('Điều Khiển Cảm Xúc', 'Kiểm soát cảm xúc hiệu quả', 140000, 112000, 20, 55, '["dieu-khien-cam-xuc.jpg"]', 4, true),

-- Sách Lịch Sử (category_id = 5)
('Lịch Sử Việt Nam', 'Những mốc son của lịch sử Việt Nam', 175000, 140000, 20, 50, '["lich-su-viet-nam.jpg"]', 5, true),
('Những Nhân Vật Lịch Sử Vĩ Đại', 'Tiểu sử những nhân vật lịch sử nổi tiếng', 185000, 148000, 20, 40, '["nhan-vat-lich-su.jpg"]', 5, true),
('Chiến Tranh Tư Liệu', 'Những sự kiện chiến tranh trong lịch sử', 195000, 156000, 20, 30, '["chien-tranh-tu-lieu.jpg"]', 5, true),

-- Sách Khoa Học (category_id = 6)
('Vũ Trụ Và Cuộc Sống', 'Khám phá bí mật của vũ trụ', 210000, 168000, 20, 40, '["vu-tru-cuoc-song.jpg"]', 6, true),
('Khoa Học Tự Nhiên', 'Nhập môn khoa học tự nhiên', 155000, 124000, 20, 60, '["khoa-hoc-tu-nhien.jpg"]', 6, true),
('Công Nghệ Sinh Học', 'Ứng dụng sinh học trong công nghệ', 235000, 188000, 20, 25, '["cong-nghe-sinh-hoc.jpg"]', 6, true),

-- Sách Ngoại Ngữ (category_id = 7)
('English Grammar Complete', 'Ngữ pháp tiếng Anh toàn diện', 165000, 132000, 20, 70, '["english-grammar.jpg"]', 7, true),
('Conversation English', 'Hội thoại tiếng Anh thực tế', 145000, 116000, 20, 50, '["conversation-english.jpg"]', 7, true),
('Vocabulary Master', 'Từ vựng tiếng Anh nâng cao', 135000, 108000, 20, 65, '["vocabulary-master.jpg"]', 7, true),

-- Sách Trẻ Em (category_id = 8)
('Chuyện Cổ Tích Dân Gian', 'Tuyển tập chuyện cổ tích hay', 75000, 60000, 20, 80, '["chuyen-co-tich.jpg"]', 8, true),
('Bé Học Màu Sắc', 'Sách dạy trẻ nhận biết màu sắc', 65000, 52000, 20, 100, '["be-hoc-mau-sac.jpg"]', 8, true),
('Những Câu Chuyện Giáo Dục', 'Câu chuyện dạy dỏ trẻ em', 85000, 68000, 20, 70, '["cau-chuyen-giao-duc.jpg"]', 8, true);

-- =====================================================
-- SCRIPT EXECUTION COMPLETE
-- =====================================================
SHOW TABLES;
SELECT COUNT(*) as total_users FROM users;
SELECT COUNT(*) as total_categories FROM categories;
SELECT COUNT(*) as total_products FROM products;



-- =====================================================
-- 4. INSERT DATA VÀO BẢNG ORDER_ADDRESSES
-- =====================================================

-- Địa chỉ 1: Nguyễn Văn A (ID=3)
INSERT INTO order_addresses (first_name, last_name, email, mobile_no, address, city, state, pincode) VALUES
    ('Nguyễn', 'Văn A', 'customer01@gmail.com', '0987654321', '456 Đường Nguyễn Huệ', 'TP Hồ Chí Minh', 'Quận 1', '70000');

-- Địa chỉ 2: Trần Thị B (ID=4)
INSERT INTO order_addresses (first_name, last_name, email, mobile_no, address, city, state, pincode) VALUES
    ('Trần', 'Thị B', 'customer02@gmail.com', '0912345678', '789 Đường Trần Hưng Đạo', 'TP Hồ Chí Minh', 'Quận 1', '70000');

-- Địa chỉ 3: Địa chỉ mới cho A (sử dụng snapshot)
INSERT INTO order_addresses (first_name, last_name, email, mobile_no, address, city, state, pincode) VALUES
    ('Nguyễn', 'Văn A', 'customer01@gmail.com', '0987654321', '689 Đường Hai Bà Trưng', 'TP Hồ Chí Minh', 'Quận 3', '70000');


-- =====================================================
-- 5. INSERT DATA VÀO BẢNG ORDERS
-- Ghi chú: order_address_id: 1, 2, 3
-- user_id: 3 (customer01), 4 (customer02)
-- =====================================================
-- Đơn hàng 1
INSERT INTO orders (order_id, order_date, status, payment_type, total_price, order_address_id, user_id) VALUES
    ('ORD-20251120-001', DATE_SUB(NOW(), INTERVAL 4 DAY), 'DELIVERED', 'COD', 136000.00, 1, 3);

-- Đơn hàng 2
INSERT INTO orders (order_id, order_date, status, payment_type, total_price, order_address_id, user_id) VALUES
    ('ORD-20251122-002', DATE_SUB(NOW(), INTERVAL 2 DAY), 'PENDING', 'BANK_TRANSFER', 236000.00, 2, 4);

-- Đơn hàng 3
INSERT INTO orders (order_id, order_date, status, payment_type, total_price, order_address_id, user_id) VALUES
    ('ORD-20251123-003', DATE_SUB(NOW(), INTERVAL 1 DAY), 'CANCELLED', 'CREDIT_CARD', 228000.00, 3, 3);

-- Đơn hàng 4
INSERT INTO orders (order_id, order_date, status, payment_type, total_price, order_address_id, user_id) VALUES
    ('ORD-20251124-004', NOW(), 'PROCESSING', 'PAYPAL', 456000.00, 2, 4);

-- Đơn hàng 5
INSERT INTO orders (order_id, order_date, status, payment_type, total_price, order_address_id, user_id) VALUES
    ('ORD-20251124-005', NOW(), 'PENDING', 'COD', 100000.00, 1, 3);


-- =====================================================
-- 6. INSERT DATA VÀO BẢNG ORDER_ITEMS
-- Lấy ID của orders (1-5) và products (1-27)
-- =====================================================

-- Đơn hàng 1 (ORD-001, total: 136k)
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
                                                                    (1, 1, 1, 68000.00),
                                                                    (1, 3, 1, 68000.00);

-- Đơn hàng 2 (ORD-002, total: 236k)
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
    (2, 7, 1, 236000.00);

-- Đơn hàng 3 (ORD-003, total: 228k)
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
    (3, 6, 1, 228000.00);

-- Đơn hàng 4 (ORD-004, total: 456k)
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
    (4, 6, 2, 228000.00);

-- Đơn hàng 5 (ORD-005, total: 100k)
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
    (5, 4, 1, 100000.00);


-- =====================================================
-- SCRIPT EXECUTION COMPLETE
-- =====================================================

-- Kiểm tra kết quả
SELECT * FROM order_addresses;
SELECT * FROM orders;
SELECT * FROM order_items;

-- =====================================================
-- 7. INSERT DATA VÀO BẢNG CARTS và CART_ITEMS
-- =====================================================

-- 1. Tạo Giỏ hàng cho customer01 (ID=3)
INSERT INTO carts (user_id, total_amount, created_at, updated_at) VALUES
    (3, 308000.00, NOW(), NOW());

-- 2. Tạo Giỏ hàng cho customer02 (ID=4)
INSERT INTO carts (user_id, total_amount, created_at, updated_at) VALUES
    (4, 132000.00, NOW(), NOW());

UPDATE carts SET total_amount = 344000.00 WHERE id = 1;

INSERT INTO cart_items (cart_id, product_id, quantity, unit_price) VALUES
                                                                       (1, 15, 1, 124000.00),
                                                                       (1, 16, 1, 108000.00),
                                                                       (1, 17, 1, 112000.00);

-- Items cho customer02 (ID=4, Cart ID=2): Total 132,000
-- Sách 10 (Chiến Lược Marketing Hiệu Quả - 132k) * 1
INSERT INTO cart_items (cart_id, product_id, quantity, unit_price) VALUES
    (2, 10, 1, 132000.00);