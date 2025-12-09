# 📊 PHÂN TÍCH SỐ LƯỢNG USE CASE TỐI THIỂU
## Hệ Thống Quản Lý Bán Sách - 3 Actor (User, Admin, Master)

---

## 📈 THỐNG KÊ CHỨC NĂNG HIỆN CÓ

### **Từ Danh Sách Controller:**

| Controller | Số Chức Năng |
|-----------|------------|
| AuthController | 4 |
| UserController | 7 |
| BookController | 5 |
| CategoryController | 4 |
| CartController | 4 |
| OrderController | 4 |
| StatisticsController | 4 |
| **TỔNG CỘNG** | **32 chức năng** |

---

## 🎯 PHÂN LOẠI USE CASE THEO ACTOR

### **1. USER (Khách Hàng)**
**Chức năng được phép:**

| # | Use Case | Loại |
|---|----------|------|
| UC1 | Đăng ký | Xác thực |
| UC2 | Đăng nhập | Xác thực |
| UC3 | Quên mật khẩu | Xác thực |
| UC4 | Đặt lại mật khẩu | Xác thực |
| UC5 | Xem hồ sơ | Tài khoản |
| UC6 | Cập nhật hồ sơ | Tài khoản |
| UC7 | Đổi mật khẩu | Tài khoản |
| UC8 | Xem danh sách sách | Sách |
| UC9 | Xem chi tiết sách | Sách |
| UC10 | Xem danh mục | Danh mục |
| UC11 | Xem giỏ hàng | Giỏ hàng |
| UC12 | Thêm vào giỏ | Giỏ hàng |
| UC13 | Cập nhật giỏ hàng | Giỏ hàng |
| UC14 | Xóa sản phẩm khỏi giỏ | Giỏ hàng |
| UC15 | Thanh toán | Giỏ hàng |
| UC16 | Xem lịch sử đơn | Đơn hàng |
| UC17 | Xem chi tiết đơn | Đơn hàng |

**Tổng: 17 Use Cases cho USER**

---

### **2. ADMIN (Quản Trị Viên)**
**Chức năng được phép (bao gồm USER + thêm):**

| # | Use Case | Loại | Ghi chú |
|---|----------|------|---------|
| UC1-UC7 | Xác thực & Tài khoản | ✅ | Kế thừa từ USER |
| UC8-UC10 | Xem sách/danh mục | ✅ | Kế thừa từ USER |
| UC18 | Thêm sách | Sách | Chỉ Admin |
| UC19 | Sửa sách | Sách | Chỉ Admin |
| UC20 | Xóa sách | Sách | Chỉ Admin |
| UC21 | Thêm danh mục | Danh mục | Chỉ Admin |
| UC22 | Sửa danh mục | Danh mục | Chỉ Admin |
| UC23 | Xóa danh mục | Danh mục | Chỉ Admin |
| UC24 | Quản lý đơn (xem tất cả) | Đơn hàng | Chỉ Admin |
| UC25 | Cập nhật đơn | Đơn hàng | Chỉ Admin |
| UC26 | Thống kê theo ngày | Thống kê | Chỉ Admin |
| UC27 | Thống kê theo tháng | Thống kê | Chỉ Admin |
| UC28 | Thống kê khách hàng | Thống kê | Chỉ Admin |
| UC29 | Thống kê sản phẩm | Thống kê | Chỉ Admin |

**Tổng: 7 Use Cases mới (+ 17 từ USER = 24 tổng cộng)**

---

### **3. MASTER (Quản Lý Hệ Thống)**
**Chức năng được phép (bao gồm ADMIN + thêm):**

| # | Use Case | Loại | Ghi chú |
|---|----------|------|---------|
| UC1-UC29 | Tất cả Admin | ✅ | Kế thừa từ ADMIN |
| UC30 | Xem danh sách user | Người dùng | Chỉ Master |
| UC31 | Xem chi tiết user | Người dùng | Chỉ Master |
| UC32 | Tạo user | Người dùng | Chỉ Master |
| UC33 | Sửa user | Người dùng | Chỉ Master |
| UC34 | Xóa user | Người dùng | Chỉ Master |

**Tổng: 6 Use Cases mới (+ 24 từ ADMIN = 30 tổng cộng)**

---

## 📊 TỔNG HỢP SỐ LƯỢNG USE CASE

### **Phân Loại Theo Chức Năng:**

```
🔐 XÁC THỰC: 4 UC
  - Đăng ký
  - Đăng nhập
  - Quên mật khẩu
  - Đặt lại mật khẩu

👤 TÀI KHOẢN NGƯỜI DÙNG: 3 UC
  - Xem hồ sơ
  - Cập nhật hồ sơ
  - Đổi mật khẩu

📚 SẢN PHẨM/SÁCH: 5 UC
  - Xem danh sách sách
  - Xem chi tiết sách
  - Thêm sách (Admin)
  - Sửa sách (Admin)
  - Xóa sách (Admin)

📑 DANH MỤC: 4 UC
  - Xem danh mục
  - Thêm danh mục (Admin)
  - Sửa danh mục (Admin)
  - Xóa danh mục (Admin)

🛒 GIỎ HÀNG: 5 UC
  - Xem giỏ hàng
  - Thêm vào giỏ
  - Cập nhật giỏ hàng
  - Xóa sản phẩm khỏi giỏ
  - Thanh toán

📋 ĐƠN HÀNG: 4 UC
  - Xem lịch sử đơn hàng (User)
  - Xem chi tiết đơn hàng (User)
  - Quản lý đơn/xem tất cả (Admin)
  - Cập nhật đơn (Admin)

👥 NGƯỜI DÙNG: 5 UC (Master Only)
  - Xem danh sách user
  - Xem chi tiết user
  - Tạo user
  - Sửa user
  - Xóa user

📊 THỐNG KÊ: 4 UC (Admin+)
  - Thống kê theo ngày
  - Thống kê theo tháng
  - Thống kê khách hàng
  - Thống kê sản phẩm
```

---

## ✅ KẾT LUẬN: SỐ LƯỢNG USE CASE TỐI THIỂU

### **Tổng cộng: 30 Use Cases**

**Phân bố theo Actor:**
- 👤 **USER**: 17 UC (xác thực, tài khoản, xem sách, giỏ hàng, đơn hàng)
- 👨‍💼 **ADMIN**: 24 UC (17 USER + 7 Admin-only)
- 👑 **MASTER**: 30 UC (24 ADMIN + 6 Master-only)

---

## 📋 BẢNG PHÂN QUYỀN CHI TIẾT

| Use Case | USER | ADMIN | MASTER |
|----------|------|-------|--------|
| **Đăng ký** | ✅ | ✅ | ✅ |
| **Đăng nhập** | ✅ | ✅ | ✅ |
| **Quên MK** | ✅ | ✅ | ✅ |
| **Đặt lại MK** | ✅ | ✅ | ✅ |
| **Xem hồ sơ** | ✅ | ✅ | ✅ |
| **Cập nhật hồ sơ** | ✅ | ✅ | ✅ |
| **Đổi MK** | ✅ | ✅ | ✅ |
| **Xem danh sách sách** | ✅ | ✅ | ✅ |
| **Xem chi tiết sách** | ✅ | ✅ | ✅ |
| **Thêm sách** | ❌ | ✅ | ✅ |
| **Sửa sách** | ❌ | ✅ | ✅ |
| **Xóa sách** | ❌ | ✅ | ✅ |
| **Xem danh mục** | ✅ | ✅ | ✅ |
| **Thêm danh mục** | ❌ | ✅ | ✅ |
| **Sửa danh mục** | ❌ | ✅ | ✅ |
| **Xóa danh mục** | ❌ | ✅ | ✅ |
| **Xem giỏ hàng** | ✅ | ✅ | ✅ |
| **Thêm vào giỏ** | ✅ | ✅ | ✅ |
| **Cập nhật giỏ** | ✅ | ✅ | ✅ |
| **Xóa khỏi giỏ** | ✅ | ✅ | ✅ |
| **Thanh toán** | ✅ | ✅ | ✅ |
| **Xem lịch sử đơn** | ✅ | ✅ | ✅ |
| **Xem chi tiết đơn** | ✅ | ✅ | ✅ |
| **Quản lý đơn** | ❌ | ✅ | ✅ |
| **Cập nhật đơn** | ❌ | ✅ | ✅ |
| **Thống kê theo ngày** | ❌ | ✅ | ✅ |
| **Thống kê theo tháng** | ❌ | ✅ | ✅ |
| **Thống kê khách hàng** | ❌ | ✅ | ✅ |
| **Thống kê sản phẩm** | ❌ | ✅ | ✅ |
| **Xem danh sách user** | ❌ | ❌ | ✅ |
| **Xem chi tiết user** | ❌ | ❌ | ✅ |
| **Tạo user** | ❌ | ❌ | ✅ |
| **Sửa user** | ❌ | ❌ | ✅ |
| **Xóa user** | ❌ | ❌ | ✅ |

---

## 🔄 INHERITANCE DIAGRAM (Kế thừa quyền)

```
        ┌─────────────┐
        │  USER (17)  │
        │  - Xác thực  │
        │  - Tài khoản │
        │  - Xem/Mua   │
        └──────┬──────┘
               │
               ▼
        ┌──────────────────┐
        │  ADMIN (24)      │
        │  + Thêm/Sửa/Xóa │
        │  + Quản lý đơn   │
        │  + Thống kê      │
        └──────┬───────────┘
               │
               ▼
        ┌──────────────────┐
        │  MASTER (30)     │
        │  + Quản lý user  │
        │  + Tối high      │
        └──────────────────┘
```

---

## 💡 NHẬN XÉT

### **Tại sao 30 Use Cases là tối thiểu?**

1. **Mỗi chức năng = 1 Use Case** (theo nguyên tắc UML)
   - Không nên gộp chung (vd: "CRUD Sách" nên tách thành 3 UC: Thêm, Sửa, Xóa)

2. **Xác thực quyền riêng biệt**
   - Cùng chức năng nhưng quyền khác = Use Case khác (vd: Xem sách vs Xóa sách)

3. **Đầy đủ tất cả 32 chức năng** từ danh sách:
   - 4 Auth + 7 User + 5 Book + 4 Category + 4 Cart + 4 Order + 4 Stats = **32 chức năng**
   - Nhưng có **3 Use Cases chính** được lặp lại cho 3 actors (kế thừa):
     - USER: 17 UC
     - ADMIN: +7 UC (tổng 24)
     - MASTER: +6 UC (tổng 30)

---

## 📝 CÓ THỂ GIẢM XE CÒN BAO NHIÊU?

### **Nếu gộp chung chức năng tương tự:**
- Gộp "Xem/Tạo/Sửa/Xóa User" = 1 UC "Quản lý User"
- Gộp "Thống kê" = 1 UC "Xem Báo Cáo"
- Gộp "CRUD Book" = 1 UC "Quản lý Sách"

→ **Có thể giảm xuống ~20-22 UC** (nhưng **không đúng UML standard**)

### **Khuyến nghị:**
✅ **Giữ 30 UC** - Này đúng UML standard, chi tiết, dễ theo dõi, dễ bảo trì

---

## 📊 BIỂU ĐỒ MINH HỌA

```
   UC1-UC7: Xác thực & TK (7 UC - Tất cả)
   UC8-UC10: Xem Sách/DM (3 UC - Tất cả)
   UC11-UC15: Giỏ Hàng (5 UC - Tất cả)
   UC16-UC17: Đơn Hàng (2 UC - USER)
   UC18-UC20: CRUD Sách (3 UC - ADMIN+)
   UC21-UC23: CRUD DM (3 UC - ADMIN+)
   UC24-UC25: Quản lý Đơn (2 UC - ADMIN+)
   UC26-UC29: Thống kê (4 UC - ADMIN+)
   UC30-UC34: Quản lý User (5 UC - MASTER)
   ────────────────────────
   TỔNG: 30 USE CASES
```

---

## 🎯 CÔNG THỨC TÍNH

```
SỐ USE CASE TỐI THIỂU = 
  (Chức năng User) + 
  (Chức năng Admin Only) + 
  (Chức năng Master Only)
  
= 17 + 7 + 6 
= 30 USE CASES
```

---

**Kết luận:** Ứng dụng của bạn cần **tối thiểu 30 Use Cases** để đạt chuẩn UML và cover hết tất cả 32 chức năng từ 7 Controller! 🎉

