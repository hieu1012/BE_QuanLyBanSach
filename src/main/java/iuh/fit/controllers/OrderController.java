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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    // Lấy user hiện tại từ Security Context (Giả định có helper function)
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Vui lòng đăng nhập");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("User không tồn tại"));
    }

    //Tạo đơn hàng mới
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MASTER')")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        // Gắn User ID hiện tại vào DTO trước khi tạo
        User currentUser = getCurrentUser();
        // Đảm bảo user không thể đặt đơn cho người khác
        orderDTO.getUser().setId(currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDTO));
    }

    //Lịch sử đơn hàng (User)
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MASTER')")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(
            @RequestParam(required = false) OrderStatus status) {

        User currentUser = getCurrentUser();
        List<OrderDTO> orders;

        if (status != null) {
            orders = orderService.findOrdersByUserIdAndStatus(currentUser.getId().intValue(), status);
        } else {
            orders = orderService.findOrdersByUserId(currentUser.getId().intValue());
        }
        return ResponseEntity.ok(orders);
    }

    //Lấy đơn hàng theo ID
    //Chi tiết đơn hàng (User)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MASTER')")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer id) {
        User currentUser = getCurrentUser();
        // Logic kiểm tra quyền được chuyển vào Service
        return ResponseEntity.ok(orderService.findOrderByIdForUser(id, currentUser));
    }

}
