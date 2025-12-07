package iuh.fit.repositories;

import iuh.fit.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Tìm một CartItem cụ thể trong một Cart theo product ID
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Integer productId);

    // Xóa tất cả CartItem tham chiếu tới một product ID
    @Query("DELETE FROM CartItem c WHERE c.product.id = :productId")
    void deleteByProductId(@Param("productId") Integer productId);
}
