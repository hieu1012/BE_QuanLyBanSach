-- =====================================================
-- DATA TEST CHO HỆ THỐNG QUẢN LÝ BÁN SÁCH
-- Database: quanlybansach
-- Created: 19/11/2025
-- =====================================================

-- =====================================================
-- XÓA DỮ LIỆU CŨ (TÙY CHỌN - BỎ COMMENT NẾU CẦN)
-- =====================================================
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE order_items;
-- TRUNCATE TABLE orders;
-- TRUNCATE TABLE order_address;
-- TRUNCATE TABLE products;
-- TRUNCATE TABLE categories;
-- TRUNCATE TABLE users;
-- SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 1. INSERT DATA VÀO BẢNG USERS
-- =====================================================
-- NOTE: Master user sẽ được tạo tự động bởi MasterUserInitializer
-- Master Account: username=master, password=1111

INSERT INTO users (username, email, password, full_name, phone_number, address, role, is_active, created_at, updated_at) VALUES
-- Admin users
('admin01', 'admin01@bookstore.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Quản Trị Viên 1', '0123456789', '123 Đường Lê Lợi, Quận 1, TP HCM', 'ADMIN', true, NOW(), NOW()),
('admin02', 'admin02@bookstore.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Quản Trị Viên 2', '0123456788', '124 Đường Nguyễn Huệ, Quận 1, TP HCM', 'ADMIN', true, NOW(), NOW()),

-- Customer users (20 customers)
('customer01', 'customer01@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Nguyễn Văn A', '0987654321', '456 Đường Nguyễn Huệ, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer02', 'customer02@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Trần Thị B', '0912345678', '789 Đường Trần Hưng Đạo, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer03', 'customer03@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Lê Văn C', '0903456789', '321 Đường Phan Bội Châu, Quận 3, TP HCM', 'USER', true, NOW(), NOW()),
('customer04', 'customer04@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Phạm Thị D', '0934567890', '654 Đường Võ Văn Kiệt, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer05', 'customer05@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Hoàng Văn E', '0945678901', '987 Đường Nguyễn Chí Thanh, Quận 2, TP HCM', 'USER', true, NOW(), NOW()),
('customer06', 'customer06@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Võ Thị F', '0956789012', '111 Đường Lý Tự Trọng, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer07', 'customer07@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Dương Văn G', '0967890123', '222 Đường Ngô Đức Kế, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer08', 'customer08@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Bùi Thị H', '0978901234', '333 Đường Bạch Đằng, Quận 3, TP HCM', 'USER', true, NOW(), NOW()),
('customer09', 'customer09@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Hồ Văn I', '0989012345', '444 Đường Ký Con, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer10', 'customer10@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Tạ Thị J', '0990123456', '555 Đường Hàm Nghi, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer11', 'customer11@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Phan Văn K', '0901234567', '666 Đường Đinh Tiên Hoàng, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer12', 'customer12@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Cao Thị L', '0912345679', '777 Đường Cộng Hòa, Quận Tân Bình, TP HCM', 'USER', true, NOW(), NOW()),
('customer13', 'customer13@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Đặng Văn M', '0923456780', '888 Đường Trần Não, Quận 2, TP HCM', 'USER', true, NOW(), NOW()),
('customer14', 'customer14@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Đào Thị N', '0934567891', '999 Đường An Dương Vương, Quận 5, TP HCM', 'USER', true, NOW(), NOW()),
('customer15', 'customer15@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Tô Văn O', '0945678902', '1000 Đường Phạm Văn Chiêu, Quận Gò Vấp, TP HCM', 'USER', true, NOW(), NOW()),
('customer16', 'customer16@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Trịnh Thị P', '0956789013', '1111 Đường Âu Cơ, Quận 11, TP HCM', 'USER', true, NOW(), NOW()),
('customer17', 'customer17@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Vũ Văn Q', '0967890124', '1212 Đường Hồ Tùng Mậu, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
('customer18', 'customer18@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Mạc Thị R', '0978901235', '1313 Đường Trương Định, Quận 3, TP HCM', 'USER', true, NOW(), NOW()),
('customer19', 'customer19@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Phùng Văn S', '0989012346', '1414 Đường Võ Oanh, Quận Gò Vấp, TP HCM', 'USER', true, NOW(), NOW()),
('customer20', 'customer20@gmail.com', '$2a$10$R9h31GNyPxWYT5mgB3jkSuK8.W8wV0v8J5K6L7M8N9O', 'Ưu Thị T', '0990123457', '1515 Đường Tạ Uyên, Quận 11, TP HCM', 'USER', true, NOW(), NOW());

-- 2. INSERT DATA VÀO BẢNG CATEGORIES
-- =====================================================
INSERT INTO categories (name, image_name, is_active) VALUES
('Sách Văn Học', 'van-hoc.jpg', true),
('Sách Kỹ Thuật', 'ky-thuat.jpg', true),
('Sách Kinh Tế', 'kinh-te.jpg', true),
('Sách Tâm Lý', 'tam-ly.jpg', true),
('Sách Lịch Sử', 'lich-su.jpg', true),
('Sách Khoa Học', 'khoa-hoc.jpg', true),
('Sách Ngoại Ngữ', 'ngoai-ngu.jpg', true),
('Sách Trẻ Em', 'tre-em.jpg', true);

-- 3. INSERT DATA VÀO BẢNG PRODUCTS
-- =====================================================
INSERT INTO products (title, description, price, discount_price, discount, stock, image, is_active, category_id) VALUES
-- Sách Văn Học (category_id = 1)
('Cô Gái Đến Từ Hàn Quốc', 'Tác phẩm lãng mạn về tình yêu giữa hai nước', 85000, 68000, 20, 50, 'co-gai-han-quoc.jpg', true, 1),
('Tâm Trạng Một Con Người', 'Tiểu thuyết tâm lý về đời sống con người', 95000, 76000, 20, 35, 'tam-trang-con-nguoi.jpg', true, 1),
('Chuyện Tình Mùa Đông', 'Câu chuyện lãng mạn trong mùa tuyết trắng', 78000, 62400, 20, 45, 'chuyen-tinh-mua-dong.jpg', true, 1),
('Người Thay Đổi Thế Giới', 'Tiểu thuyết kỳ lạ về những người bất thường', 125000, 100000, 20, 25, 'nguoi-thay-doi-the-gioi.jpg', true, 1),

-- Sách Kỹ Thuật (category_id = 2)
('Java Programming Advanced', 'Hướng dẫn lập trình Java nâng cao từ cơ bản đến chuyên sâu', 320000, 256000, 20, 60, 'java-advanced.jpg', true, 2),
('Spring Boot in Practice', 'Thực hành Spring Boot từ A đến Z', 285000, 228000, 20, 40, 'spring-boot-practice.jpg', true, 2),
('Database Design Patterns', 'Thiết kế cơ sở dữ liệu hiệu quả', 295000, 236000, 20, 30, 'database-design.jpg', true, 2),
('Web Development Mastery', 'Thành thạo phát triển web hiện đại', 310000, 248000, 20, 50, 'web-dev-mastery.jpg', true, 2),

-- Sách Kinh Tế (category_id = 3)
('Thay Đổi Tư Duy Kinh Doanh', 'Cách thay đổi cách suy nghĩ để thành công', 150000, 120000, 20, 70, 'tu-duy-kinh-doanh.jpg', true, 3),
('Chiến Lược Marketing Hiệu Quả', 'Các kỹ thuật marketing hiện đại', 165000, 132000, 20, 55, 'marketing-hieu-qua.jpg', true, 3),
('Lãnh Đạo Và Quản Lý', 'Nghệ thuật lãnh đạo đội ngũ', 180000, 144000, 20, 40, 'lanh-dao-quan-ly.jpg', true, 3),
('Tài Chính Cá Nhân', 'Quản lý tài chính để giàu có', 145000, 116000, 20, 65, 'tai-chinh-ca-nhan.jpg', true, 3),

-- Sách Tâm Lý (category_id = 4)
('Tâm Lý Học Hành Vi', 'Hiểu rõ hành vi con người', 165000, 132000, 20, 45, 'tam-ly-hanh-vi.jpg', true, 4),
('Sức Khỏe Tâm Thần', 'Chăm sóc sức khỏe tâm lý', 155000, 124000, 20, 50, 'suc-khoe-tam-than.jpg', true, 4),
('Tự Tin Và Tự Yêu Bản Thân', 'Xây dựng lòng tự tin mạnh mẽ', 135000, 108000, 20, 60, 'tu-tin-tu-yeu.jpg', true, 4),
('Điều Khiển Cảm Xúc', 'Kiểm soát cảm xúc hiệu quả', 140000, 112000, 20, 55, 'dieu-khien-cam-xuc.jpg', true, 4),

-- Sách Lịch Sử (category_id = 5)
('Lịch Sử Việt Nam', 'Những mốc son của lịch sử Việt Nam', 175000, 140000, 20, 50, 'lich-su-viet-nam.jpg', true, 5),
('Những Nhân Vật Lịch Sử Vĩ Đại', 'Tiểu sử những nhân vật lịch sử nổi tiếng', 185000, 148000, 20, 40, 'nhan-vat-lich-su.jpg', true, 5),
('Chiến Tranh Tư Liệu', 'Những sự kiện chiến tranh trong lịch sử', 195000, 156000, 20, 30, 'chien-tranh-tu-lieu.jpg', true, 5),

-- Sách Khoa Học (category_id = 6)
('Vũ Trụ Và Cuộc Sống', 'Khám phá bí mật của vũ trụ', 210000, 168000, 20, 40, 'vu-tru-cuoc-song.jpg', true, 6),
('Khoa Học Tự Nhiên', 'Nhập môn khoa học tự nhiên', 155000, 124000, 20, 60, 'khoa-hoc-tu-nhien.jpg', true, 6),
('Công Nghệ Sinh Học', 'Ứng dụng sinh học trong công nghệ', 235000, 188000, 20, 25, 'cong-nghe-sinh-hoc.jpg', true, 6),

-- Sách Ngoại Ngữ (category_id = 7)
('English Grammar Complete', 'Ngữ pháp tiếng Anh toàn diện', 165000, 132000, 20, 70, 'english-grammar.jpg', true, 7),
('Conversation English', 'Hội thoại tiếng Anh thực tế', 145000, 116000, 20, 50, 'conversation-english.jpg', true, 7),
('Vocabulary Master', 'Từ vựng tiếng Anh nâng cao', 135000, 108000, 20, 65, 'vocabulary-master.jpg', true, 7),

-- Sách Trẻ Em (category_id = 8)
('Chuyện Cổ Tích Dân Gian', 'Tuyển tập chuyện cổ tích hay', 75000, 60000, 20, 80, 'chuyen-co-tich.jpg', true, 8),
('Bé Học Màu Sắc', 'Sách dạy trẻ nhận biết màu sắc', 65000, 52000, 20, 100, 'be-hoc-mau-sac.jpg', true, 8),
('Những Câu Chuyện Giáo Dục', 'Câu chuyện dạy dỏ trẻ em', 85000, 68000, 20, 70, 'cau-chuyen-giao-duc.jpg', true, 8);

-- 4. INSERT DATA VÀO BẢNG ORDER_ADDRESS
-- =====================================================
INSERT INTO order_address (recipient_name, phone_number, address, district, city, postal_code) VALUES
('Nguyễn Văn A', '0987654321', '456 Đường Nguyễn Huệ', 'Quận 1', 'TP HCM', '70000'),
('Trần Thị B', '0912345678', '789 Đường Trần Hưng Đạo', 'Quận 1', 'TP HCM', '70001'),
('Lê Văn C', '0903456789', '321 Đường Phan Bội Châu', 'Quận 3', 'TP HCM', '70002'),
('Phạm Thị D', '0934567890', '654 Đường Võ Văn Kiệt', 'Quận 1', 'TP HCM', '70003'),
('Hoàng Văn E', '0945678901', '987 Đường Nguyễn Chí Thanh', 'Quận 2', 'TP HCM', '70004');

-- 5. INSERT DATA VÀO BẢNG ORDERS
-- =====================================================
INSERT INTO orders (order_id, order_date, status, payment_type, total_price, order_address_id, user_id) VALUES
-- Orders với trạng thái PENDING
('ORD20250119001', '2025-01-19 08:30:00', 'PENDING', 'CREDIT_CARD', 215000.00, 1, 3),
('ORD20250119002', '2025-01-19 09:15:00', 'PENDING', 'BANK_TRANSFER', 145000.00, 2, 4),

-- Orders với trạng thái CONFIRMED
('ORD20250118001', '2025-01-18 14:30:00', 'CONFIRMED', 'CREDIT_CARD', 485000.00, 3, 5),
('ORD20250118002', '2025-01-18 15:45:00', 'CONFIRMED', 'CASH_ON_DELIVERY', 320000.00, 4, 6),
('ORD20250118003', '2025-01-18 16:20:00', 'CONFIRMED', 'BANK_TRANSFER', 250000.00, 5, 7),

-- Orders với trạng thái SHIPPED
('ORD20250117001', '2025-01-17 10:00:00', 'SHIPPED', 'CREDIT_CARD', 175000.00, 1, 8),
('ORD20250117002', '2025-01-17 11:30:00', 'SHIPPED', 'BANK_TRANSFER', 295000.00, 2, 9),
('ORD20250117003', '2025-01-17 13:45:00', 'SHIPPED', 'CASH_ON_DELIVERY', 380000.00, 3, 10),

-- Orders với trạng thái DELIVERED
('ORD20250115001', '2025-01-15 09:00:00', 'DELIVERED', 'CREDIT_CARD', 450000.00, 4, 11),
('ORD20250115002', '2025-01-15 10:20:00', 'DELIVERED', 'BANK_TRANSFER', 265000.00, 5, 12),
('ORD20250115003', '2025-01-15 11:40:00', 'DELIVERED', 'CASH_ON_DELIVERY', 340000.00, 1, 13),
('ORD20250115004', '2025-01-15 14:15:00', 'DELIVERED', 'CREDIT_CARD', 520000.00, 2, 14),
('ORD20250115005', '2025-01-15 15:30:00', 'DELIVERED', 'BANK_TRANSFER', 195000.00, 3, 15),

-- Orders với trạng thái CANCELLED
('ORD20250116001', '2025-01-16 09:30:00', 'CANCELLED', 'CREDIT_CARD', 165000.00, 4, 16),
('ORD20250116002', '2025-01-16 10:45:00', 'CANCELLED', 'BANK_TRANSFER', 280000.00, 5, 17);

-- 6. INSERT DATA VÀO BẢNG ORDER_ITEMS
-- =====================================================
-- Order 1: ORD20250119001 (Văn Học)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 68000.00, 1, 1),
(2, 76000.00, 2, 1);

-- Order 2: ORD20250119002 (Kỹ Thuật)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 256000.00, 5, 2),
(1, 228000.00, 6, 2);

-- Order 3: ORD20250118001 (Kinh Tế)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 120000.00, 9, 3),
(2, 100000.00, 10, 3);

-- Order 4: ORD20250118002 (Tâm Lý)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 132000.00, 13, 4),
(1, 124000.00, 14, 4);

-- Order 5: ORD20250118003 (Lịch Sử)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 140000.00, 17, 5),
(1, 148000.00, 18, 5);

-- Order 6: ORD20250117001 (Khoa Học)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 168000.00, 21, 6),
(2, 124000.00, 22, 6);

-- Order 7: ORD20250117002 (Ngoại Ngữ)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 132000.00, 25, 7),
(1, 116000.00, 26, 7);

-- Order 8: ORD20250117003 (Trẻ Em)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(2, 60000.00, 27, 8),
(3, 52000.00, 28, 8);

-- Order 9: ORD20250115001 (Mixed)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 62400.00, 3, 9),
(2, 100000.00, 4, 9),
(1, 236000.00, 7, 9);

-- Order 10: ORD20250115002 (Mixed)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 108000.00, 15, 10),
(2, 112000.00, 16, 10);

-- Order 11: ORD20250115003 (Mixed)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 156000.00, 19, 11),
(1, 188000.00, 23, 11);

-- Order 12: ORD20250115004 (Mixed)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(2, 156000.00, 20, 12),
(1, 248000.00, 8, 12);

-- Order 13: ORD20250115005 (Mixed)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 108000.00, 24, 13),
(2, 68000.00, 29, 13);

-- Order 14: ORD20250116001 (Cancelled)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 165000.00, 11, 14);

-- Order 15: ORD20250116002 (Cancelled)
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 144000.00, 12, 15),
(1, 136000.00, 30, 15);

-- =====================================================
-- KIỂM TRA DỮ LIỆU ĐANG CÓ
-- =====================================================
-- SELECT COUNT(*) as user_count FROM users;
-- SELECT COUNT(*) as category_count FROM categories;
-- SELECT COUNT(*) as product_count FROM products;
-- SELECT COUNT(*) as order_count FROM orders;
-- SELECT COUNT(*) as order_item_count FROM order_items;
