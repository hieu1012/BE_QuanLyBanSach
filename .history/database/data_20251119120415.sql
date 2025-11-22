-- =====================================================
-- TEST DATA FOR QUẢN LÝ BÁN SÁCH
-- Database: quanlybansach
-- Created: 19/11/2025
-- =====================================================

-- =====================================================
-- XÓA DỮ LIỆU CŨ (NẾU CẦN)
-- =====================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
TRUNCATE TABLE order_addresses;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 1. INSERT DATA VÀO BẢNG USERS
-- =====================================================
INSERT INTO users (username, email, password, full_name, phone_number, address, role, is_active, created_at, updated_at) VALUES
-- Admin users
('admin01', 'admin01@bookstore.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Quản Trị Viên 1', '0123456789', '123 Đường Lê Lợi, Quận 1, TP HCM', 'ADMIN', true, NOW(), NOW()),
('admin02', 'admin02@bookstore.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Quản Trị Viên 2', '0123456788', '124 Đường Nguyễn Huệ, Quận 1, TP HCM', 'ADMIN', true, NOW(), NOW()),

-- Customer users (20 customers)
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
('Sách Văn Học', 1),
('Sách Kỹ Thuật', 1),
('Sách Kinh Tế', 1),
('Sách Tâm Lý', 1),
('Sách Lịch Sử', 1),
('Sách Khoa Học', 1),
('Sách Ngoại Ngữ', 1),
('Sách Trẻ Em', 1);

-- =====================================================
-- 3. INSERT DATA VÀO BẢNG PRODUCTS
-- =====================================================
INSERT INTO products (title, description, price, discount_price, discount, stock, image, is_active, category_id) VALUES
-- Sách Văn Học (category_id = 1)
('Cô Gái Đến Từ Hàn Quốc', 'Tác phẩm lãng mạn về tình yêu giữa hai nước', 85000, 68000, 20, 50, 'co-gai-han-quoc.jpg', 1, 1),
('Tâm Trạng Một Con Người', 'Tiểu thuyết tâm lý về đời sống con người', 95000, 76000, 20, 35, 'tam-trang-con-nguoi.jpg', 1, 1),
('Chuyện Tình Mùa Đông', 'Câu chuyện lãng mạn trong mùa tuyết trắng', 78000, 62400, 20, 45, 'chuyen-tinh-mua-dong.jpg', 1, 1),
('Người Thay Đổi Thế Giới', 'Tiểu thuyết kỳ lạ về những người bất thường', 125000, 100000, 20, 25, 'nguoi-thay-doi-the-gioi.jpg', 1, 1),

-- Sách Kỹ Thuật (category_id = 2)
('Java Programming Advanced', 'Hướng dẫn lập trình Java nâng cao từ cơ bản đến chuyên sâu', 320000, 256000, 20, 60, 'java-advanced.jpg', 1, 2),
('Spring Boot in Practice', 'Thực hành Spring Boot từ A đến Z', 285000, 228000, 20, 40, 'spring-boot-practice.jpg', 1, 2),
('Database Design Patterns', 'Thiết kế cơ sở dữ liệu hiệu quả', 295000, 236000, 20, 30, 'database-design.jpg', 1, 2),
('Web Development Mastery', 'Thành thạo phát triển web hiện đại', 310000, 248000, 20, 50, 'web-dev-mastery.jpg', 1, 2),

-- Sách Kinh Tế (category_id = 3)
('Thay Đổi Tư Duy Kinh Doanh', 'Cách thay đổi cách suy nghĩ để thành công', 150000, 120000, 20, 70, 'tu-duy-kinh-doanh.jpg', 1, 3),
('Chiến Lược Marketing Hiệu Quả', 'Các kỹ thuật marketing hiện đại', 165000, 132000, 20, 55, 'marketing-hieu-qua.jpg', 1, 3),
('Lãnh Đạo Và Quản Lý', 'Nghệ thuật lãnh đạo đội ngũ', 180000, 144000, 20, 40, 'lanh-dao-quan-ly.jpg', 1, 3),
('Tài Chính Cá Nhân', 'Quản lý tài chính để giàu có', 145000, 116000, 20, 65, 'tai-chinh-ca-nhan.jpg', 1, 3),

-- Sách Tâm Lý (category_id = 4)
('Tâm Lý Học Hành Vi', 'Hiểu rõ hành vi con người', 165000, 132000, 20, 45, 'tam-ly-hanh-vi.jpg', 1, 4),
('Sức Khỏe Tâm Thần', 'Chăm sóc sức khỏe tâm lý', 155000, 124000, 20, 50, 'suc-khoe-tam-than.jpg', 1, 4),
('Tự Tin Và Tự Yêu Bản Thân', 'Xây dựng lòng tự tin mạnh mẽ', 135000, 108000, 20, 60, 'tu-tin-tu-yeu.jpg', 1, 4),
('Điều Khiển Cảm Xúc', 'Kiểm soát cảm xúc hiệu quả', 140000, 112000, 20, 55, 'dieu-khien-cam-xuc.jpg', 1, 4),

-- Sách Lịch Sử (category_id = 5)
('Lịch Sử Việt Nam', 'Những mốc son của lịch sử Việt Nam', 175000, 140000, 20, 50, 'lich-su-viet-nam.jpg', 1, 5),
('Những Nhân Vật Lịch Sử Vĩ Đại', 'Tiểu sử những nhân vật lịch sử nổi tiếng', 185000, 148000, 20, 40, 'nhan-vat-lich-su.jpg', 1, 5),
('Chiến Tranh Tư Liệu', 'Những sự kiện chiến tranh trong lịch sử', 195000, 156000, 20, 30, 'chien-tranh-tu-lieu.jpg', 1, 5),

-- Sách Khoa Học (category_id = 6)
('Vũ Trụ Và Cuộc Sống', 'Khám phá bí mật của vũ trụ', 210000, 168000, 20, 40, 'vu-tru-cuoc-song.jpg', 1, 6),
('Khoa Học Tự Nhiên', 'Nhập môn khoa học tự nhiên', 155000, 124000, 20, 60, 'khoa-hoc-tu-nhien.jpg', 1, 6),
('Công Nghệ Sinh Học', 'Ứng dụng sinh học trong công nghệ', 235000, 188000, 20, 25, 'cong-nghe-sinh-hoc.jpg', 1, 6),

-- Sách Ngoại Ngữ (category_id = 7)
('English Grammar Complete', 'Ngữ pháp tiếng Anh toàn diện', 165000, 132000, 20, 70, 'english-grammar.jpg', 1, 7),
('Conversation English', 'Hội thoại tiếng Anh thực tế', 145000, 116000, 20, 50, 'conversation-english.jpg', 1, 7),
('Vocabulary Master', 'Từ vựng tiếng Anh nâng cao', 135000, 108000, 20, 65, 'vocabulary-master.jpg', 1, 7),

-- Sách Trẻ Em (category_id = 8)
('Chuyện Cổ Tích Dân Gian', 'Tuyển tập chuyện cổ tích hay', 75000, 60000, 20, 80, 'chuyen-co-tich.jpg', 1, 8),
('Bé Học Màu Sắc', 'Sách dạy trẻ nhận biết màu sắc', 65000, 52000, 20, 100, 'be-hoc-mau-sac.jpg', 1, 8),
('Những Câu Chuyện Giáo Dục', 'Câu chuyện dạy dỏ trẻ em', 85000, 68000, 20, 70, 'cau-chuyen-giao-duc.jpg', 1, 8);

-- =====================================================
-- 4. INSERT DATA VÀO BẢNG ORDER_ADDRESSES
-- =====================================================
INSERT INTO order_addresses (first_name, last_name, email, mobile_no, address, city, state, pincode) VALUES
('Nguyễn', 'Văn A', 'nguyenvana@gmail.com', '0987654321', '456 Đường Nguyễn Huệ', 'TP HCM', 'Quận 1', '70000'),
('Trần', 'Thị B', 'tranthib@gmail.com', '0912345678', '789 Đường Trần Hưng Đạo', 'TP HCM', 'Quận 1', '70001'),
('Lê', 'Văn C', 'levanc@gmail.com', '0903456789', '321 Đường Phan Bội Châu', 'TP HCM', 'Quận 3', '70002'),
('Phạm', 'Thị D', 'phamthid@gmail.com', '0934567890', '654 Đường Võ Văn Kiệt', 'TP HCM', 'Quận 1', '70003'),
('Hoàng', 'Văn E', 'hoangvane@gmail.com', '0945678901', '987 Đường Nguyễn Chí Thanh', 'TP HCM', 'Quận 2', '70004');

-- =====================================================
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
('ORD20250115002', '2025-01-15 10:20:00', 'DELIVERED', 'BANK_TRANSFER', 265000.00, 5, 12);

-- =====================================================
-- 6. INSERT DATA VÀO BẢNG ORDER_ITEMS
-- =====================================================
-- Order 1: ORD20250119001
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(1, 1, 1, 68000.00),
(1, 2, 2, 76000.00);

-- Order 2: ORD20250119002
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(2, 5, 1, 256000.00),
(2, 6, 1, 228000.00);

-- Order 3: ORD20250118001
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(3, 9, 1, 120000.00),
(3, 10, 2, 100000.00);

-- Order 4: ORD20250118002
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(4, 13, 1, 132000.00),
(4, 14, 1, 124000.00);

-- Order 5: ORD20250118003
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(5, 17, 1, 140000.00),
(5, 18, 1, 148000.00);

-- Order 6: ORD20250117001
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(6, 21, 1, 168000.00),
(6, 22, 2, 124000.00);

-- Order 7: ORD20250117002
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(7, 25, 1, 132000.00),
(7, 26, 1, 116000.00);

-- Order 8: ORD20250117003
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(8, 27, 2, 60000.00),
(8, 28, 3, 52000.00);

-- Order 9: ORD20250115001
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(9, 3, 1, 62400.00),
(9, 4, 2, 100000.00),
(9, 7, 1, 236000.00);

-- Order 10: ORD20250115002
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(10, 15, 1, 108000.00),
(10, 16, 2, 112000.00);
