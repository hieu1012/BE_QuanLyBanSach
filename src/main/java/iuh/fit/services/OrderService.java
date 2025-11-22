package iuh.fit.services;

import iuh.fit.dtos.order.OrderDTO;
import iuh.fit.dtos.order.OrderSummaryDTO;
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

    //Lấy danh sách đơn hàng có phân trang (dành cho admin)
    Page<OrderSummaryDTO> findAllOrders(Pageable pageable);

    //Lọc đơn hàng theo trạng thái (Pending, Delivered,...)
    List<OrderDTO> findOrdersByStatus(OrderStatus status);

    //Lọc đơn hàng trong khoảng thời gian
    List<OrderDTO> findOrdersBetweenDates(LocalDateTime start, LocalDateTime end);

    //Tìm kiếm đơn hàng theo từ khóa
    List<OrderDTO> searchOrders(String keyword);

    //Cập nhật trạng thái đơn hàng (admin)
    OrderDTO updateOrderStatus(Integer orderId, OrderStatus newStatus);

    // Xóa đơn hàng
    void deleteOrder(Integer id);

    // Cập nhật toàn bộ đơn hàng (dành cho Admin)
    OrderDTO updateOrderAdmin(Integer orderId, OrderDTO orderDTO);

}
