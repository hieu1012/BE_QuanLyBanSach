package iuh.fit.services;

import iuh.fit.dtos.CreateProductDTO;
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

    ProductDTO saveFromDTO(CreateProductDTO dto);

    ProductDTO update(int id, Product product);

    ProductDTO updateFromDTO(int id, CreateProductDTO dto);

    boolean delete(int id);

    /**
     * Xóa nhiều sản phẩm theo danh sách ID
     */
    boolean deleteMultiple(List<Integer> ids);

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

    /**
     * Tạo sản phẩm mới với upload hình ảnh
     */
    ProductDTO saveProductWithImages(String title, String description, Double price, Double discountPrice,
                                     Integer discount, Integer stock, Boolean isActive, Integer categoryId,
                                     org.springframework.web.multipart.MultipartFile[] images) throws java.io.IOException;

    /**
     * Cập nhật sản phẩm với upload hình ảnh
     */
    ProductDTO updateProductWithImages(int id, String title, String description, Double price, Double discountPrice,
                                       Integer discount, Integer stock, Boolean isActive, Integer categoryId,
                                       org.springframework.web.multipart.MultipartFile[] images, Boolean keepExistingImages) throws java.io.IOException;

    /**
     * Upload ảnh cho sản phẩm
     */
    ProductDTO uploadProductImages(int id, org.springframework.web.multipart.MultipartFile[] images, Boolean replaceExisting) throws java.io.IOException;

    /**
     * Xóa ảnh sản phẩm
     */
    ProductDTO deleteProductImages(int id, List<String> fileNames);

}
