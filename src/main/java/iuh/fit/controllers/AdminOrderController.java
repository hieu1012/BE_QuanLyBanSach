package iuh.fit.controllers;

import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.dtos.order.OrderSummaryDTO;
import iuh.fit.entities.enums.OrderStatus;
import iuh.fit.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor

public class AdminOrderController {

    private final OrderService orderService;

    //Cập nhật đơn (Admin)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<OrderDTO> updateOrderAdmin(
            @PathVariable Integer id,
            @RequestBody OrderDTO orderDTO) {

        return ResponseEntity.ok(orderService.updateOrderAdmin(id, orderDTO));
    }

    // Cập nhật trạng thái đơn hàng
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Integer id, @RequestParam OrderStatus newStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, newStatus));
    }

    // Xóa đơn hàng
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Integer id) {
        // Gọi service đã sửa logic thành soft-delete (set status CANCELLED)
        OrderDTO cancelledOrder = orderService.deleteOrder(id);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Đã hủy đơn hàng thành công (Admin action)");
        response.put("data", cancelledOrder);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public ResponseEntity<Page<OrderSummaryDTO>> getOrdersByFilter(
            //Tìm kiếm
            @RequestParam(required = false) String keyword,
            //Lọc trạng thái
            @RequestParam(required = false) OrderStatus status,
            //Lọc thời gian
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            //Phân trang
            @ParameterObject Pageable pageable) {

        // ADMIN/MASTER có thể xem tất cả đơn hàng 
        Page<OrderSummaryDTO> orders = orderService.getOrdersByFilter(
                null, keyword, status, startDate, endDate, pageable);

        return ResponseEntity.ok(orders);
    }
}
