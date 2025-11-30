package iuh.fit.services;

import iuh.fit.dtos.CategoryDTO;
import iuh.fit.entities.Category;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    CategoryDTO findById(int id);

    List<CategoryDTO> findAll();

    Page<CategoryDTO> findAllWithPaging(@ParameterObject Pageable pageable);

    CategoryDTO save(Category category);

    CategoryDTO update(int id, Category category);

    boolean delete(int id);

    List<CategoryDTO> search(String keyword);

    Page<CategoryDTO> searchWithPaging(String keyword, @ParameterObject Pageable pageable);
}
