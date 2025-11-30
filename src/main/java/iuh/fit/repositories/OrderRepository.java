package iuh.fit.repositories;

import iuh.fit.entities.Order;
import iuh.fit.entities.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer>, JpaSpecificationExecutor<Order> {

    //Lấy danh sách đơn hàng của 1 user (dùng cho "Lịch sử đơn hàng")
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.orderDate DESC")
    List<Order> findByUserId(@Param("userId") Integer userId);

    //Lấy danh sách đơn hàng có phân trang (DƯỚI DẠNG SPECIFICATION CHUNG ĐÃ THAY THẾ)
    // Giữ lại nếu cần cho các service cũ chưa refactor, nhưng sẽ không được dùng trong API mới
    @EntityGraph(attributePaths = {"items", "items.product", "user"})
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    Page<Order> findAllWithPaging(Pageable pageable);

    //Tìm đơn hàng theo trạng thái (DƯỚI DẠNG SPECIFICATION CHUNG ĐÃ THAY THẾ)
    List<Order> findByStatus(OrderStatus status);

    //Tìm kiếm đơn hàng trong khoảng thời gian (DƯỚI DẠNG SPECIFICATION CHUNG ĐÃ THAY THẾ)
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :start AND :end ORDER BY o.orderDate DESC")
    List<Order> findOrdersBetweenDates(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    //Tìm kiếm đơn hàng theo mã đơn hoặc tên khách hàng (DƯỚI DẠNG SPECIFICATION CHUNG ĐÃ THAY THẾ)
    @Query("SELECT o FROM Order o WHERE LOWER(o.orderId) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(o.user.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Order> search(@Param("keyword") String keyword);

    //Đếm số đơn hàng theo trạng thái (Dùng cho dashboard admin)
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    long countByStatus(@Param("status") OrderStatus status);

    // Lấy tất cả đơn hàng theo trạng thái và theo user (giúp người dùng lọc nhanh)
    List<Order> findByUserIdAndStatus(Integer userId, OrderStatus status);

    // Lấy tổng doanh thu trong một khoảng thời gian
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderDate BETWEEN :start AND :end")
    Double getTotalRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
