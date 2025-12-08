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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;

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
        double calculatedTotalPrice = 0.0; //Tính tổng tiền

        for (OrderItemDTO dto : orderDTO.getItems()) {
            Product product = productRepository.findById(dto.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProduct().getId()));

            //Luôn lấy giá từ Product trong DB, bỏ qua dto.getPrice()
            Double currentPrice = product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice();

            OrderItem it = new OrderItem();
            it.setProduct(product);
            it.setQuantity(dto.getQuantity());
            it.setPrice(currentPrice); // Set giá từ DB
            it.setOrder(order);

            items.add(it);

            // Cộng dồn tổng tiền
            calculatedTotalPrice += (currentPrice * dto.getQuantity());
        }

        order.setItems(items);
        //Set tổng tiền do backend tính toán
        order.setTotalPrice(calculatedTotalPrice);

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

    @Override
    public Page<OrderSummaryDTO> getOrdersByFilter(
            User currentUser,
            String keyword,
            OrderStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {

        // 1. Phân quyền và tạo Specification cơ bản
        Specification<Order> spec = (root, query, cb) -> null;

        if (currentUser.getRole() == Role.USER) {
            // User chỉ xem đơn hàng của chính mình
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), currentUser.getId()));
        } else if (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.MASTER) {
            // Cấm nếu không phải USER, ADMIN, MASTER
            throw new ForbiddenException("Bạn không có quyền xem danh sách đơn hàng");
        }

        // 2. Thêm điều kiện Lọc theo Trạng thái
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        // 3. Thêm điều kiện Tìm kiếm theo Từ khóa (orderId hoặc fullName)
        if (keyword != null && !keyword.isBlank()) {
            String lowerCaseKeyword = "%" + keyword.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("orderId")), lowerCaseKeyword),
                    cb.like(cb.lower(root.get("user").get("fullName")), lowerCaseKeyword)
            ));
        }

        // 4. Thêm điều kiện Lọc theo Khoảng thời gian
        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, cb) -> cb.between(root.get("orderDate"), startDate, endDate));
        }

        // 5. Thực thi truy vấn với Phân trang
        Page<Order> orders = orderRepository.findAll(spec, pageable);

        // 6. Ánh xạ kết quả sang OrderSummaryDTO
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
    public OrderDTO updateOrderStatus(Integer orderId, OrderStatus newStatus){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));
        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);
        return modelMapper.map(updated, OrderDTO.class);
    }

    @Override
    @Transactional
    public OrderDTO requestCancelOrder(Integer orderId, User currentUser, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ItemNotFoundException("Đơn hàng không tồn tại"));

        // Kiểm tra quyền sở hữu
        if (!order.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Bạn không có quyền hủy đơn hàng này");
        }

        // Chỉ cho phép hủy khi đơn ở trạng thái PENDING hoặc PROCESSING
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PROCESSING) {
            throw new RuntimeException("Không thể hủy đơn hàng đang giao hoặc đã hoàn tất");
        }

        order.setStatus(OrderStatus.CANCEL_REQUESTED); // Chuyển trạng thái chờ duyệt
        order.setCancelReason(reason); // Lưu lý do

        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderDTO.class);
    }

    //Admin hủy/duyệt hủy
    @Override
    @Transactional
    public OrderDTO deleteOrder(Integer id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Order Not Found"));

        // Thay vì xóa cứng (deleteById), ta chuyển trạng thái thành CANCELLED
        order.setStatus(OrderStatus.CANCELLED);

        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderDTO.class);
    }

    @Override
    @Transactional
    public OrderDTO updateOrderAdmin(Integer orderId, OrderDTO orderDTO) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        // 1. Cập nhật địa chỉ giao hàng
        if (orderDTO.getOrderAddress() != null) {
            if (order.getOrderAddress() == null) {
                // Nếu chưa có địa chỉ, tạo mới (ModelMapper map ID null thì Hibernate sẽ tự gen ID mới -> OK)
                OrderAddress newAddress = modelMapper.map(orderDTO.getOrderAddress(), OrderAddress.class);
                order.setOrderAddress(newAddress);
            } else {
                // [SỬA LỖI TẠI ĐÂY]
                // Không dùng modelMapper.map() trực tiếp để tránh bị ghi đè ID thành null
                // Ta cập nhật thủ công các trường thông tin
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
                // TUYỆT ĐỐI KHÔNG SET ID
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
            // Xóa items cũ (orphanRemoval = true trong Entity sẽ giúp xóa khỏi DB)
            order.getItems().clear();

            // Thêm items mới
            List<OrderItem> newItems = new ArrayList<>();
            for (var dto : orderDTO.getItems()) {
                Product product = productRepository.findById(dto.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));

                OrderItem item = new OrderItem();
                item.setProduct(product);
                item.setQuantity(dto.getQuantity());
                item.setPrice(dto.getPrice());
                item.setOrder(order); // Quan trọng
                newItems.add(item);
            }
            order.getItems().addAll(newItems);
        }

        // 5. Cập nhật tổng đơn
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
