package iuh.fit.repositories;

import iuh.fit.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Tìm một CartItem cụ thể trong một Cart theo product ID
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Integer productId);
}
