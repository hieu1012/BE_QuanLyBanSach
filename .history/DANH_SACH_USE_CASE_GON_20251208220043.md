# Danh Sách Use Case và Mô Tả - Hệ Thống Quản Lý Bán Sách

## 2.3 Danh sách Use case và mô tả

| ID | Tên Use case | Mô tả ngắn gọn Use case | Chức năng | Ghi chú |
|----|----|----|----|---|
| UC001 | Đăng ký | Cho phép người dùng mới tạo tài khoản với username, password, email | Xác thực | Bắt buộc có email hợp lệ |
| UC002 | Đăng nhập | Xác thực username/password và cấp JWT token để truy cập hệ thống | Xác thực | Kiểm tra tài khoản có bị khóa không |
| UC003 | Quên/Đặt lại mật khẩu | Cho phép người dùng reset mật khẩu thông qua email xác minh | Xác thực | Gửi link reset qua email |
| UC004 | Đổi mật khẩu | Cho phép người dùng đã đăng nhập thay đổi mật khẩu | Tài khoản | Cần xác minh mật khẩu cũ |
| UC005 | Xem/Cập nhật hồ sơ cá nhân | Hiển thị và cho phép cập nhật thông tin hồ sơ (tên, email, điện thoại, địa chỉ) | Tài khoản | Không được phép đổi username |
| UC006 | Xem danh sách sách | Hiển thị danh sách sách có trong hệ thống với phân trang, tìm kiếm, lọc theo danh mục | Quản lý sách | Tất cả người dùng có thể xem |
| UC007 | Xem chi tiết sách | Hiển thị thông tin chi tiết sách: mô tả, tác giả, giá, hình ảnh, đánh giá | Quản lý sách | Cho phép người dùng xem trước |
| UC008 | Thêm/Sửa/Xóa sách (Admin) | Cho phép Admin thêm sách mới, cập nhật thông tin, xóa sách khỏi hệ thống | Quản lý sách | Chỉ Admin và Master có quyền |
| UC009 | Xem danh mục | Hiển thị danh sách danh mục sách | Quản lý danh mục | Tất cả người dùng có thể xem |
| UC010 | Thêm/Sửa/Xóa danh mục (Admin) | Cho phép Admin quản lý danh mục: tạo mới, sửa, xóa | Quản lý danh mục | Chỉ Admin và Master có quyền |
| UC011 | Quản lý giỏ hàng | Cho phép người dùng xem giỏ, thêm sách, cập nhật số lượng, xóa sách khỏi giỏ | Giỏ hàng | Cập nhật tự động tổng tiền |
| UC012 | Thanh toán/Tạo đơn hàng | Cho phép người dùng thanh toán giỏ hàng và tạo đơn hàng | Đơn hàng | Hỗ trợ COD và thanh toán trực tuyến |
| UC013 | Xem lịch sử/Chi tiết đơn hàng | Cho phép người dùng xem danh sách đơn hàng của mình và chi tiết từng đơn | Đơn hàng | Chỉ xem đơn của chính mình |
| UC014 | Quản lý đơn hàng (Admin) | Cho phép Admin xem tất cả đơn hàng, cập nhật trạng thái, duyệt/từ chối hủy đơn | Đơn hàng | Chỉ Admin và Master có quyền |
| UC015 | Thống kê doanh thu | Hiển thị thống kê doanh thu theo ngày/tháng, khách hàng top, sản phẩm bán chạy | Thống kê | Chỉ Admin và Master có quyền |
| UC016 | Quản lý người dùng (Master) | Cho phép Master xem danh sách user, tạo/sửa/xóa user, khóa/mở khóa, gán quyền | Quản lý user | Chỉ Master có quyền |

---

## Bảng Phân Quyền Chi Tiết

| Use Case | USER | ADMIN | MASTER |
|----------|------|-------|--------|
| UC001 - Đăng ký | ✅ | ✅ | ✅ |
| UC002 - Đăng nhập | ✅ | ✅ | ✅ |
| UC003 - Quên/Đặt lại mật khẩu | ✅ | ✅ | ✅ |
| UC004 - Đổi mật khẩu | ✅ | ✅ | ✅ |
| UC005 - Xem/Cập nhật hồ sơ | ✅ | ✅ | ✅ |
| UC006 - Xem danh sách sách | ✅ | ✅ | ✅ |
| UC007 - Xem chi tiết sách | ✅ | ✅ | ✅ |
| UC008 - Thêm/Sửa/Xóa sách | ❌ | ✅ | ✅ |
| UC009 - Xem danh mục | ✅ | ✅ | ✅ |
| UC010 - Thêm/Sửa/Xóa danh mục | ❌ | ✅ | ✅ |
| UC011 - Quản lý giỏ hàng | ✅ | ✅ | ✅ |
| UC012 - Thanh toán/Tạo đơn | ✅ | ✅ | ✅ |
| UC013 - Xem lịch sử/Chi tiết đơn | ✅ | ✅ | ✅ |
| UC014 - Quản lý đơn hàng (Admin) | ❌ | ✅ | ✅ |
| UC015 - Thống kê doanh thu | ❌ | ✅ | ✅ |
| UC016 - Quản lý người dùng | ❌ | ❌ | ✅ |

---

## Tóm Tắt

**Tổng cộng: 16 Use Cases (gộp chung các chức năng liên quan)**

- 👤 **USER**: 7 UC (Đăng ký, Đăng nhập, Hồ sơ, Xem sách, Giỏ hàng, Đơn hàng)
- 👨‍💼 **ADMIN**: 15 UC (7 USER + 8 Admin-only)
- 👑 **MASTER**: 16 UC (15 ADMIN + 1 Master-only)

### Ghi chú về gộp chung:
- **UC008**: Gộp "Thêm/Sửa/Xóa sách" thành 1 chức năng
- **UC010**: Gộp "Thêm/Sửa/Xóa danh mục" thành 1 chức năng
- **UC011**: Gộp "Xem/Thêm/Cập nhật/Xóa giỏ" thành 1 chức năng
- **UC013**: Gộp "Xem lịch sử + Chi tiết" thành 1 chức năng
- **UC015**: Gộp "Thống kê theo ngày/tháng/khách/sản phẩm" thành 1 chức năng
- **UC016**: Gộp "Xem/Tạo/Sửa/Xóa/Khóa user + Gán quyền" thành 1 chức năng
