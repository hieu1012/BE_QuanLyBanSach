package iuh.fit.repositories;

import iuh.fit.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value = "SELECT c FROM Category c WHERE c.name LIKE %:keyword%")
    List<Category> search(@Param("keyword") String keyword);
}
