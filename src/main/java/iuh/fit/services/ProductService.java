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

    Page<ProductDTO> searchWithPaging(String keyword, @ParameterObject Pageable pageable);

    List<ProductDTO> findByCategoryId(int categoryId);

    /**
     * Tìm kiếm sản phẩm theo khoảng giá
     */
    Page<ProductDTO> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable);

    /**
     * Tìm kiếm sản phẩm theo category (có phân trang)
     */
    Page<ProductDTO> findByCategoryIdWithPaging(int categoryId, Pageable pageable);

    /**
     * Tìm kiếm sản phẩm theo category và khoảng giá
     */
    Page<ProductDTO> findByCategoryAndPriceBetween(int categoryId, double minPrice, double maxPrice, Pageable pageable);

    /**
     * Tìm kiếm sản phẩm theo keyword và khoảng giá
     */
    Page<ProductDTO> searchWithKeywordAndPrice(String keyword, double minPrice, double maxPrice, Pageable pageable);

    /**
     * Tìm kiếm sản phẩm có tồn kho > minStock
     */
    Page<ProductDTO> findByStockGreaterThan(int minStock, Pageable pageable);

    /**
     * Tìm kiếm sản phẩm có tồn kho <= maxStock
     */
    Page<ProductDTO> findByStockLessThanOrEqual(int maxStock, Pageable pageable);

    /**
     * Tìm kiếm sản phẩm hết hàng (stock = 0)
     */
    Page<ProductDTO> findOutOfStock(Pageable pageable);

    /**
     * Tìm kiếm sản phẩm theo category và tồn kho
     */
    Page<ProductDTO> findByCategoryAndStockGreaterThan(int categoryId, int minStock, Pageable pageable);

    /**
     * Tìm kiếm sản phẩm theo giá và tồn kho
     */
    Page<ProductDTO> findByPriceAndStockGreaterThan(double minPrice, double maxPrice, int minStock, Pageable pageable);

}
