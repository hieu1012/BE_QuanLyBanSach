package iuh.fit.repositories;

import iuh.fit.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT p FROM Product p WHERE " +
            "p.title LIKE %:keyword% " +
            "OR p.description LIKE %:keyword% " +
            "OR p.imageName LIKE %:keyword%")
    List<Product> search(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") int categoryId);

}
