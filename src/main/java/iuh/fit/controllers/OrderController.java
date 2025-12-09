package iuh.fit.controllers;

import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.dtos.order.OrderSummaryDTO;
import iuh.fit.entities.User;
import iuh.fit.entities.enums.OrderStatus;
import iuh.fit.exceptions.UnauthorizedException;
import iuh.fit.repositories.UserRepository;
import iuh.fit.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

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

    // Tạo đơn hàng mới
    @PostMapping
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderDTO orderDTO) {
        User currentUser = getCurrentUser();
        orderDTO.getUser().setId(currentUser.getId());

        OrderDTO createdOrder = orderService.createOrder(orderDTO);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("message", "Tạo đơn hàng thành công!");
        response.put("data", createdOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Lấy danh sách đơn hàng
    @GetMapping
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Map<String, Object>> getOrdersByFilter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @ParameterObject @PageableDefault(sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable) {

        User currentUser = getCurrentUser();
        Page<OrderSummaryDTO> orders = orderService.getOrdersByFilter(
                currentUser, keyword, status, startDate, endDate, pageable);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lấy danh sách đơn hàng thành công");
        response.put("data", orders);
        return ResponseEntity.ok(response);
    }

    // Lấy chi tiết đơn hàng
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Integer id) {
        User currentUser = getCurrentUser();
        OrderDTO order = orderService.findOrderByIdForUser(id, currentUser);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lấy thông tin đơn hàng thành công");
        response.put("data", order);
        return ResponseEntity.ok(response);
    }

    // User hủy đơn hàng
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Integer id) {
        User currentUser = getCurrentUser();
        OrderDTO cancelledOrder = orderService.cancelOrderByUser(id, currentUser);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Hủy đơn hàng thành công! Đã hoàn lại tồn kho.");
        response.put("data", cancelledOrder);
        return ResponseEntity.ok(response);
    }
}