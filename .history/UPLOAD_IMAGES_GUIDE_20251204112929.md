# Hướng dẫn Upload Nhiều Ảnh Sản Phẩm

## 1. Cấu trúc thay đổi

### Các file cập nhật:

-   `Product.java`: Thay đổi từ `imageName` (String) → `imageNames` (JSON String array)
-   `ProductDTO.java`: Thay đổi từ `imageName` → `imageNames` (List<String>)
-   `CreateProductDTO.java`: Thay đổi từ `imageName` → `imageNames` (List<String>)
-   `ProductController.java`: Thêm endpoints mới để upload ảnh
-   `ProductServiceImpl.java`: Thêm methods xử lý upload ảnh
-   `FileUploadUtil.java`: Utility class mới để xử lý file upload/delete

### Config mới:

-   `application.properties`: Thêm cấu hình upload (thư mục, kích thước, định dạng)
-   `AppConfig.java`: Thêm ObjectMapper Bean
-   `SecurityConfig.java`: Cho phép access `/uploads/**`
-   `WebMvcConfig.java`: Mapping static resource cho uploads folder

---

## 2. API Endpoints

### A. Tạo sản phẩm mới với upload ảnh

```http
POST /products
Content-Type: multipart/form-data

Parameters:
- title (String, required): Tên sản phẩm
- description (String, required): Mô tả
- price (Double, required): Giá
- discountPrice (Double, optional): Giá giảm
- discount (Integer, optional): % giảm giá
- stock (Integer, required): Số lượng tồn
- isActive (Boolean, optional, default: true): Kích hoạt
- categoryId (Integer, required): ID danh mục
- images (MultipartFile[], optional): Mảng ảnh (tối đa 10MB mỗi file)

Example:
curl -X POST http://localhost:8081/products \
  -H "Authorization: Bearer <token>" \
  -F "title=Sách Python Cơ Bản" \
  -F "description=Hướng dẫn lập trình Python" \
  -F "price=150000" \
  -F "stock=50" \
  -F "categoryId=1" \
  -F "images=@book1.jpg" \
  -F "images=@book2.jpg"
```

### B. Cập nhật sản phẩm với ảnh mới

```http
PUT /products/{id}
Content-Type: multipart/form-data

Parameters:
- title (String, optional): Tên sản phẩm
- description (String, optional): Mô tả
- price (Double, optional): Giá
- discountPrice (Double, optional): Giá giảm
- discount (Integer, optional): % giảm giá
- stock (Integer, optional): Số lượng tồn
- isActive (Boolean, optional): Kích hoạt
- categoryId (Integer, optional): ID danh mục
- images (MultipartFile[], optional): Mảng ảnh mới
- keepExistingImages (Boolean, optional, default: false): Giữ ảnh cũ hay thay thế

Các option:
- keepExistingImages=true: Thêm ảnh mới vào ảnh cũ (cộng dồn)
- keepExistingImages=false: Xóa ảnh cũ, dùng ảnh mới

Example - Thay thế ảnh:
curl -X PUT http://localhost:8081/products/1 \
  -H "Authorization: Bearer <token>" \
  -F "title=Python Pro Edition" \
  -F "images=@new_book1.jpg" \
  -F "images=@new_book2.jpg" \
  -F "keepExistingImages=false"

Example - Thêm ảnh:
curl -X PUT http://localhost:8081/products/1 \
  -H "Authorization: Bearer <token>" \
  -F "images=@extra_book.jpg" \
  -F "keepExistingImages=true"
```

### C. Upload ảnh cho sản phẩm đã tồn tại

```http
POST /products/{id}/upload-images
Content-Type: multipart/form-data

Parameters:
- images (MultipartFile[], required): Mảng ảnh
- replaceExisting (Boolean, optional, default: false): Thay thế hay thêm

Các option:
- replaceExisting=true: Xóa ảnh cũ, upload ảnh mới
- replaceExisting=false: Giữ ảnh cũ, thêm ảnh mới

Example - Thay thế:
curl -X POST http://localhost:8081/products/1/upload-images \
  -H "Authorization: Bearer <token>" \
  -F "images=@photo1.jpg" \
  -F "images=@photo2.jpg" \
  -F "images=@photo3.jpg" \
  -F "replaceExisting=true"

Example - Thêm ảnh:
curl -X POST http://localhost:8081/products/1/upload-images \
  -H "Authorization: Bearer <token>" \
  -F "images=@extra.jpg" \
  -F "replaceExisting=false"
```

### D. Lấy thông tin sản phẩm (bao gồm danh sách ảnh)

```http
GET /products/{id}

Response:
{
  "status": 200,
  "data": {
    "id": 1,
    "title": "Sách Python",
    "description": "...",
    "price": 150000,
    "discountPrice": null,
    "discount": 0,
    "stock": 50,
    "imageNames": [
      "1701234567890_abc12345.jpg",
      "1701234567891_def67890.jpg"
    ],
    "isActive": true,
    "categoryId": 1,
    "categoryName": "Công nghệ"
  }
}
```

---

## 3. Cấu hình

### File: `application.properties`

```properties
# File Upload Configuration
app.upload.dir=uploads/products/
app.upload.max-file-size=10485760  # 10MB in bytes
app.upload.allowed-extensions=jpg,jpeg,png,gif,webp
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=50MB
```

### Thư mục upload

-   Tạo tự động nếu chưa tồn tại
-   Vị trí: `uploads/products/` (tương đối với project root)
-   Truy cập ảnh qua URL: `http://localhost:8081/uploads/products/<filename>`

---

## 4. Cách sử dụng từ Frontend

### JavaScript / Fetch API

```javascript
// Tạo sản phẩm với nhiều ảnh
const formData = new FormData();
formData.append("title", "Sách Lập Trình");
formData.append("description", "Hướng dẫn JavaScript");
formData.append("price", 200000);
formData.append("stock", 30);
formData.append("categoryId", 1);

// Thêm nhiều ảnh
document.getElementById("imageInput").files.forEach((file) => {
    formData.append("images", file);
});

const response = await fetch("/products", {
    method: "POST",
    headers: {
        Authorization: `Bearer ${token}`,
    },
    body: formData,
});

const result = await response.json();
console.log(result.data.imageNames); // ["file1.jpg", "file2.jpg"]
```

### Hiển thị ảnh

```javascript
// Từ API response
const product = response.data;
product.imageNames.forEach((imageName) => {
    const img = document.createElement("img");
    img.src = `/uploads/products/${imageName}`;
    document.body.appendChild(img);
});
```

---

## 5. Ưu điểm của phương pháp này

✅ **Upload nhiều ảnh cùng lúc** - Tiết kiệm time, user-friendly  
✅ **Tự động tạo thư mục** - Không cần config phức tạp  
✅ **Tên file duy nhất** - Tránh trùng lặp (timestamp + UUID)  
✅ **Validate file** - Kiểm tra kích thước, định dạng  
✅ **Linh hoạt** - Có thể thêm hoặc thay thế ảnh  
✅ **An toàn** - Authorize bằng JWT token

---

## 6. Lưu ý quan trọng

⚠️ **Database migration**: Cần chạy SQL để rename column

```sql
-- Chỉ cần nếu table đã tồn tại
ALTER TABLE products CHANGE COLUMN image_name image_names TEXT;
```

⚠️ **Định dạng ảnh**: Chỉ cho phép `jpg, jpeg, png, gif, webp`

⚠️ **Kích thước file**: Tối đa 10MB/file, 50MB/request

⚠️ **Dung lượng server**: Cần có đủ dung lượng để lưu ảnh

⚠️ **Permissions**: Thư mục `uploads/` cần write permission

---

## 7. Troubleshooting

### ❌ "Lỗi 403 - Forbidden"

-   Kiểm tra JWT token
-   Phải login với role MASTER hoặc ADMIN

### ❌ "Lỗi 413 - Payload Too Large"

-   Ảnh quá lớn (> 10MB)
-   Nén ảnh trước khi upload

### ❌ "Lỗi 400 - Bad Request - File type not allowed"

-   Định dạng file không hỗ trợ
-   Chỉ dùng: jpg, jpeg, png, gif, webp

### ❌ "Lỗi 500 - Thư mục uploads không tồn tại"

-   Tự động tạo lần đầu khi upload
-   Hoặc tạo thủ công: `mkdir -p uploads/products/`

### ❌ "Không thấy ảnh"

-   Kiểm tra đường dẫn: `/uploads/products/<filename>`
-   Đảm bảo file tồn tại trong thư mục
-   Check SecurityConfig có cho phép `/uploads/**` không

---

## 8. Ví dụ Postman

```
1. POST /products
   - Headers: Authorization: Bearer <token>
   - Body: form-data
     - title: "Sách học Java"
     - description: "Tự học Java từ cơ bản"
     - price: 300000
     - stock: 20
     - categoryId: 2
     - images: [chọn 2-3 file ảnh]

2. PUT /products/1
   - Headers: Authorization: Bearer <token>
   - Body: form-data
     - images: [chọn ảnh mới]
     - keepExistingImages: true

3. POST /products/1/upload-images
   - Headers: Authorization: Bearer <token>
   - Body: form-data
     - images: [chọn ảnh]
     - replaceExisting: false
```

---

Hết! Bạn có thể bắt đầu upload ảnh rồi! 🚀
