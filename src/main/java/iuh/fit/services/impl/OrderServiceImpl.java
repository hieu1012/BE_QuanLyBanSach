package iuh.fit.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.dtos.CheckoutRequest;
import iuh.fit.dtos.ProductDTO;
import iuh.fit.dtos.cart.CartItemDTO;
import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.dtos.order.OrderItemDTO;
import iuh.fit.dtos.order.OrderSummaryDTO;
import iuh.fit.entities.*;
import iuh.fit.entities.enums.OrderStatus;
import iuh.fit.entities.enums.Role;
import iuh.fit.exceptions.ForbiddenException;
import iuh.fit.exceptions.ItemNotFoundException;
import iuh.fit.repositories.*;
import iuh.fit.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository  orderItemRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final CartRepository cartRepository;

    private String generateOrderId() {
        return "ORD-" + System.currentTimeMillis();
    }

    //Xử lý trừ tồn kho
    private void deductStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw new RuntimeException("Sản phẩm '" + product.getTitle() + "' không đủ số lượng tồn kho (Còn: " + product.getStock() + ")");
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    //Xử lý hoàn tồn kho (dùng khi hủy/xóa)
    private void restoreStock(Order order) {
//        for (OrderItem item : order.getItems()) {
//            Product product = productRepository.findById(item.getProduct().getId()).orElse(null);
//            if (product != null) {
//                product.setStock(product.getStock() + item.getQuantity());
//                productRepository.save(product);
//            }
//        }
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());

        if (items.isEmpty()) {
            System.out.println("CẢNH BÁO: Không tìm thấy sản phẩm nào trong đơn hàng " + order.getId() + " để hoàn kho.");
            return;
        }

        for (OrderItem item : items) {
            Product product = productRepository.findById(item.getProduct().getId()).orElse(null);

            if (product != null) {
                int oldStock = product.getStock();
                int quantityToRestore = item.getQuantity();

                product.setStock(oldStock + quantityToRestore);

                productRepository.save(product);

            }
        }
    }

    // Xử lý ảnh
    private void enrichProductImage(OrderDTO orderDTO) {
        String baseUrl = "https://res.cloudinary.com/dcedtiyrf/image/upload/q_auto,f_auto/";

        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
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


    //Tạo dơn hàng mới
    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderId(generateOrderId());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentType(orderDTO.getPaymentType());

        User user = userRepository.findById(orderDTO.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);

        OrderAddress address = modelMapper.map(orderDTO.getOrderAddress(), OrderAddress.class);
        order.setOrderAddress(address);

        List<OrderItem> items = new ArrayList<>();
        double calculatedTotalPrice = 0.0;

        for (OrderItemDTO dto : orderDTO.getItems()) {
            Product product = productRepository.findById(dto.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProduct().getId()));

            deductStock(product, dto.getQuantity());

            Double currentPrice = product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice();

            OrderItem it = new OrderItem();
            it.setProduct(product);
            it.setQuantity(dto.getQuantity());
            it.setPrice(currentPrice);
            it.setOrder(order);
            items.add(it);

            calculatedTotalPrice += (currentPrice * dto.getQuantity());
        }

        order.setItems(items);
        order.setTotalPrice(calculatedTotalPrice);

        Order saved = orderRepository.save(order);

        Optional<Cart> cartOptional = cartRepository.findByUserId(user.getId());

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();

            cart.getItems().clear();
            cart.setTotalAmount(0.0);

            cartRepository.save(cart);
        }

        OrderDTO resultDTO = modelMapper.map(saved, OrderDTO.class);
        enrichProductImage(resultDTO);
        return resultDTO;
    }

    //Lấy danh sách đơn hàng của 1 user
    @Override
    public List<OrderDTO> findOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(o -> {
                    OrderDTO dto = modelMapper.map(o, OrderDTO.class);
                    enrichProductImage(dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public Page<OrderSummaryDTO> getOrdersByFilter(User currentUser, String keyword, OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Specification<Order> spec = (root, query, cb) -> null;
        if (currentUser.getRole() == Role.USER) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), currentUser.getId()));
        } else if (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.MASTER) {
            throw new ForbiddenException("Không có quyền");
        }
        if (status != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        if (keyword != null && !keyword.isBlank()) {
            String k = "%" + keyword.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(cb.like(cb.lower(root.get("orderId")), k), cb.like(cb.lower(root.get("user").get("fullName")), k)));
        }
        if (startDate != null && endDate != null) spec = spec.and((root, query, cb) -> cb.between(root.get("orderDate"), startDate, endDate));

        Page<Order> orders = orderRepository.findAll(spec, pageable);
        return orders.map(o -> {
            OrderSummaryDTO s = new OrderSummaryDTO();
            s.setId(o.getId());
            s.setOrderId(o.getOrderId());
            s.setOrderDate(o.getOrderDate());
            s.setStatus(o.getStatus().name());
            s.setPaymentType(o.getPaymentType().name());
            s.setTotalPrice(o.getTotalPrice());
            if (o.getItems() != null && !o.getItems().isEmpty()) {
                s.setFirstProductTitle(o.getItems().get(0).getProduct().getTitle());
                s.setTotalItem(o.getItems().stream().mapToInt(OrderItem::getQuantity).sum());
            }
            s.setUserEmail(o.getUser() != null ? o.getUser().getEmail() : null);
            return s;
        });
    }

    //Cập nhật trạng thái đơn hàng
    @Override
    public OrderDTO updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Not Found"));
        order.setStatus(newStatus);
        return modelMapper.map(orderRepository.save(order), OrderDTO.class);
    }

    @Override
    @Transactional
    public OrderDTO cancelOrderByUser(Integer orderId, User currentUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ItemNotFoundException("Đơn hàng không tồn tại"));

        if (!order.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Bạn không có quyền hủy đơn hàng này");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể hủy đơn hàng đang chờ xử lý");
        }

        restoreStock(order);
        order.setStatus(OrderStatus.CANCELLED);
        Order saved = orderRepository.save(order);

        OrderDTO dto = modelMapper.map(saved, OrderDTO.class);
        enrichProductImage(dto);
        return dto;
    }

    //Xóa đơn hàng (Admin)
    @Override
    @Transactional
    public OrderDTO deleteOrder(Integer id){
        Order order = orderRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Not Found"));
        OrderDTO dto = modelMapper.map(order, OrderDTO.class);
        enrichProductImage(dto);

        restoreStock(order);
        orderRepository.delete(order);
        return dto;
    }

    @Override
    @Transactional
    public OrderDTO updateOrderAdmin(Integer orderId, OrderDTO orderDTO) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        // 1. Cập nhật địa chỉ giao hàng
        if (orderDTO.getOrderAddress() != null) {
            if (order.getOrderAddress() == null) {
                OrderAddress newAddress = modelMapper.map(orderDTO.getOrderAddress(), OrderAddress.class);
                order.setOrderAddress(newAddress);
            } else {
                var newAddrInfo = orderDTO.getOrderAddress();
                var currentAddr = order.getOrderAddress();

                currentAddr.setFirstName(newAddrInfo.getFirstName());
                currentAddr.setLastName(newAddrInfo.getLastName());
                currentAddr.setAddress(newAddrInfo.getAddress());
                currentAddr.setCity(newAddrInfo.getCity());
                currentAddr.setState(newAddrInfo.getState());
                currentAddr.setPincode(newAddrInfo.getPincode());
                currentAddr.setMobileNo(newAddrInfo.getMobileNo());
                currentAddr.setEmail(newAddrInfo.getEmail());
            }
        }

        // 2. Cập nhật payment type
        if (orderDTO.getPaymentType() != null) {
            order.setPaymentType(orderDTO.getPaymentType());
        }

        // 3. Cập nhật trạng thái đơn hàng
        if (orderDTO.getStatus() != null) {
            order.setStatus(orderDTO.getStatus());
        }

        // 4. Cập nhật items
        if (orderDTO.getItems() != null) {
            order.getItems().clear();
            List<OrderItem> newItems = new ArrayList<>();
            for (var dto : orderDTO.getItems()) {
                Product product = productRepository.findById(dto.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                OrderItem item = new OrderItem();
                item.setProduct(product);
                item.setQuantity(dto.getQuantity());
                item.setPrice(dto.getPrice());
                item.setOrder(order);
                newItems.add(item);
            }
            order.getItems().addAll(newItems);
        }

        // 5. Cập nhật tổng đơn
        if (orderDTO.getTotalPrice() != null) {
            order.setTotalPrice(orderDTO.getTotalPrice());
        }

        Order updated = orderRepository.save(order);
        OrderDTO dto = modelMapper.map(updated, OrderDTO.class);
        enrichProductImage(dto);
        return dto;
    }


    //Lấy chi tiết đơn hàng
    @Override
    @Transactional(readOnly = true)
    public OrderDTO findOrderByIdForUser(Integer orderId, User currentUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy đơn hàng: " + orderId));

        if (currentUser.getRole() != Role.MASTER && currentUser.getRole() != Role.ADMIN) {
            if (!order.getUser().getId().equals(currentUser.getId())) {
                throw new ForbiddenException("Bạn không có quyền xem đơn hàng này");
            }
        }

        OrderDTO dto = modelMapper.map(order, OrderDTO.class);
        enrichProductImage(dto);
        return dto;
    }

    //Lấy đơn hàng theo User và Status
    @Override
    public List<OrderDTO> findOrdersByUserIdAndStatus(Integer userId, OrderStatus status) {
        return orderRepository.findByUserIdAndStatus(userId, status).stream()
                .map(o -> {
                    OrderDTO dto = modelMapper.map(o, OrderDTO.class);
                    enrichProductImage(dto);
                    return dto;
                }).collect(Collectors.toList());
    }

    //Lấy đơn hàng theo ID
    @Override
    public OrderDTO findOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ItemNotFoundException("Not Found"));
        OrderDTO dto = modelMapper.map(order, OrderDTO.class);
        enrichProductImage(dto);
        return dto;
    }

    //Yêu cầu Thanh toán
    @Override
    @Transactional
    public OrderDTO checkout(User currentUser, Cart cart, CheckoutRequest request) {
//        if (cart.getItems().isEmpty()) {
//            throw new RuntimeException("Giỏ hàng rỗng, không thể thanh toán.");
//        }

        Order order = new Order();
        order.setOrderId(generateOrderId());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PROCESSING);
        order.setPaymentType(request.getPaymentType());
        order.setUser(currentUser);

        OrderAddress address = modelMapper.map(request.getOrderAddress(), OrderAddress.class);
        address.setId(null);
        order.setOrderAddress(address);

        List<OrderItem> items = new ArrayList<>();
        double calculatedTotal = 0.0;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            deductStock(product, cartItem.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());

            Double itemPrice = cartItem.getUnitPrice();
            orderItem.setPrice(itemPrice);
            orderItem.setOrder(order);
            items.add(orderItem);

            calculatedTotal += (itemPrice * cartItem.getQuantity());
        }

        order.setItems(items);
        order.setTotalPrice(calculatedTotal);

//        Order saved = orderRepository.save(order);

        OrderDTO resultDTO = modelMapper.map(order, OrderDTO.class);
        enrichProductImage(resultDTO);
        return resultDTO;
    }

}
