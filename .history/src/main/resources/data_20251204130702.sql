-- =====================================================
-- INSERT DATA - MASTER ACCOUNT + INITIAL DATA
-- =====================================================

-- =====================================================
-- 1. INSERT DATA VÀO BẢNG USERS - MASTER ACCOUNT
-- =====================================================
INSERT INTO users (username, email, password, full_name, phone_number, address, role, is_active, created_at, updated_at) VALUES
('master', 'master@bookstore.com', '$2a$10$slYQmyNdGzin7olVN3p5be4DlH.PKZbv5H8KnzzVgXXbVxzy4QAZG', 'Master Administrator', '0123456789', '100 Đường Lê Lợi, Quận 1, TP HCM', 'MASTER', true, NOW(), NOW());

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
