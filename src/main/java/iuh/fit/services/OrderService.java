package iuh.fit.services;

import iuh.fit.dtos.CheckoutRequest;
import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.dtos.order.OrderSummaryDTO;
import iuh.fit.entities.Cart;
import iuh.fit.entities.User;
import iuh.fit.entities.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;


public interface OrderService {
    //Tạo đơn hàng mới
    OrderDTO createOrder(OrderDTO orderDTO);

    //Lấy chi tiết 1 đơn hàng theo ID
    OrderDTO findOrderById(Integer orderId);

    //Lấy danh sách đơn hàng của một user (lịch sử mua hàng)
    List<OrderDTO> findOrdersByUserId(Integer userId);

    Page<OrderSummaryDTO> getOrdersByFilter(
            User currentUser,
            String keyword,
            OrderStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    //Cập nhật trạng thái đơn hàng (admin)
    OrderDTO updateOrderStatus(Integer orderId, OrderStatus newStatus);

    // Xóa đơn hàng
    void deleteOrder(Integer id);

    // Cập nhật toàn bộ đơn hàng (dành cho Admin)
    OrderDTO updateOrderAdmin(Integer orderId, OrderDTO orderDTO);

    //Lấy đơn hàng theo ID, có kiểm tra quyền USER
    OrderDTO findOrderByIdForUser(Integer orderId, User currentUser);

    //Lấy đơn hàng theo User và Status
    List<OrderDTO> findOrdersByUserIdAndStatus(Integer userId, OrderStatus status);

    //Yêu cầu Thanh toán
    OrderDTO checkout(User currentUser, Cart cart, CheckoutRequest request);

}
