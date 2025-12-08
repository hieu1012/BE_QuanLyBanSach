package iuh.fit.controllers;

import iuh.fit.dtos.admin.AdminCreateUserRequest;
import iuh.fit.dtos.admin.AdminUpdateUserRequest;
import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.dtos.order.OrderSummaryDTO;
import iuh.fit.entities.User;
import iuh.fit.entities.enums.OrderStatus;
import iuh.fit.exceptions.ItemNotFoundException;
import iuh.fit.exceptions.UnauthorizedException;
import iuh.fit.repositories.UserRepository;
import iuh.fit.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MASTER')") // Đảm bảo annotation này đúng
public class AdminOrderController {

    private final OrderService orderService;
    private final UserRepository userRepository; // [MỚI] Cần để lấy User hiện tại

    // [MỚI] Helper function để lấy User từ Token
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Vui lòng đăng nhập");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User không tồn tại"));
    }

    //Cập nhật đơn (Admin) - PUT
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrderAdmin(
            @PathVariable Integer id,
            @RequestBody OrderDTO orderDTO) {
        // endpoint này không dùng currentUser trong logic service cũ,
        // nhưng @PreAuthorize sẽ chặn nếu role sai.
        return ResponseEntity.ok(orderService.updateOrderAdmin(id, orderDTO));
    }

    // Cập nhật trạng thái đơn hàng - PUT
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Integer id, @RequestParam OrderStatus newStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, newStatus));
    }

    // Xóa đơn hàng - DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Integer id) {
        OrderDTO cancelledOrder = orderService.deleteOrder(id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Đã hủy đơn hàng thành công (Admin action)");
        response.put("data", cancelledOrder);
        return ResponseEntity.ok(response);
    }

    // GET /admin/orders
    @GetMapping
    public ResponseEntity<Page<OrderSummaryDTO>> getOrdersByFilter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @ParameterObject Pageable pageable) {

        // [SỬA LỖI] Lấy user hiện tại thay vì truyền null
        User currentUser = getCurrentUser();

        Page<OrderSummaryDTO> orders = orderService.getOrdersByFilter(
                currentUser, keyword, status, startDate, endDate, pageable);

        return ResponseEntity.ok(orders);
    }
}