package iuh.fit.repositories;

import iuh.fit.entities.OrderItem;
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
    Integer countSoldByProductId(@Param("productId") Integer productId);


}
