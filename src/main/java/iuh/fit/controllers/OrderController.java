package iuh.fit.controllers;

import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.dtos.order.OrderSummaryDTO;
import iuh.fit.entities.enums.OrderStatus;
import iuh.fit.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //Tạo đơn hàng mới
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

    //Chi tiết đơn hàng
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.findOrderById(id));
    }

    //Lấy danh sách đơn hàng theo người dùng
    @GetMapping("/user/{userId}") // Endpoint cũ là /user/{userId}
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(orderService.findOrdersByUserId(userId));
    }

    //Lọc theo trạng thái đơn hàng
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.findOrdersByStatus(status));
    }

    //Tìm kiếm đơn hàng
    @GetMapping("/search")
    public ResponseEntity<List<OrderDTO>> searchOrders(@RequestParam String keyword) {
        return ResponseEntity.ok(orderService.searchOrders(keyword));
    }

    //Xóa đơn hàng
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    // Lọc đơn hàng theo khoảng thời gian (Báo cáo, thống kê - Admin)
    @GetMapping("/between")
    public ResponseEntity<List<OrderDTO>> getOrdersBetweenDates(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(orderService.findOrdersBetweenDates(start, end));
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<OrderSummaryDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.findAllOrders(PageRequest.of(page, size)));
    }

    // Cập nhật trạng thái đơn hàng (Admin)
    @PutMapping("/admin/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Integer id, @RequestParam OrderStatus newStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, newStatus));
    }

    // 3. Cập nhật đơn (Admin)
    @PutMapping("/admin/{id}")
    public ResponseEntity<OrderDTO> updateOrderAdmin(@PathVariable Integer id, @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.updateOrderAdmin(id, orderDTO));
    }
}
