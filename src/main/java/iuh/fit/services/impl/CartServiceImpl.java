package iuh.fit.services.impl;

import iuh.fit.dtos.CheckoutRequest;
import iuh.fit.dtos.cart.AddToCartRequest;
import iuh.fit.dtos.cart.CartDTO;
import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.entities.Cart;
import iuh.fit.entities.CartItem;
import iuh.fit.entities.Product;
import iuh.fit.entities.User;
import iuh.fit.exceptions.ItemNotFoundException;
import iuh.fit.repositories.CartItemRepository;
import iuh.fit.repositories.CartRepository;
import iuh.fit.repositories.ProductRepository;
import iuh.fit.services.CartService;
import iuh.fit.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService; // Dùng để gọi khi Checkout
    private final ModelMapper modelMapper;

    private Cart findOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .totalAmount(0.0)
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    private void recalculateCart(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
        cart.setTotalAmount(total);
        cartRepository.save(cart);
    }

    //Giỏ hàng
    @Override
    @Transactional(readOnly = true)
    public CartDTO getCartByUser(User currentUser) {
        Cart cart = findOrCreateCart(currentUser);
        return modelMapper.map(cart, CartDTO.class);
    }

    //Thêm vào giỏ
    @Override
    @Transactional
    public CartDTO addOrUpdateItem(User currentUser, AddToCartRequest request) {
        Cart cart = findOrCreateCart(currentUser);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy sản phẩm có ID: " + request.getProductId()));

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId());

        CartItem item;
        if (existingItem.isPresent()) {
            // Cập nhật số lượng
            item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            // Thêm mới
            item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .unitPrice(product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice())
                    .build();
            cart.getItems().add(item);
        }

        cartItemRepository.save(item);
        recalculateCart(cart);
        return modelMapper.map(cart, CartDTO.class);
    }

    //Cập nhật giỏ hàng
    @Override
    @Transactional
    public CartDTO updateItemQuantity(User currentUser, Integer productId, Integer quantity) {
        if (quantity <= 0) {
            return removeItem(currentUser, productId);
        }

        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy giỏ hàng của user"));

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng"));

        item.setQuantity(quantity);
        cartItemRepository.save(item);
        recalculateCart(cart);
        return modelMapper.map(cart, CartDTO.class);
    }

    //Xóa sản phẩm khỏi giỏ
    @Override
    @Transactional
    public CartDTO removeItem(User currentUser, Integer productId) {
        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy giỏ hàng của user"));

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng"));

        cartItemRepository.delete(item);
        cart.getItems().remove(item);

        recalculateCart(cart);
        return modelMapper.map(cart, CartDTO.class);
    }

    //Thanh toán
    @Override
    @Transactional
    public OrderDTO checkout(User currentUser, CheckoutRequest request) { // [ĐÃ SỬA] Thêm tham số request và thay đổi kiểu trả về
        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy giỏ hàng của user"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng rỗng, không thể thanh toán.");
        }

        // --- BƯỚC 1: CHUYỂN CART THÀNH ORDER (Gọi OrderService mới) ---
        OrderDTO newOrder = orderService.checkout(currentUser, cart, request);

        // --- BƯỚC 2: XÓA GIỎ HÀNG SAU KHI TẠO ĐƠN ---
        cart.getItems().clear();
        cart.setTotalAmount(0.0);
        cartRepository.save(cart);

        // [ĐÃ SỬA] Trả về OrderDTO mới tạo
        return newOrder;
    }
}