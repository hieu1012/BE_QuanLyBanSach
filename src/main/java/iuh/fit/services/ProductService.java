package iuh.fit.services;

import iuh.fit.dtos.ProductDTO;
import iuh.fit.entities.Product;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductDTO findById(Long id);
    List<ProductDTO> findAll();
    Page<ProductDTO> findAllWithPaging(@ParameterObject Pageable pageable);
    ProductDTO save(Product product);
    ProductDTO update(Long id, Product product);
    boolean delete(Long id);
    List<ProductDTO> search(String keyword);
    List<ProductDTO> findByCategory(Long categoryId);
}