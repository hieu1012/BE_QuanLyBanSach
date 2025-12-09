# 📋 HƯỚNG DẪN HOÀN THIỆN CÁC CHỨC NĂNG CỦA APP
## Hệ Thống Quản Lý Bán Sách - Spring Boot REST API

---

## 📊 TỔNG HỢP TÌNH TRẠNG HIỆN TẠI

### Các Controller Hiện Tại:
✅ **AuthController** - Đăng nhập, đăng ký (90% hoàn thành)
✅ **UserController** - Quản lý hồ sơ người dùng
✅ **ProductController** - Quản lý sản phẩm
✅ **CategoryController** - Quản lý danh mục
✅ **CartController** - Quản lý giỏ hàng
✅ **OrderController** - Quản lý đơn hàng (User)
✅ **AdminOrderController** - Quản lý đơn hàng (Admin)
✅ **AdminController** - Quản lý người dùng (Admin)
✅ **StatisticsController** - Thống kê (Admin/Master)

---

## 🎯 DANH SÁCH CHỨC NĂNG CẦN HOÀN THIỆN

### **PHẦN 1: XÁC THỰC & TÀI KHOẢN (Authentication)**

#### 1.1 AuthController - Cần Hoàn Thiện:
```
🔲 POST /auth/login                    ✅ DONE
🔲 POST /auth/register                 ✅ DONE
🔲 POST /auth/refresh-token            ⚠️ CẦN CODE
🔲 POST /auth/logout                   ⚠️ CẦN CODE
🔲 POST /auth/forgot-password          ✅ DONE
🔲 POST /auth/reset-password           ✅ DONE
🔲 GET  /auth/verify-token             ⚠️ CẦN CODE
🔲 POST /auth/change-password          ⚠️ CẦN CODE
```

#### 1.2 UserController - Cần Hoàn Thiện:
```
🔲 GET  /users/profile                 ⚠️ CẦN CODE
🔲 PUT  /users/profile                 ⚠️ CẦN CODE
🔲 GET  /users/{id}                    ⚠️ CẦN CODE
🔲 DELETE /users/{id}                  ⚠️ CẦN CODE
🔲 PUT  /users/{id}/activate           ⚠️ CẦN CODE (Master only)
🔲 PUT  /users/{id}/deactivate         ⚠️ CẦN CODE (Master only)
```

---

### **PHẦN 2: SẢN PHẨM & DANH MỤC**

#### 2.1 ProductController:
```
🔲 GET  /products                      ✅ DONE (xem danh sách)
🔲 GET  /products/{id}                 ✅ DONE (xem chi tiết)
🔲 GET  /products/search               ✅ DONE (tìm kiếm)
🔲 POST /products                       ✅ DONE (Admin thêm)
🔲 PUT  /products/{id}                 ✅ DONE (Admin sửa)
🔲 DELETE /products/{id}               ⚠️ CẦN CODE (Admin xóa)
🔲 GET  /products/category/{id}        ✅ DONE (lọc danh mục)
```

#### 2.2 CategoryController:
```
🔲 GET  /categories                    ✅ DONE
🔲 GET  /categories/{id}               ⚠️ CẦN CODE
🔲 POST /categories                    ⚠️ CẦN CODE (Admin only)
🔲 PUT  /categories/{id}               ⚠️ CẦN CODE (Admin only)
🔲 DELETE /categories/{id}             ⚠️ CẦN CODE (Admin only)
```

---

### **PHẦN 3: GIỎ HÀNG**

#### 3.1 CartController:
```
🔲 GET  /cart                          ✅ DONE (xem giỏ)
🔲 POST /cart/add                      ✅ DONE (thêm vào giỏ)
🔲 PUT  /cart/items/{id}               ✅ DONE (cập nhật số lượng)
🔲 DELETE /cart/items/{id}             ✅ DONE (xóa sản phẩm)
🔲 DELETE /cart                        ⚠️ CẦN CODE (xóa toàn bộ giỏ)
🔲 POST /cart/checkout                 ✅ DONE (thanh toán)
```

---

### **PHẦN 4: ĐƠN HÀNG**

#### 4.1 OrderController (User):
```
🔲 POST /orders                        ✅ DONE (tạo đơn)
🔲 GET  /orders                        ✅ DONE (xem danh sách)
🔲 GET  /orders/{id}                   ✅ DONE (xem chi tiết)
🔲 POST /orders/{id}/cancel            ✅ DONE (hủy đơn)
🔲 GET  /orders/{id}/track             ⚠️ CẦN CODE (theo dõi trạng thái)
```

#### 4.2 AdminOrderController (Admin/Master):
```
🔲 GET  /admin/orders                  ✅ DONE (xem tất cả)
🔲 PUT  /admin/orders/{id}             ✅ DONE (cập nhật)
🔲 PUT  /admin/orders/{id}/status      ✅ DONE (đổi trạng thái)
🔲 DELETE /admin/orders/{id}           ✅ DONE (xóa/hủy)
🔲 GET  /admin/orders/{id}/details     ⚠️ CẦN CODE (chi tiết đơn)
🔲 POST /admin/orders/{id}/approve-cancel  ⚠️ CẦN CODE (duyệt hủy đơn)
🔲 POST /admin/orders/{id}/reject-cancel   ⚠️ CẦN CODE (từ chối hủy đơn)
```

---

### **PHẦN 5: QUẢN LÝ NGƯỜI DÙNG (Master Only)**

#### 5.1 AdminController (Master Functions):
```
🔲 GET  /admin/users                   ✅ DONE (xem danh sách)
🔲 GET  /admin/users/{id}              ✅ DONE (xem chi tiết)
🔲 POST /admin/users                   ✅ DONE (tạo user)
🔲 PUT  /admin/users/{id}              ✅ DONE (cập nhật)
🔲 DELETE /admin/users/{id}            ⚠️ CẦN CODE (xóa user)
🔲 PUT  /admin/users/{id}/role         ⚠️ CẦN CODE (gán quyền)
🔲 PUT  /admin/users/{id}/lock         ⚠️ CẦN CODE (khóa user)
🔲 PUT  /admin/users/{id}/unlock       ⚠️ CẦN CODE (mở khóa user)
```

---

### **PHẦN 6: THỐNG KÊ & BÁO CÁO (Admin/Master)**

#### 6.1 StatisticsController:
```
🔲 GET  /stats/day                     ✅ DONE (thống kê theo ngày)
🔲 GET  /stats/month                   ✅ DONE (thống kê theo tháng)
🔲 GET  /stats/customers               ✅ DONE (khách top)
🔲 GET  /stats/products                ✅ DONE (sản phẩm bán chạy)
🔲 GET  /stats/revenue                 ⚠️ CẦN CODE (doanh thu tổng)
🔲 GET  /stats/orders                  ⚠️ CẦN CODE (thống kê đơn hàng)
🔲 GET  /stats/report/pdf              ⚠️ CẦN CODE (xuất báo cáo PDF)
```

---

## 🛠️ HƯỚNG DẪN TRIỂN KHAI TỪNG PHẦN

### **STEP 1: Hoàn Thiện AuthController**

#### Task 1.1: Thêm Refresh Token
```java
@PostMapping("/refresh-token")
public ResponseEntity<Map<String, Object>> refreshToken(
        @RequestHeader("Authorization") String authorizationHeader) {
    try {
        // Kiểm tra header
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid token");
        }
        
        // Lấy token từ header
        String refreshToken = authorizationHeader.substring(7);
        
        // Verify token
        User user = jwtUtil.verifyRefreshToken(refreshToken);
        
        // Generate new access token
        String newAccessToken = jwtUtil.generateToken(user);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Token refreshed successfully");
        
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("accessToken", newAccessToken);
        data.put("tokenType", "Bearer");
        
        response.put("data", data);
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        throw new UnauthorizedException("Invalid refresh token");
    }
}
```

#### Task 1.2: Thêm Logout
```java
@PostMapping("/logout")
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MASTER')")
public ResponseEntity<Map<String, Object>> logout() {
    // Lưu token vào blacklist (cache/redis)
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    
    // TODO: Implement token blacklist logic
    
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("status", HttpStatus.OK.value());
    response.put("message", "Đăng xuất thành công");
    return ResponseEntity.ok(response);
}
```

#### Task 1.3: Thêm Change Password
```java
@PostMapping("/change-password")
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MASTER')")
public ResponseEntity<Map<String, Object>> changePassword(
        @Valid @RequestBody ChangePasswordRequest request) {
    try {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
        
        // Verify old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedException("Mật khẩu cũ không đúng");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Đổi mật khẩu thành công");
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("status", 400, "message", e.getMessage()));
    }
}
```

---

### **STEP 2: Hoàn Thiện UserController**

```java
package iuh.fit.controllers;

import iuh.fit.dtos.user.UserDTO;
import iuh.fit.entities.User;
import iuh.fit.exceptions.UnauthorizedException;
import iuh.fit.repositories.UserRepository;
import iuh.fit.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Vui lòng đăng nhập");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User không tồn tại"));
    }

    // GET /users/profile - Xem hồ sơ của user hiện tại
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MASTER')")
    public ResponseEntity<Map<String, Object>> getProfile() {
        User currentUser = getCurrentUser();
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lấy hồ sơ thành công");
        response.put("data", modelMapper.map(currentUser, UserDTO.class));
        return ResponseEntity.ok(response);
    }

    // PUT /users/profile - Cập nhật hồ sơ của user hiện tại
    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MASTER')")
    public ResponseEntity<Map<String, Object>> updateProfile(
            @Valid @RequestBody UserDTO userDTO) {
        User currentUser = getCurrentUser();
        
        // Cập nhật các trường cho phép
        currentUser.setFullName(userDTO.getFullName());
        currentUser.setEmail(userDTO.getEmail());
        currentUser.setPhone(userDTO.getPhone());
        currentUser.setAddress(userDTO.getAddress());
        
        User updatedUser = userRepository.save(currentUser);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Cập nhật hồ sơ thành công");
        response.put("data", modelMapper.map(updatedUser, UserDTO.class));
        return ResponseEntity.ok(response);
    }

    // GET /users/{id} - Xem chi tiết user (Admin/Master)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("User không tồn tại"));
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", modelMapper.map(user, UserDTO.class));
        return ResponseEntity.ok(response);
    }

    // PUT /users/{id}/role - Gán quyền cho user (Master only)
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<Map<String, Object>> assignRole(
            @PathVariable Long id,
            @RequestParam String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("User không tồn tại"));
        
        userService.updateUserRole(user, role);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Gán quyền thành công");
        response.put("data", modelMapper.map(user, UserDTO.class));
        return ResponseEntity.ok(response);
    }

    // PUT /users/{id}/lock - Khóa user (Master only)
    @PutMapping("/{id}/lock")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<Map<String, Object>> lockUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("User không tồn tại"));
        
        user.setIsActive(false);
        userRepository.save(user);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Khóa user thành công");
        return ResponseEntity.ok(response);
    }

    // PUT /users/{id}/unlock - Mở khóa user (Master only)
    @PutMapping("/{id}/unlock")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<Map<String, Object>> unlockUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("User không tồn tại"));
        
        user.setIsActive(true);
        userRepository.save(user);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Mở khóa user thành công");
        return ResponseEntity.ok(response);
    }

    // DELETE /users/{id} - Xóa user (Master only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("User không tồn tại"));
        
        userRepository.delete(user);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Xóa user thành công");
        return ResponseEntity.ok(response);
    }
}
```

---

### **STEP 3: Hoàn Thiện CategoryController**

```java
package iuh.fit.controllers;

import iuh.fit.dtos.CategoryDTO;
import iuh.fit.dtos.CreateCategoryDTO;
import iuh.fit.entities.Category;
import iuh.fit.exceptions.ItemNotFoundException;
import iuh.fit.repositories.CategoryRepository;
import iuh.fit.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    // GET /categories - Xem tất cả danh mục
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lấy danh sách danh mục thành công");
        response.put("data", categories.stream()
                .map(c -> modelMapper.map(c, CategoryDTO.class))
                .toList());
        return ResponseEntity.ok(response);
    }

    // GET /categories/{id} - Xem chi tiết danh mục
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCategoryById(@PathVariable Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Danh mục không tồn tại"));
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", modelMapper.map(category, CategoryDTO.class));
        return ResponseEntity.ok(response);
    }

    // POST /categories - Tạo danh mục mới (Admin only)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<Map<String, Object>> createCategory(
            @Valid @RequestBody CreateCategoryDTO createCategoryDTO) {
        Category newCategory = new Category();
        newCategory.setName(createCategoryDTO.getName());
        newCategory.setDescription(createCategoryDTO.getDescription());
        newCategory.setIsActive(true);
        
        Category savedCategory = categoryRepository.save(newCategory);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("message", "Tạo danh mục thành công");
        response.put("data", modelMapper.map(savedCategory, CategoryDTO.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT /categories/{id} - Cập nhật danh mục (Admin only)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<Map<String, Object>> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody CreateCategoryDTO updateCategoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Danh mục không tồn tại"));
        
        category.setName(updateCategoryDTO.getName());
        category.setDescription(updateCategoryDTO.getDescription());
        
        Category updatedCategory = categoryRepository.save(category);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Cập nhật danh mục thành công");
        response.put("data", modelMapper.map(updatedCategory, CategoryDTO.class));
        return ResponseEntity.ok(response);
    }

    // DELETE /categories/{id} - Xóa danh mục (Admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Danh mục không tồn tại"));
        
        categoryRepository.delete(category);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Xóa danh mục thành công");
        return ResponseEntity.ok(response);
    }
}
```

---

### **STEP 4: Hoàn Thiện ProductController - Thêm Xóa Sản Phẩm**

```java
// Thêm vào ProductController.java

// DELETE /products/{id} - Xóa sản phẩm (Admin only)
@DeleteMapping("/{id}")
@PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable int id) {
    productService.deleteById(id);
    
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("status", HttpStatus.OK.value());
    response.put("message", "Xóa sản phẩm thành công");
    return ResponseEntity.ok(response);
}
```

---

### **STEP 5: Hoàn Thiện CartController - Xóa Giỏ Hàng**

```java
// Thêm vào CartController.java

// DELETE /cart - Xóa toàn bộ giỏ hàng
@DeleteMapping
public ResponseEntity<Map<String, Object>> clearCart() {
    User currentUser = getCurrentUser();
    cartService.clearCart(currentUser.getId());
    
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("status", HttpStatus.OK.value());
    response.put("message", "Xóa giỏ hàng thành công");
    return ResponseEntity.ok(response);
}
```

---

### **STEP 6: Hoàn Thiện OrderController - Theo Dõi Đơn Hàng**

```java
// Thêm vào OrderController.java

// GET /orders/{id}/track - Theo dõi trạng thái đơn hàng
@GetMapping("/{id}/track")
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MASTER')")
public ResponseEntity<Map<String, Object>> trackOrder(@PathVariable Integer id) {
    User currentUser = getCurrentUser();
    OrderDTO order = orderService.findOrderByIdForUser(id, currentUser);
    
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("status", HttpStatus.OK.value());
    response.put("message", "Thông tin theo dõi đơn hàng");
    response.put("data", Map.of(
            "orderId", order.getId(),
            "status", order.getStatus(),
            "createdDate", order.getCreatedDate(),
            "totalAmount", order.getTotalAmount(),
            "estimatedDelivery", "5-7 ngày" // TODO: Tính toán từ DB
    ));
    return ResponseEntity.ok(response);
}
```

---

### **STEP 7: Hoàn Thiện AdminOrderController - Duyệt Hủy Đơn**

```java
// Thêm vào AdminOrderController.java

// POST /admin/orders/{id}/approve-cancel - Duyệt hủy đơn
@PostMapping("/{id}/approve-cancel")
@PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
public ResponseEntity<Map<String, Object>> approveCancelOrder(
        @PathVariable Integer id,
        @RequestParam(required = false) String reason) {
    OrderDTO approvedOrder = orderService.approveCancelOrder(id, reason);
    
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("status", HttpStatus.OK.value());
    response.put("message", "Duyệt hủy đơn hàng thành công");
    response.put("data", approvedOrder);
    return ResponseEntity.ok(response);
}

// POST /admin/orders/{id}/reject-cancel - Từ chối hủy đơn
@PostMapping("/{id}/reject-cancel")
@PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
public ResponseEntity<Map<String, Object>> rejectCancelOrder(
        @PathVariable Integer id,
        @RequestParam String reason) {
    OrderDTO rejectedOrder = orderService.rejectCancelOrder(id, reason);
    
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("status", HttpStatus.OK.value());
    response.put("message", "Từ chối hủy đơn hàng thành công");
    response.put("data", rejectedOrder);
    return ResponseEntity.ok(response);
}

// GET /admin/orders/{id}/details - Chi tiết đơn hàng
@GetMapping("/{id}/details")
@PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
public ResponseEntity<Map<String, Object>> getOrderDetails(@PathVariable Integer id) {
    OrderDTO orderDetails = orderService.getOrderDetails(id);
    
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("status", HttpStatus.OK.value());
    response.put("message", "Lấy chi tiết đơn hàng thành công");
    response.put("data", orderDetails);
    return ResponseEntity.ok(response);
}
```

---

### **STEP 8: Hoàn Thiện StatisticsController - Thêm Thống Kê**

```java
// Thêm vào StatisticsController.java

// GET /stats/revenue - Doanh thu tổng
@GetMapping("/revenue")
public ResponseEntity<Map<String, Object>> getTotalRevenue(
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate) {
    
    Double totalRevenue = statisticsService.calculateTotalRevenue(startDate, endDate);
    
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("status", HttpStatus.OK.value());
    response.put("message", "Tính doanh thu thành công");
    response.put("data", Map.of(
            "totalRevenue", totalRevenue,
            "period", startDate + " to " + endDate
    ));
    return ResponseEntity.ok(response);
}

// GET /stats/orders - Thống kê đơn hàng
@GetMapping("/orders")
public ResponseEntity<Map<String, Object>> getOrderStatistics() {
    Map<String, Object> stats = statisticsService.getOrderStatistics();
    
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("status", HttpStatus.OK.value());
    response.put("message", "Lấy thống kê đơn hàng thành công");
    response.put("data", stats);
    return ResponseEntity.ok(response);
}

// GET /stats/report/pdf - Xuất báo cáo PDF (Master only)
@GetMapping("/report/pdf")
@PreAuthorize("hasRole('MASTER')")
public ResponseEntity<byte[]> generatePdfReport(
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate) {
    
    // TODO: Implement PDF generation using iText or similar library
    byte[] pdfContent = statisticsService.generatePdfReport(startDate, endDate);
    
    return ResponseEntity.ok()
            .header("Content-Type", "application/pdf")
            .header("Content-Disposition", "attachment; filename=report.pdf")
            .body(pdfContent);
}
```

---

## 📝 DANH SÁCH CÔNG VIỆC CẦN LÀM

### **Priority 1: CRITICAL (Làm Ngay)**
- [ ] Hoàn thiện UserController (GET profile, PUT profile)
- [ ] Hoàn thiện CategoryController (GET, POST, PUT, DELETE)
- [ ] Thêm xóa sản phẩm vào ProductController
- [ ] Thêm xóa giỏ hàng vào CartController

### **Priority 2: HIGH (Làm Tiếp)**
- [ ] Hoàn thiện AuthController (refresh-token, logout, change-password)
- [ ] Thêm quản lý người dùng (Admin) - lock/unlock/assign role
- [ ] Hoàn thiện OrderController (track order)
- [ ] Hoàn thiện AdminOrderController (approve/reject cancel)

### **Priority 3: MEDIUM (Làm Sau)**
- [ ] Thêm thống kê doanh thu, đơn hàng
- [ ] Xuất báo cáo PDF
- [ ] Tối ưu hóa hiệu năng query

### **Priority 4: LOW (Tùy Chọn)**
- [ ] Thêm email notification
- [ ] Thêm SMS notification
- [ ] Thêm webhook
- [ ] Thêm analytics

---

## 🔗 SWAGGER DOCUMENTATION

Sau khi hoàn thiện, tất cả API sẽ được tự động document:
```
GET http://localhost:8080/swagger-ui.html
```

---

## 📱 CẤU TRÚC REQUEST/RESPONSE CHUẨN

### Success Response:
```json
{
  "status": 200,
  "message": "Thành công",
  "data": { ... }
}
```

### Error Response:
```json
{
  "status": 400,
  "message": "Lỗi",
  "error": "BAD_REQUEST"
}
```

---

## 🚀 LƯỚI THỰC HIỆN

Bạn nên thực hiện theo thứ tự sau:
1. Hoàn thiện Xác thực (AuthController + UserController)
2. Hoàn thiện Sản phẩm (ProductController + CategoryController)
3. Hoàn thiện Đơn hàng (OrderController + AdminOrderController)
4. Hoàn thiện Thống kê (StatisticsController)
5. Tester & Deploy

---

## 💡 MẢO LẠ

- Luôn sử dụng `@PreAuthorize` để kiểm tra quyền
- Sử dụng `ModelMapper` để map Entity ↔ DTO
- Trả về response đúng format (status, message, data)
- Log tất cả các hành động quan trọng
- Handle exception hợp lý

---

## 📧 LIÊN HỆ HỖ TRỢ

Nếu bạn gặp vấn đề, vui lòng:
1. Kiểm tra các lỗi trong console
2. Xem tệp log (logs/)
3. Kiểm tra database connection
4. Kiểm tra JWT token validity

---

**Happy Coding! 🎉**

