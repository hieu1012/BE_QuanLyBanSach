-- =====================================================
-- DATA TEST CHO HỆ THỐNG QUẢN LÝ BÁN SÁCH
-- =====================================================

-- 1. INSERT DATA VÀO BẢNG USERS
-- =====================================================
INSERT INTO users (username, email, password, full_name, phone_number, address, role, is_active, created_at, updated_at) VALUES
-- Admin user
('admin', 'admin@bookstore.com', '$2a$10$YJ.7q3s8.V8N5YP5yQ5fweCJ5z3vV0vqY9L8K3K5M5M5M5M5M5M5M', 'Quản Trị Viên', '0123456789', '123 Đường Lê Lợi, Quận 1, TP HCM', 'ADMIN', true, NOW(), NOW()),

-- Customer users
('customer1', 'customer1@gmail.com', '$2a$10$YJ.7q3s8.V8N5YP5yQ5fweCJ5z3vV0vqY9L8K3K5M5M5M5M5M5M5M', 'Nguyễn Văn A', '0987654321', '456 Đường Nguyễn Huệ, Quận 1, TP HCM', 'CUSTOMER', true, NOW(), NOW()),
('customer2', 'customer2@gmail.com', '$2a$10$YJ.7q3s8.V8N5YP5yQ5fweCJ5z3vV0vqY9L8K3K5M5M5M5M5M5M5M', 'Trần Thị B', '0912345678', '789 Đường Trần Hưng Đạo, Quận 1, TP HCM', 'CUSTOMER', true, NOW(), NOW()),
('customer3', 'customer3@gmail.com', '$2a$10$YJ.7q3s8.V8N5YP5yQ5fweCJ5z3vV0vqY9L8K3K5M5M5M5M5M5M5M', 'Lê Văn C', '0903456789', '321 Đường Phan Bội Châu, Quận 3, TP HCM', 'CUSTOMER', true, NOW(), NOW()),
('customer4', 'customer4@gmail.com', '$2a$10$YJ.7q3s8.V8N5YP5yQ5fweCJ5z3vV0vqY9L8K3K5M5M5M5M5M5M5M', 'Phạm Thị D', '0934567890', '654 Đường Võ Văn Kiệt, Quận 1, TP HCM', 'CUSTOMER', true, NOW(), NOW()),
('customer5', 'customer5@gmail.com', '$2a$10$YJ.7q3s8.V8N5YP5yQ5fweCJ5z3vV0vqY9L8K3K5M5M5M5M5M5M5M', 'Hoàng Văn E', '0945678901', '987 Đường Nguyễn Chí Thanh, Quận 2, TP HCM', 'CUSTOMER', true, NOW(), NOW());

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
('ORD001', '2025-01-10 10:30:00', 'PENDING', 'CREDIT_CARD', 215000, 1, 2),
('ORD002', '2025-01-11 14:15:00', 'CONFIRMED', 'BANK_TRANSFER', 485000, 2, 3),
('ORD003', '2025-01-12 09:45:00', 'SHIPPED', 'CASH_ON_DELIVERY', 320000, 3, 4),
('ORD004', '2025-01-12 16:20:00', 'DELIVERED', 'CREDIT_CARD', 175000, 4, 5),
('ORD005', '2025-01-13 11:00:00', 'PENDING', 'BANK_TRANSFER', 295000, 5, 2),
('ORD006', '2025-01-13 15:30:00', 'CONFIRMED', 'CASH_ON_DELIVERY', 450000, 1, 3),
('ORD007', '2025-01-14 08:45:00', 'CANCELLED', 'CREDIT_CARD', 165000, 2, 4),
('ORD008', '2025-01-14 13:20:00', 'DELIVERED', 'BANK_TRANSFER', 380000, 3, 5);

-- 6. INSERT DATA VÀO BẢNG ORDER_ITEMS
-- =====================================================
-- Order 1: ORD001
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 68000, 1, 1),
(2, 76000, 2, 1);

-- Order 2: ORD002
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 256000, 5, 2),
(1, 228000, 6, 2);

-- Order 3: ORD003
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 120000, 9, 3),
(2, 100000, 10, 3);

-- Order 4: ORD004
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 140000, 11, 4),
(1, 35000, 27, 4);

-- Order 5: ORD005
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 236000, 7, 5),
(1, 59000, 3, 5);

-- Order 6: ORD006
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(2, 132000, 13, 6),
(1, 188000, 22, 6);

-- Order 7: ORD007
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 165000, 15, 7);

-- Order 8: ORD008
INSERT INTO order_items (quantity, price, product_id, order_id) VALUES
(1, 132000, 19, 8),
(2, 124000, 20, 8);

-- =====================================================
-- KIỂM TRA DỮ LIỆU ĐANG CÓ
-- =====================================================
-- SELECT COUNT(*) as user_count FROM users;
-- SELECT COUNT(*) as category_count FROM categories;
-- SELECT COUNT(*) as product_count FROM products;
-- SELECT COUNT(*) as order_count FROM orders;
-- SELECT COUNT(*) as order_item_count FROM order_items;
