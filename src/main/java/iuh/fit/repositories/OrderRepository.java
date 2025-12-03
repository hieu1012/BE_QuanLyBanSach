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

    //Lấy danh sách đơn hàng có phân trang
    @EntityGraph(attributePaths = {"items", "items.product", "user"})
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    Page<Order> findAllWithPaging(Pageable pageable);

    //Tìm đơn hàng theo trạng thái
    List<Order> findByStatus(OrderStatus status);

    //Tìm kiếm đơn hàng trong khoảng thời gian
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :start AND :end ORDER BY o.orderDate DESC")
    List<Order> findOrdersBetweenDates(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    //Tìm kiếm đơn hàng theo mã đơn hoặc tên khách hàng
    @Query("SELECT o FROM Order o WHERE LOWER(o.orderId) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(o.user.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Order> search(@Param("keyword") String keyword);

    //Đếm số đơn hàng theo trạng thái (Dùng cho dashboard admin)
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    long countByStatus(@Param("status") OrderStatus status);

    // Lấy tất cả đơn hàng theo trạng thái và theo user
    List<Order> findByUserIdAndStatus(Integer userId, OrderStatus status);

    // Lấy tổng doanh thu trong một khoảng thời gian
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderDate BETWEEN :start AND :end")
    Double getTotalRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    //Thống kê tổng doanh thu và tổng số đơn hàng theo Ngày
    @Query(value = "SELECT DATE(o.orderDate), SUM(o.totalPrice), COUNT(o) " +
            "FROM Order o " +
            "WHERE o.status = iuh.fit.entities.enums.OrderStatus.DELIVERED " +
            "AND o.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(o.orderDate) " +
            "ORDER BY DATE(o.orderDate)")
    List<Object[]> getDailyStats(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    //Thống kê tổng doanh thu và tổng số đơn hàng theo Tháng
    @Query(value = "SELECT MONTH(o.orderDate), YEAR(o.orderDate), SUM(o.totalPrice), COUNT(o) " +
            "FROM Order o " +
            "WHERE o.status = iuh.fit.entities.enums.OrderStatus.DELIVERED " +
            "AND YEAR(o.orderDate) = :year " +
            "GROUP BY MONTH(o.orderDate), YEAR(o.orderDate) " +
            "ORDER BY YEAR(o.orderDate), MONTH(o.orderDate)")
    List<Object[]> getMonthlyStats(@Param("year") Integer year);

    //Thống kê Khách hàng Top: Tổng giá trị và số lượng đơn hàng của User
    @Query(value = "SELECT o.user.id, o.user.fullName, COUNT(o), SUM(o.totalPrice) " +
            "FROM Order o " +
            "WHERE o.status = iuh.fit.entities.enums.OrderStatus.DELIVERED " +
            "GROUP BY o.user.id, o.user.fullName " +
            "ORDER BY SUM(o.totalPrice) DESC")
    List<Object[]> getTopCustomerStats(Pageable pageable);
}
