package iuh.fit.controllers;

import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.dtos.order.OrderSummaryDTO;
import iuh.fit.entities.User;
import iuh.fit.entities.enums.OrderStatus;
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
@PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
public class AdminOrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Vui lòng đăng nhập");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User không tồn tại"));
    }

    // Cập nhật đơn hàng (Admin)
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateOrderAdmin(
            @PathVariable Integer id,
            @RequestBody OrderDTO orderDTO) {

        OrderDTO updatedOrder = orderService.updateOrderAdmin(id, orderDTO);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Cập nhật thông tin đơn hàng thành công");
        response.put("data", updatedOrder);
        return ResponseEntity.ok(response);
    }

    // Cập nhật trạng thái đơn hàng
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Integer id,
            @RequestParam OrderStatus newStatus) {

        OrderDTO updatedOrder = orderService.updateOrderStatus(id, newStatus);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Cập nhật trạng thái đơn hàng thành công: " + newStatus);
        response.put("data", updatedOrder);
        return ResponseEntity.ok(response);
    }

    // Xóa đơn hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Integer id) {
        OrderDTO deletedOrder = orderService.deleteOrder(id);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Đã xóa vĩnh viễn đơn hàng và hoàn lại tồn kho thành công");
        response.put("data", deletedOrder);
        return ResponseEntity.ok(response);
    }

    // Lấy danh sách (Admin)
    @GetMapping
    public ResponseEntity<Map<String, Object>> getOrdersByFilter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @ParameterObject Pageable pageable) {

        User currentUser = getCurrentUser();
        Page<OrderSummaryDTO> orders = orderService.getOrdersByFilter(
                currentUser, keyword, status, startDate, endDate, pageable);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lấy danh sách đơn hàng (Admin) thành công");
        response.put("data", orders);
        return ResponseEntity.ok(response);
    }
}