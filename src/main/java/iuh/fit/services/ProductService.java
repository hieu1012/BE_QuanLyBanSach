package iuh.fit.services;

import iuh.fit.dtos.ProductDTO;
import iuh.fit.entities.Product;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductDTO findById(int id);

    List<ProductDTO> findAll();

    Page<ProductDTO> findAllWithPaging(@ParameterObject Pageable pageable);

    ProductDTO save(Product product);

    ProductDTO update(int id, Product product);

    boolean delete(int id);

    List<ProductDTO> search(String keyword);

    List<ProductDTO> findByCategoryId(int categoryId);

}
