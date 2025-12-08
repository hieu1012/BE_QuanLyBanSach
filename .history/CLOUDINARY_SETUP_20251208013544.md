# 🚀 Hướng Dẫn Cấu Hình Cloudinary

## Step 1: Tạo Tài Khoản Cloudinary

1. Truy cập https://cloudinary.com/users/register/free
2. Đăng ký tài khoản miễn phí (25GB/tháng)
3. Xác thực email
4. Sau khi login, bạn sẽ thấy **Dashboard**

## Step 2: Lấy Credentials

### Lấy Cloud Name, API Key, API Secret:

**Cách 1: Từ Dashboard**

```
Dashboard → Account → API Keys
```

Bạn sẽ thấy:

```
Cloud Name: [YOUR_CLOUD_NAME]
API Key: [YOUR_API_KEY]
API Secret: [YOUR_API_SECRET]
```

**Cách 2: Settings → Upload** (optional)

-   Xem lịch sử upload
-   Cấu hình folder mặc định (bạn có thể để mặc định là `quanlybansach/products`)

## Step 3: Cập Nhật application.properties

Mở file: `src/main/resources/application.properties`

Thay thế các giá trị:

```properties
# Cloudinary Configuration
cloudinary.cloud-name=YOUR_CLOUD_NAME
cloudinary.api-key=YOUR_API_KEY
cloudinary.api-secret=YOUR_API_SECRET
cloudinary.upload-folder=quanlybansach/products
```

**Ví dụ:**

```properties
cloudinary.cloud-name=dxyz1234abc
cloudinary.api-key=123456789abc
cloudinary.api-secret=abcXYZ123456_secret
cloudinary.upload-folder=quanlybansach/products
```

## Step 4: Build & Run

```bash
# Compile
mvn clean install

# Run
mvn spring-boot:run
```

## Step 5: Test Upload ảnh

### Cách 1: Postman

```
POST: http://localhost:8081/api/products/{id}/upload
Authorization: Bearer [YOUR_JWT_TOKEN]

Form-Data:
- images: [Chọn 1 hoặc nhiều file ảnh]
- replaceExisting: true/false (mặc định false)
```

**Response:**

```json
{
  "id": 1,
  "title": "Sách Java",
  "imageNames": [
    "quanlybansach/products/1701234567890_a1b2c3d4.jpg",
    "quanlybansach/products/1701234567891_e5f6g7h8.png"
  ],
  ...
}
```

### Cách 2: Frontend (JavaScript)

```javascript
const formData = new FormData();
formData.append("images", file1);
formData.append("images", file2);
formData.append("replaceExisting", false);

const response = await fetch(`/api/products/1/upload`, {
    method: "POST",
    headers: {
        Authorization: `Bearer ${token}`,
    },
    body: formData,
});

const product = await response.json();
console.log(product.imageNames); // Mảng public_id từ Cloudinary
```

## Step 6: Hiển Thị Ảnh trong Frontend

### Lấy URL ảnh từ public_id:

**JavaScript:**

```javascript
const publicId = "quanlybansach/products/1701234567890_a1b2c3d4";

// URL cơ bản
const imageUrl = `https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/${publicId}`;

// URL với transformation (auto quality, format)
const imageUrl = `https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/q_auto,f_auto/${publicId}`;

// URL responsive (chiều rộng 300px)
const imageUrl = `https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/w_300,q_auto,f_auto/${publicId}`;
```

**HTML:**

```html
<img
    src="https://res.cloudinary.com/YOUR_CLOUD_NAME/image/upload/q_auto,f_auto/quanlybansach/products/1701234567890_a1b2c3d4"
    alt="Product"
/>
```

## Tính Năng Cloudinary

✅ **Auto Optimization**: Tự nén ảnh dựa trên device
✅ **Format Auto**: Dùng WebP nếu browser hỗ trợ
✅ **Responsive**: Resize ảnh động theo viewport
✅ **CDN Global**: Phục vụ từ server gần nhất
✅ **Unlimited Transformations**: Crop, rotate, filter, etc.

## Troubleshooting

### Lỗi: Invalid cloud name

```
❌ Error: Invalid cloud name
✅ Kiểm tra: cloudinary.cloud-name đúng không?
```

### Lỗi: Unauthorized

```
❌ Error: Unauthorized
✅ Kiểm tra: API Key & Secret có đúng không?
```

### Lỗi: Upload quá lớn

```
❌ Error: File size exceeds limit
✅ Mặc định: 10MB, tăng trong application.properties
app.upload.max-file-size=20971520  // 20MB
```

## Xóa File (Optional)

Nếu muốn xóa toàn bộ folder `quanlybansach/products`:

1. Vào Dashboard → Media Library
2. Chọn folder → Delete
3. Hoặc dùng API: `DELETE /api/products/{id}/images`

---

**Notes:**

-   Cloudinary có **free tier 25GB/tháng** - đủ cho hầu hết app
-   Không cần server storage cho ảnh nữa ✅
-   Tự động backup & redundancy ✅
-   Hỗ trợ 200+ định dạng file ✅
