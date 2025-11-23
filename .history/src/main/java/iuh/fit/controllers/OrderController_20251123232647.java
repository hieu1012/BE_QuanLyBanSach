package iuh.fit.controllers;

import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.dtos.order.OrderSummaryDTO;
import iuh.fit.entities.enums.OrderStatus;
import iuh.fit.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * POST /api/orders
     * Tạo đơn hàng mới - yêu cầu USER hoặc cao hơn
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN', 'USER')")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

    /**
     * GET /api/orders/{id}
     * Lấy đơn hàng theo ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN', 'USER')")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.findOrderById(id));
    }

    /**
     * GET /api/orders/user/{userId}
     * Lấy danh sách đơn hàng theo người dùng
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN', 'USER')")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(orderService.findOrdersByUserId(userId));
    }

    /**
     * GET /api/orders
     * Lấy danh sách đơn hàng có phân trang - yêu cầu MASTER hoặc ADMIN
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Page<OrderSummaryDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.findAllOrders(PageRequest.of(page, size)));
    }

    /**
     * GET /api/orders/status/{status}
     * Lọc theo trạng thái đơn hàng - yêu cầu MASTER hoặc ADMIN
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.findOrdersByStatus(status));
    }

    /**
     * GET /api/orders/search
     * Tìm kiếm đơn hàng - yêu cầu MASTER hoặc ADMIN
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> searchOrders(@RequestParam String keyword) {
        return ResponseEntity.ok(orderService.searchOrders(keyword));
    }

    /**
     * PUT /api/orders/{id}/status
     * Cập nhật trạng thái đơn hàng - yêu cầu MASTER hoặc ADMIN
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Integer id, @RequestParam OrderStatus newStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, newStatus));
    }

    /**
     * DELETE /api/orders/{id}
     * Xóa đơn hàng - yêu cầu MASTER hoặc ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/orders/between
     * Lọc đơn hàng theo khoảng thời gian - yêu cầu MASTER hoặc ADMIN
     */
    @GetMapping("/between")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<List<OrderDTO>> getOrdersBetweenDates(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(orderService.findOrdersBetweenDates(start, end));
    }
}
