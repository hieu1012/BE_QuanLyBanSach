package iuh.fit.services.impl;

import iuh.fit.dtos.CheckoutRequest;
import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.dtos.order.OrderItemDTO;
import iuh.fit.dtos.order.OrderSummaryDTO;
import iuh.fit.entities.*;
import iuh.fit.entities.enums.OrderStatus;
import iuh.fit.entities.enums.Role;
import iuh.fit.exceptions.ForbiddenException;
import iuh.fit.exceptions.ItemNotFoundException;
import iuh.fit.repositories.OrderItemRepository;
import iuh.fit.repositories.OrderRepository;
import iuh.fit.repositories.ProductRepository;
import iuh.fit.repositories.UserRepository;
import iuh.fit.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository  orderItemRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private String generateOrderId() {
        return "ORD-" + System.currentTimeMillis();
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
        for (OrderItemDTO dto : orderDTO.getItems()) {
            Product product = productRepository.findById(dto.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProduct().getId()));

            // Optionally check stock:
            // if (product.getStock() < dto.getQuantity()) throw new RuntimeException("Insufficient stock...");

            OrderItem it = new OrderItem();
            it.setProduct(product);
            it.setQuantity(dto.getQuantity());
            // Prefer using product price (or dto.price if that's business rule)
            it.setPrice(dto.getPrice() != null ? dto.getPrice() : product.getPrice());
            it.setOrder(order);
            items.add(it);
        }

        order.setItems(items);
        order.setTotalPrice(orderDTO.getTotalPrice());
        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderDTO.class);
    }

    //Lấy danh sách đơn hàng của 1 user
    @Override
    public List<OrderDTO> findOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    //Lấy danh sách đơn hàng có phân trang
    @Override
    @EntityGraph(attributePaths = {"items", "items.product", "user"})
    public Page<OrderSummaryDTO> findAllOrders(Pageable pageable){
        return orderRepository.findAllWithPaging(pageable)
                .map(o -> {
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

    //Lọc theo trạng thái
    @Override
    public List<OrderDTO> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status)
                .stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    //Lọc đơn hàng trong khoảng thời gian
    public List<OrderDTO> findOrdersBetweenDates(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findOrdersBetweenDates(start, end)
                .stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    //tìm kiếm đơn hàng
    @Override
    public List<OrderDTO> searchOrders(String keyword){
        return orderRepository.search(keyword)
                .stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    //Cập nhật trạng thái đơn hàng
    @Override
    public OrderDTO updateOrderStatus(Integer orderId, OrderStatus newStatus){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));
        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);
        return modelMapper.map(updated, OrderDTO.class);
    }

    @Override
    @Transactional
    public void deleteOrder(Integer id){
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public OrderDTO updateOrderAdmin(Integer orderId, OrderDTO orderDTO) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        // Cập nhật địa chỉ giao hàng
        if (orderDTO.getOrderAddress() != null) {
            modelMapper.map(orderDTO.getOrderAddress(), order.getOrderAddress());
        }

        // Cập nhật payment type
        if (orderDTO.getPaymentType() != null) {
            order.setPaymentType(orderDTO.getPaymentType());
        }

        // Cập nhật trạng thái đơn hàng
        if (orderDTO.getStatus() != null) {
            order.setStatus(orderDTO.getStatus());
        }

        // Cập nhật items
        if (orderDTO.getItems() != null) {
            order.getItems().clear();
            orderDTO.getItems().forEach(dto -> {
                var item = modelMapper.map(dto, OrderItem.class);
                item.setOrder(order);
                order.getItems().add(item);
            });
        }

        // Cập nhật tổng đơn
        if (orderDTO.getTotalPrice() != null) {
            order.setTotalPrice(orderDTO.getTotalPrice());
        }

        Order updated = orderRepository.save(order);
        return modelMapper.map(updated, OrderDTO.class);
    }
    //Lấy chi tiết đơn hàng, kiểm tra User có phải là chủ đơn hàng không
    @Override
    @Transactional(readOnly = true)
    public OrderDTO findOrderByIdForUser(Integer orderId, User currentUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy đơn hàng: " + orderId));

        // Kiểm tra quyền: Chỉ cho phép chủ đơn hàng (hoặc Admin/Master) xem
        if (currentUser.getRole() != Role.MASTER && currentUser.getRole() != Role.ADMIN) {
            if (!order.getUser().getId().equals(currentUser.getId())) {
                throw new ForbiddenException("Bạn không có quyền xem đơn hàng này");
            }
        }

        return modelMapper.map(order, OrderDTO.class);
    }

    //Lấy đơn hàng theo User và Status
    @Override
    public List<OrderDTO> findOrdersByUserIdAndStatus(Integer userId, OrderStatus status) {
        return orderRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    //Lấy đơn hàng theo ID
    @Override
    @Transactional(readOnly = true)
    public OrderDTO findOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy đơn hàng: " + orderId));
        return modelMapper.map(order, OrderDTO.class);
    }

    //Yêu cầu Thanh toán
    @Override
    @Transactional
    public OrderDTO checkout(User currentUser, Cart cart, CheckoutRequest request) {
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng rỗng, không thể thanh toán.");
        }

        Order order = new Order();
        order.setOrderId(generateOrderId());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING); // Đơn mới luôn ở trạng thái PENDING
        order.setPaymentType(request.getPaymentType());

        // 1. Thiết lập User
        order.setUser(currentUser);

        // 2. Thiết lập Địa chỉ (snapshot)
        OrderAddress address = modelMapper.map(request.getOrderAddress(), OrderAddress.class);
        order.setOrderAddress(address);

        // 3. Chuyển Cart Items thành Order Items
        List<OrderItem> items = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            // Kiểm tra stock ở đây (nếu cần)

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            // Lấy giá từ CartItem (đã được lưu khi thêm vào giỏ)
            orderItem.setPrice(cartItem.getUnitPrice());
            orderItem.setOrder(order);
            items.add(orderItem);
        }

        order.setItems(items);
        order.setTotalPrice(cart.getTotalAmount()); // Tổng tiền lấy từ Cart Entity

        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderDTO.class);
    }

}
