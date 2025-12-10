package iuh.fit.controllers;

import iuh.fit.dtos.CheckoutRequest;
import iuh.fit.dtos.cart.AddToCartRequest;
import iuh.fit.dtos.cart.CartDTO;
import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.entities.User;
import iuh.fit.exceptions.UnauthorizedException;
import iuh.fit.repositories.UserRepository;
import iuh.fit.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MASTER', 'ADMIN', 'USER')")
public class CartController {

    private final CartService cartService;
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

    // Lấy giỏ hàng
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCart() {
        User currentUser = getCurrentUser();
        CartDTO cart = cartService.getCartByUser(currentUser);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lấy thông tin giỏ hàng thành công");
        response.put("data", cart);
        return ResponseEntity.ok(response);
    }

    // Thêm vào giỏ
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(
            @Valid @RequestBody AddToCartRequest request) {
        User currentUser = getCurrentUser();
        CartDTO cart = cartService.addOrUpdateItem(currentUser, request);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Đã thêm sản phẩm vào giỏ hàng");
        response.put("data", cart);
        return ResponseEntity.ok(response);
    }

    // Cập nhật số lượng
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateCartItem(
            @Valid @RequestBody AddToCartRequest request) {
        User currentUser = getCurrentUser();
        CartDTO cart = cartService.updateItemQuantity(currentUser, request.getProductId(), request.getQuantity());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Cập nhật số lượng sản phẩm thành công");
        response.put("data", cart);
        return ResponseEntity.ok(response);
    }

    // Xóa khỏi giỏ
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Map<String, Object>> removeCartItem(@PathVariable("id") Integer productId) {
        User currentUser = getCurrentUser();
        CartDTO cart = cartService.removeItem(currentUser, productId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Đã xóa sản phẩm khỏi giỏ hàng");
        response.put("data", cart);
        return ResponseEntity.ok(response);
    }

    // Thanh toán
    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout(
            @Valid @RequestBody CheckoutRequest request) {

        User currentUser = getCurrentUser();
        OrderDTO newOrder = cartService.checkout(currentUser, request);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("message", "Thanh toán thành công! Đơn hàng đang được xử lý.");
        response.put("data", newOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}