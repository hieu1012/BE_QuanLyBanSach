package iuh.fit.repositories;

import iuh.fit.entities.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    //Lấy chi tiết các sản phẩm trong 1 đơn hàng
    @Query("SELECT i FROM OrderItem i WHERE i.order.id = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") Integer orderId);

    //Thống kê số lượng sản phẩm được bán theo productId
    @Query("SELECT SUM(i.quantity) FROM OrderItem i WHERE i.product.id = :productId")
    Long countSoldByProductId(@Param("productId") Integer productId);

    //Thống kê Sản phẩm Bán chạy: Tổng số lượng bán và Tổng giá trị
    @Query(value = "SELECT i.product.title, SUM(i.quantity), SUM(i.price * i.quantity) " +
            "FROM OrderItem i " +
            "WHERE i.order.status = iuh.fit.entities.enums.OrderStatus.DELIVERED " +
            "GROUP BY i.product.title " +
            "ORDER BY SUM(i.quantity) DESC")
    List<Object[]> getTopSellingProductStats(Pageable pageable);
}
