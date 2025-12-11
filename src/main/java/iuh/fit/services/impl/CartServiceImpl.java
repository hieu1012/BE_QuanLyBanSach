package iuh.fit.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.dtos.CheckoutRequest;
import iuh.fit.dtos.ProductDTO;
import iuh.fit.dtos.cart.AddToCartRequest;
import iuh.fit.dtos.cart.CartDTO;
import iuh.fit.dtos.cart.CartItemDTO;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    private void enrichCartImages(CartDTO cartDTO) {
        if (cartDTO.getItems() == null) return;

        String baseUrl = "https://res.cloudinary.com/dcedtiyrf/image/upload/q_auto,f_auto/";

        for (CartItemDTO itemDTO : cartDTO.getItems()) {
            ProductDTO productDTO = itemDTO.getProduct();
            try {
                Product product = productRepository.findById(productDTO.getId()).orElse(null);
                if (product != null && product.getImageNames() != null) {
                    List<String> imageNames = Arrays.asList(objectMapper.readValue(product.getImageNames(), String[].class));

                    List<String> imageUrls = imageNames.stream()
                            .map(name -> baseUrl + name)
                            .collect(Collectors.toList());

                    productDTO.setImageUrls(imageUrls);
                    productDTO.setImageNames(imageNames);
                }
            } catch (Exception e) {
                productDTO.setImageUrls(new ArrayList<>());
            }
        }
    }

    private Cart findOrCreateCart(User user) {
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .totalAmount(0.0)
//                            .items(new ArrayList<>())
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

        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            //cart.getItems().sort((item1, item2) -> item2.getId().compareTo(item1.getId()));
            cart.getItems().sort(Comparator.comparing(CartItem::getId).reversed());
        }

        CartDTO dto = modelMapper.map(cart, CartDTO.class);
        enrichCartImages(dto);
        return dto;
    }

    //Thêm vào giỏ
    @Override
    @Transactional
    public CartDTO addOrUpdateItem(User currentUser, AddToCartRequest request) {
        Cart cart = findOrCreateCart(currentUser);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy sản phẩm: " + request.getProductId()));

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .unitPrice(product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice())
                    .build();
            cart.getItems().add(item);
            cartItemRepository.save(item);
        }

        recalculateCart(cart);

        if (cart.getItems() != null) cart.getItems().sort(Comparator.comparing(CartItem::getId).reversed());
        CartDTO dto = modelMapper.map(cart, CartDTO.class);
        enrichCartImages(dto);
        return dto;
    }

    //Cập nhật giỏ hàng
    @Override
    @Transactional
    public CartDTO updateItemQuantity(User currentUser, Integer productId, Integer quantity) {
        if (quantity <= 0) {
            return removeItem(currentUser, productId);
        }

        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ItemNotFoundException("Cart not found"));

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found in cart"));

        item.setQuantity(quantity);
        cartItemRepository.save(item);
        recalculateCart(cart);

        CartDTO dto = modelMapper.map(cart, CartDTO.class);
        enrichCartImages(dto);
        return dto;
    }

    //Xóa sản phẩm khỏi giỏ
    @Override
    @Transactional
    public CartDTO removeItem(User currentUser, Integer productId) {
        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ItemNotFoundException("Cart not found"));

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found"));

        cartItemRepository.delete(item);
        cart.getItems().remove(item);

        recalculateCart(cart);

        CartDTO dto = modelMapper.map(cart, CartDTO.class);
        enrichCartImages(dto);
        return dto;
    }

    //Thanh toán
    @Override
    @Transactional
    public OrderDTO checkout(User currentUser, CheckoutRequest request) {
        Cart cart = cartRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ItemNotFoundException("Cart not found"));

//        if (cart.getItems().isEmpty()) {
//            throw new RuntimeException("Giỏ hàng rỗng");
//        }

        OrderDTO newOrder = orderService.checkout(currentUser, cart, request);

        // cart.getItems().clear();
        // cart.setTotalAmount(0.0);

        cartRepository.save(cart);

        return newOrder;
    }
}