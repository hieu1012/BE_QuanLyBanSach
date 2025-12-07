# Postman Collection cho Upload Ảnh Sản Phẩm

## Hướng dẫn import collection vào Postman

1. Copy toàn bộ JSON dưới đây
2. Vào Postman → File → Import
3. Paste JSON vào
4. Click Import

---

## Collection JSON

```json
{
    "info": {
        "name": "Product Image Upload API",
        "description": "API collection để upload và quản lý ảnh sản phẩm",
        "version": "1.0.0"
    },
    "auth": {
        "type": "bearer",
        "bearer": [
            {
                "key": "token",
                "value": "{{token}}",
                "type": "string"
            }
        ]
    },
    "variable": [
        {
            "key": "baseUrl",
            "value": "http://localhost:8081",
            "type": "string"
        },
        {
            "key": "token",
            "value": "",
            "type": "string"
        },
        {
            "key": "productId",
            "value": "1",
            "type": "string"
        }
    ],
    "item": [
        {
            "name": "Auth",
            "item": [
                {
                    "name": "Login - Get Token",
                    "request": {
                        "method": "POST",
                        "header": [
                            {
                                "key": "Content-Type",
                                "value": "application/json"
                            }
                        ],
                        "body": {
                            "mode": "raw",
                            "raw": "{\n  \"username\": \"master\",\n  \"password\": \"1111\"\n}"
                        },
                        "url": {
                            "raw": "{{baseUrl}}/auth/login",
                            "host": ["{{baseUrl}}"],
                            "path": ["auth", "login"]
                        },
                        "description": "Login và lấy JWT token"
                    },
                    "response": []
                }
            ]
        },
        {
            "name": "Create Product with Images",
            "request": {
                "method": "POST",
                "header": [
                    {
                        "key": "Authorization",
                        "value": "Bearer {{token}}",
                        "type": "text"
                    }
                ],
                "body": {
                    "mode": "formdata",
                    "formdata": [
                        {
                            "key": "title",
                            "value": "Sách Lập Trình Python Cơ Bản",
                            "type": "text"
                        },
                        {
                            "key": "description",
                            "value": "Hướng dẫn lập trình Python từ cơ bản đến nâng cao",
                            "type": "text"
                        },
                        {
                            "key": "price",
                            "value": "150000",
                            "type": "text"
                        },
                        {
                            "key": "discountPrice",
                            "value": "120000",
                            "type": "text"
                        },
                        {
                            "key": "discount",
                            "value": "20",
                            "type": "text"
                        },
                        {
                            "key": "stock",
                            "value": "50",
                            "type": "text"
                        },
                        {
                            "key": "isActive",
                            "value": "true",
                            "type": "text"
                        },
                        {
                            "key": "categoryId",
                            "value": "1",
                            "type": "text"
                        },
                        {
                            "key": "images",
                            "type": "file",
                            "src": []
                        },
                        {
                            "key": "images",
                            "type": "file",
                            "src": []
                        }
                    ]
                },
                "url": {
                    "raw": "{{baseUrl}}/products",
                    "host": ["{{baseUrl}}"],
                    "path": ["products"]
                },
                "description": "Tạo sản phẩm mới kèm upload ảnh\n\nNhớ chọn 2+ file ảnh ở field 'images'"
            },
            "response": []
        },
        {
            "name": "Get Product by ID",
            "request": {
                "method": "GET",
                "header": [],
                "url": {
                    "raw": "{{baseUrl}}/products/{{productId}}",
                    "host": ["{{baseUrl}}"],
                    "path": ["products", "{{productId}}"]
                },
                "description": "Lấy thông tin sản phẩm (bao gồm danh sách ảnh)"
            },
            "response": []
        },
        {
            "name": "Update Product with New Images",
            "request": {
                "method": "PUT",
                "header": [
                    {
                        "key": "Authorization",
                        "value": "Bearer {{token}}",
                        "type": "text"
                    }
                ],
                "body": {
                    "mode": "formdata",
                    "formdata": [
                        {
                            "key": "title",
                            "value": "Sách Python Pro Edition",
                            "type": "text"
                        },
                        {
                            "key": "description",
                            "value": "Bản nâng cao với nội dung mới",
                            "type": "text"
                        },
                        {
                            "key": "price",
                            "value": "200000",
                            "type": "text"
                        },
                        {
                            "key": "images",
                            "type": "file",
                            "src": []
                        },
                        {
                            "key": "images",
                            "type": "file",
                            "src": []
                        },
                        {
                            "key": "keepExistingImages",
                            "value": "false",
                            "type": "text",
                            "description": "false = thay thế ảnh cũ, true = thêm ảnh mới"
                        }
                    ]
                },
                "url": {
                    "raw": "{{baseUrl}}/products/{{productId}}",
                    "host": ["{{baseUrl}}"],
                    "path": ["products", "{{productId}}"]
                },
                "description": "Cập nhật sản phẩm + upload ảnh mới\n\nkeepExistingImages:\n- false: xóa ảnh cũ, upload ảnh mới\n- true: giữ ảnh cũ, thêm ảnh mới"
            },
            "response": []
        },
        {
            "name": "Upload Images to Product",
            "request": {
                "method": "POST",
                "header": [
                    {
                        "key": "Authorization",
                        "value": "Bearer {{token}}",
                        "type": "text"
                    }
                ],
                "body": {
                    "mode": "formdata",
                    "formdata": [
                        {
                            "key": "images",
                            "type": "file",
                            "src": []
                        },
                        {
                            "key": "images",
                            "type": "file",
                            "src": []
                        },
                        {
                            "key": "replaceExisting",
                            "value": "false",
                            "type": "text",
                            "description": "false = thêm ảnh mới, true = thay thế ảnh cũ"
                        }
                    ]
                },
                "url": {
                    "raw": "{{baseUrl}}/products/{{productId}}/upload-images",
                    "host": ["{{baseUrl}}"],
                    "path": ["products", "{{productId}}", "upload-images"]
                },
                "description": "Upload ảnh cho sản phẩm đã tồn tại\n\nreplaceExisting:\n- false: giữ ảnh cũ, thêm ảnh mới\n- true: xóa ảnh cũ, upload ảnh mới"
            },
            "response": []
        },
        {
            "name": "Replace All Images (true)",
            "request": {
                "method": "POST",
                "header": [
                    {
                        "key": "Authorization",
                        "value": "Bearer {{token}}",
                        "type": "text"
                    }
                ],
                "body": {
                    "mode": "formdata",
                    "formdata": [
                        {
                            "key": "images",
                            "type": "file",
                            "src": []
                        },
                        {
                            "key": "replaceExisting",
                            "value": "true",
                            "type": "text"
                        }
                    ]
                },
                "url": {
                    "raw": "{{baseUrl}}/products/{{productId}}/upload-images?replaceExisting=true",
                    "host": ["{{baseUrl}}"],
                    "path": ["products", "{{productId}}", "upload-images"],
                    "query": [
                        {
                            "key": "replaceExisting",
                            "value": "true"
                        }
                    ]
                },
                "description": "Thay thế tất cả ảnh cũ bằng ảnh mới"
            },
            "response": []
        },
        {
            "name": "Get All Products",
            "request": {
                "method": "GET",
                "header": [],
                "url": {
                    "raw": "{{baseUrl}}/products",
                    "host": ["{{baseUrl}}"],
                    "path": ["products"]
                },
                "description": "Lấy danh sách tất cả sản phẩm"
            },
            "response": []
        },
        {
            "name": "Get Products Pagination",
            "request": {
                "method": "GET",
                "header": [],
                "url": {
                    "raw": "{{baseUrl}}/products/hasPage?page=0&size=10&categoryId=1",
                    "host": ["{{baseUrl}}"],
                    "path": ["products", "hasPage"],
                    "query": [
                        {
                            "key": "page",
                            "value": "0"
                        },
                        {
                            "key": "size",
                            "value": "10"
                        },
                        {
                            "key": "categoryId",
                            "value": "1",
                            "disabled": true
                        }
                    ]
                },
                "description": "Lấy danh sách sản phẩm có phân trang"
            },
            "response": []
        },
        {
            "name": "Delete Product",
            "request": {
                "method": "DELETE",
                "header": [
                    {
                        "key": "Authorization",
                        "value": "Bearer {{token}}",
                        "type": "text"
                    }
                ],
                "url": {
                    "raw": "{{baseUrl}}/products/{{productId}}",
                    "host": ["{{baseUrl}}"],
                    "path": ["products", "{{productId}}"]
                },
                "description": "Xóa sản phẩm (cũng xóa ảnh)"
            },
            "response": []
        }
    ]
}
```

---

## Hướng dẫn sử dụng trong Postman

### 1. Setup biến

-   Vào `Collections` → chọn collection → `Variables` tab
-   Set giá trị cho các biến:
    -   `baseUrl`: `http://localhost:8081`
    -   `token`: (lấy từ login response)
    -   `productId`: ID sản phẩm bạn muốn test

### 2. Login trước tiên

-   Chạy request "Login - Get Token"
-   Copy token từ response
-   Paste vào biến `{{token}}`

### 3. Tạo sản phẩm với ảnh

-   Click "Create Product with Images"
-   Vào tab Body → chọn 2 file ảnh ở field images
-   Send

### 4. Upload ảnh thêm/thay thế

-   Chọn request "Upload Images to Product"
-   Chọn ảnh mới
-   Set `replaceExisting` (true/false)
-   Send

### 5. Cập nhật sản phẩm + ảnh

-   Click "Update Product with New Images"
-   Thay đổi dữ liệu nếu cần
-   Chọn ảnh mới
-   Set `keepExistingImages` (true/false)
-   Send

---

## Pre-request Script (tuỳ chọn)

Để tự động login, thêm script này vào collection:

```javascript
// Chỉ chạy nếu token chưa có
if (!pm.variables.get("token")) {
    pm.sendRequest(
        {
            url: pm.variables.get("baseUrl") + "/auth/login",
            method: "POST",
            header: {
                "Content-Type": "application/json",
            },
            body: {
                mode: "raw",
                raw: JSON.stringify({
                    username: "master",
                    password: "1111",
                }),
            },
        },
        function (err, response) {
            if (!err) {
                let data = response.json();
                pm.variables.set("token", data.data.token);
            }
        }
    );
}
```

Thiết lập:

-   Collections → Settings → Pre-request Script (paste script trên)

---

Đã sẵn sàng test! 🎉
