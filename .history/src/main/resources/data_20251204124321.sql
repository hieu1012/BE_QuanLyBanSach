-- =====================================================
-- SEED DATA FOR APPLICATION STARTUP
-- This file is automatically executed by Spring after schema creation
-- =====================================================

-- =====================================================
-- 1. INSERT DATA VÀO BẢNG USERS
-- =====================================================
-- NOTE: All passwords are hashed using BCrypt (Spring Security)
-- Default password for all accounts: 1111
-- =====================================================
INSERT IGNORE INTO users (id, username, email, password, full_name, phone_number, address, role, is_active, created_at, updated_at) VALUES
-- Admin users (Password: 1111)
(1, 'admin01', 'admin01@bookstore.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Quản Trị Viên 1', '0123456789', '123 Đường Lê Lợi, Quận 1, TP HCM', 'ADMIN', true, NOW(), NOW()),
(2, 'admin02', 'admin02@bookstore.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Quản Trị Viên 2', '0123456788', '124 Đường Nguyễn Huệ, Quận 1, TP HCM', 'ADMIN', true, NOW(), NOW()),

-- Customer users (Password: 1111)
(3, 'customer01', 'customer01@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Nguyễn Văn A', '0987654321', '456 Đường Nguyễn Huệ, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
(4, 'customer02', 'customer02@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Trần Thị B', '0912345678', '789 Đường Trần Hưng Đạo, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
(5, 'customer03', 'customer03@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Lê Văn C', '0903456789', '321 Đường Phan Bội Châu, Quận 3, TP HCM', 'USER', true, NOW(), NOW()),
(6, 'customer04', 'customer04@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Phạm Thị D', '0934567890', '654 Đường Võ Văn Kiệt, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
(7, 'customer05', 'customer05@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Hoàng Văn E', '0945678901', '987 Đường Nguyễn Chí Thanh, Quận 2, TP HCM', 'USER', true, NOW(), NOW()),
(8, 'customer06', 'customer06@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Võ Thị F', '0956789012', '111 Đường Lý Tự Trọng, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
(9, 'customer07', 'customer07@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Dương Văn G', '0967890123', '222 Đường Ngô Đức Kế, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
(10, 'customer08', 'customer08@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Bùi Thị H', '0978901234', '333 Đường Bạch Đằng, Quận 3, TP HCM', 'USER', true, NOW(), NOW()),
(11, 'customer09', 'customer09@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Hồ Văn I', '0989012345', '444 Đường Ký Con, Quận 1, TP HCM', 'USER', true, NOW(), NOW()),
(12, 'customer10', 'customer10@gmail.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Tạ Thị J', '0990123456', '555 Đường Hàm Nghi, Quận 1, TP HCM', 'USER', true, NOW(), NOW());

-- =====================================================
-- 2. INSERT DATA VÀO BẢNG CATEGORIES
-- =====================================================
INSERT IGNORE INTO categories (id, name, is_active) VALUES
(1, 'Sách Văn Học', true),
(2, 'Sách Kỹ Thuật', true),
(3, 'Sách Kinh Tế', true),
(4, 'Sách Tâm Lý', true),
(5, 'Sách Lịch Sử', true),
(6, 'Sách Khoa Học', true),
(7, 'Sách Ngoại Ngữ', true),
(8, 'Sách Trẻ Em', true);

-- =====================================================
-- 3. INSERT DATA VÀO BẢNG PRODUCTS
-- =====================================================
INSERT IGNORE INTO products (id, title, description, price, discount_price, discount, stock, image_names, category_id, is_active) VALUES
-- Sách Văn Học (category_id = 1)
(1, 'Cô Gái Đến Từ Hàn Quốc', 'Tác phẩm lãng mạn về tình yêu giữa hai nước', 85000, 68000, 20, 50, '["co-gai-han-quoc.jpg"]', 1, true),
(2, 'Tâm Trạng Một Con Người', 'Tiểu thuyết tâm lý về đời sống con người', 95000, 76000, 20, 35, '["tam-trang-con-nguoi.jpg"]', 1, true),
(3, 'Chuyện Tình Mùa Đông', 'Câu chuyện lãng mạn trong mùa tuyết trắng', 78000, 62400, 20, 45, '["chuyen-tinh-mua-dong.jpg"]', 1, true),
(4, 'Người Thay Đổi Thế Giới', 'Tiểu thuyết kỳ lạ về những người bất thường', 125000, 100000, 20, 25, '["nguoi-thay-doi-the-gioi.jpg"]', 1, true),

-- Sách Kỹ Thuật (category_id = 2)
(5, 'Java Programming Advanced', 'Hướng dẫn lập trình Java nâng cao từ cơ bản đến chuyên sâu', 320000, 256000, 20, 60, '["java-advanced.jpg"]', 2, true),
(6, 'Spring Boot in Practice', 'Thực hành Spring Boot từ A đến Z', 285000, 228000, 20, 40, '["spring-boot-practice.jpg"]', 2, true),
(7, 'Database Design Patterns', 'Thiết kế cơ sở dữ liệu hiệu quả', 295000, 236000, 20, 30, '["database-design.jpg"]', 2, true),
(8, 'Web Development Mastery', 'Thành thạo phát triển web hiện đại', 310000, 248000, 20, 50, '["web-dev-mastery.jpg"]', 2, true),

-- Sách Kinh Tế (category_id = 3)
(9, 'Thay Đổi Tư Duy Kinh Doanh', 'Cách thay đổi cách suy nghĩ để thành công', 150000, 120000, 20, 70, '["tu-duy-kinh-doanh.jpg"]', 3, true),
(10, 'Chiến Lược Marketing Hiệu Quả', 'Các kỹ thuật marketing hiện đại', 165000, 132000, 20, 55, '["marketing-hieu-qua.jpg"]', 3, true),
(11, 'Lãnh Đạo Và Quản Lý', 'Nghệ thuật lãnh đạo đội ngũ', 180000, 144000, 20, 40, '["lanh-dao-quan-ly.jpg"]', 3, true),
(12, 'Tài Chính Cá Nhân', 'Quản lý tài chính để giàu có', 145000, 116000, 20, 65, '["tai-chinh-ca-nhan.jpg"]', 3, true),

-- Sách Tâm Lý (category_id = 4)
(13, 'Tâm Lý Học Hành Vi', 'Hiểu rõ hành vi con người', 165000, 132000, 20, 45, '["tam-ly-hanh-vi.jpg"]', 4, true),
(14, 'Sức Khỏe Tâm Thần', 'Chăm sóc sức khỏe tâm lý', 155000, 124000, 20, 50, '["suc-khoe-tam-than.jpg"]', 4, true),
(15, 'Tự Tin Và Tự Yêu Bản Thân', 'Xây dựng lòng tự tin mạnh mẽ', 135000, 108000, 20, 60, '["tu-tin-tu-yeu.jpg"]', 4, true),
(16, 'Điều Khiển Cảm Xúc', 'Kiểm soát cảm xúc hiệu quả', 140000, 112000, 20, 55, '["dieu-khien-cam-xuc.jpg"]', 4, true),

-- Sách Lịch Sử (category_id = 5)
(17, 'Lịch Sử Việt Nam', 'Những mốc son của lịch sử Việt Nam', 175000, 140000, 20, 50, '["lich-su-viet-nam.jpg"]', 5, true),
(18, 'Những Nhân Vật Lịch Sử Vĩ Đại', 'Tiểu sử những nhân vật lịch sử nổi tiếng', 185000, 148000, 20, 40, '["nhan-vat-lich-su.jpg"]', 5, true),
(19, 'Chiến Tranh Tư Liệu', 'Những sự kiện chiến tranh trong lịch sử', 195000, 156000, 20, 30, '["chien-tranh-tu-lieu.jpg"]', 5, true),

-- Sách Khoa Học (category_id = 6)
(20, 'Vũ Trụ Và Cuộc Sống', 'Khám phá bí mật của vũ trụ', 210000, 168000, 20, 40, '["vu-tru-cuoc-song.jpg"]', 6, true),
(21, 'Khoa Học Tự Nhiên', 'Nhập môn khoa học tự nhiên', 155000, 124000, 20, 60, '["khoa-hoc-tu-nhien.jpg"]', 6, true),
(22, 'Công Nghệ Sinh Học', 'Ứng dụng sinh học trong công nghệ', 235000, 188000, 20, 25, '["cong-nghe-sinh-hoc.jpg"]', 6, true),

-- Sách Ngoại Ngữ (category_id = 7)
(23, 'English Grammar Complete', 'Ngữ pháp tiếng Anh toàn diện', 165000, 132000, 20, 70, '["english-grammar.jpg"]', 7, true),
(24, 'Conversation English', 'Hội thoại tiếng Anh thực tế', 145000, 116000, 20, 50, '["conversation-english.jpg"]', 7, true),
(25, 'Vocabulary Master', 'Từ vựng tiếng Anh nâng cao', 135000, 108000, 20, 65, '["vocabulary-master.jpg"]', 7, true),

-- Sách Trẻ Em (category_id = 8)
(26, 'Chuyện Cổ Tích Dân Gian', 'Tuyển tập chuyện cổ tích hay', 75000, 60000, 20, 80, '["chuyen-co-tich.jpg"]', 8, true),
(27, 'Bé Học Màu Sắc', 'Sách dạy trẻ nhận biết màu sắc', 65000, 52000, 20, 100, '["be-hoc-mau-sac.jpg"]', 8, true),
(28, 'Những Câu Chuyện Giáo Dục', 'Câu chuyện dạy dỏ trẻ em', 85000, 68000, 20, 70, '["cau-chuyen-giao-duc.jpg"]', 8, true);

-- =====================================================
-- 4. INSERT DATA VÀO BẢNG CARTS
-- =====================================================
INSERT IGNORE INTO carts (id, user_id, total_amount, created_at, updated_at) VALUES
(1, 3, 308000.00, NOW(), NOW()),
(2, 4, 132000.00, NOW(), NOW());

-- =====================================================
-- 5. INSERT DATA VÀO BẢNG CART_ITEMS
-- =====================================================
INSERT IGNORE INTO cart_items (cart_id, product_id, quantity, unit_price) VALUES
(1, 15, 1, 108000.00),
(1, 16, 1, 112000.00),
(1, 13, 1, 132000.00),
(2, 10, 1, 132000.00);
