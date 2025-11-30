package iuh.fit.controllers;

import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.dtos.order.OrderSummaryDTO;
import iuh.fit.entities.enums.OrderStatus;
import iuh.fit.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MASTER')") // Chỉ Admin/Master được truy cập
public class AdminOrderController {

    private final OrderService orderService;

    //Cập nhật đơn (Admin)
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrderAdmin(
            @PathVariable Integer id,
            @RequestBody OrderDTO orderDTO) {

        return ResponseEntity.ok(orderService.updateOrderAdmin(id, orderDTO));
    }

    // Cập nhật trạng thái đơn hàng
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Integer id, @RequestParam OrderStatus newStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, newStatus));
    }

    // Xóa đơn hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<OrderSummaryDTO>> getOrdersByFilter(
            // Tham số tìm kiếm
            @RequestParam(required = false) String keyword,
            // Tham số lọc trạng thái
            @RequestParam(required = false) OrderStatus status,
            // Tham số lọc thời gian
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            // Tham số phân trang
            @ParameterObject Pageable pageable) {

        // ADMIN/MASTER có thể xem tất cả đơn hàng (pass null cho currentUser để Service biết)
        Page<OrderSummaryDTO> orders = orderService.getOrdersByFilter(
                null, keyword, status, startDate, endDate, pageable);

        return ResponseEntity.ok(orders);
    }
}