package iuh.fit.repositories;

import iuh.fit.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT p FROM Product p WHERE " +
            "p.title LIKE %:keyword% " +
            "OR p.description LIKE %:keyword%")
    List<Product> search(@Param("keyword") String keyword);

    @Query(value = "SELECT p FROM Product p WHERE " +
            "p.title LIKE %:keyword% " +
            "OR p.description LIKE %:keyword% " +
            "ORDER BY p.id DESC")
    Page<Product> searchWithPaging(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") int categoryId);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice ORDER BY p.id DESC")
    Page<Product> findByPriceBetween(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId ORDER BY p.id DESC")
    Page<Product> findByCategoryIdWithPaging(@Param("categoryId") int categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.price BETWEEN :minPrice AND :maxPrice ORDER BY p.id DESC")
    Page<Product> findByCategoryAndPriceBetween(@Param("categoryId") int categoryId, @Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(p.title LIKE %:keyword% OR p.description LIKE %:keyword%) " +
            "AND p.price BETWEEN :minPrice AND :maxPrice " +
            "ORDER BY p.id DESC")
    Page<Product> searchWithKeywordAndPrice(@Param("keyword") String keyword, @Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.stock > :minStock ORDER BY p.id DESC")
    Page<Product> findByStockGreaterThan(@Param("minStock") int minStock, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.stock <= :maxStock")
    Page<Product> findByStockLessThanOrEqual(@Param("maxStock") int maxStock, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.stock = 0")
    Page<Product> findOutOfStock(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.stock > :minStock")
    Page<Product> findByCategoryAndStockGreaterThan(@Param("categoryId") int categoryId, @Param("minStock") int minStock, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.stock > :minStock")
    Page<Product> findByPriceAndStockGreaterThan(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice, @Param("minStock") int minStock, Pageable pageable);

}
