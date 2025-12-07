package iuh.fit.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.dtos.CreateProductDTO;
import iuh.fit.dtos.ProductDTO;
import iuh.fit.entities.Product;
import iuh.fit.exceptions.ItemNotFoundException;
import iuh.fit.exceptions.ValidationException;
import iuh.fit.repositories.CartItemRepository;
import iuh.fit.repositories.ProductRepository;
import iuh.fit.repositories.CategoryRepository;
import iuh.fit.services.ProductService;
import iuh.fit.utils.FileUploadUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;
    private final FileUploadUtil fileUploadUtil;
    private final ObjectMapper objectMapper;

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = modelMapper.map(product, ProductDTO.class);
        // Chuyển đổi imageNames từ JSON string thành List
        dto.setImageNames(convertJsonStringToList(product.getImageNames()));
        return dto;
    }

    @Override
    public ProductDTO findById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy sản phẩm có ID: " + id));
        return convertToDTO(product);
    }

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> findAllWithPaging(@ParameterObject Pageable pageable) {
        return productRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional
    @Override
    public ProductDTO save(Product product) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {
            Map<String, Object> errors = new LinkedHashMap<>();
            violations.forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
            throw new ValidationException("Có lỗi khi thêm sản phẩm mới!", errors);
        }

        productRepository.save(product);
        return convertToDTO(product);
    }

    @Transactional
    @Override
    public ProductDTO update(int id, Product product) {
        this.findById(id); // ném lỗi nếu không tồn tại

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {
            Map<String, Object> errors = new LinkedHashMap<>();
            violations.forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
            throw new ValidationException("Có lỗi khi cập nhật sản phẩm!", errors);
        }

        product.setId(id);
        productRepository.save(product);
        return convertToDTO(product);
    }

    @Override
    public boolean delete(int id) {
        ProductDTO dto = this.findById(id);
        productRepository.deleteById(dto.getId());
        return true;
    }

    @Override
    public boolean deleteMultiple(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        try {
            // Xóa tất cả cart_items tham chiếu tới các product này trước
            for (Integer productId : ids) {
                cartItemRepository.deleteByProductId(productId);
            }
            // Sau đó xóa các product
            productRepository.deleteAllById(ids);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<ProductDTO> search(String keyword) {
        return productRepository.search(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> searchWithPaging(String keyword, @ParameterObject Pageable pageable) {
        return productRepository.searchWithPaging(keyword, pageable).map(this::convertToDTO);
    }

    @Override
    public List<ProductDTO> findByCategoryId(int categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable) {
        return productRepository.findByPriceBetween(minPrice, maxPrice, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> findByCategoryIdWithPaging(int categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdWithPaging(categoryId, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> findByCategoryAndPriceBetween(int categoryId, double minPrice, double maxPrice, Pageable pageable) {
        return productRepository.findByCategoryAndPriceBetween(categoryId, minPrice, maxPrice, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> searchWithKeywordAndPrice(String keyword, double minPrice, double maxPrice, Pageable pageable) {
        return productRepository.searchWithKeywordAndPrice(keyword, minPrice, maxPrice, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> findByStockGreaterThan(int minStock, Pageable pageable) {
        return productRepository.findByStockGreaterThan(minStock, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> findByStockLessThanOrEqual(int maxStock, Pageable pageable) {
        return productRepository.findByStockLessThanOrEqual(maxStock, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> findOutOfStock(Pageable pageable) {
        return productRepository.findOutOfStock(pageable).map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> findByCategoryAndStockGreaterThan(int categoryId, int minStock, Pageable pageable) {
        return productRepository.findByCategoryAndStockGreaterThan(categoryId, minStock, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<ProductDTO> findByPriceAndStockGreaterThan(double minPrice, double maxPrice, int minStock, Pageable pageable) {
        return productRepository.findByPriceAndStockGreaterThan(minPrice, maxPrice, minStock, pageable).map(this::convertToDTO);
    }

    @Transactional
    @Override
    public ProductDTO saveFromDTO(CreateProductDTO dto) {
        Product product = new Product();
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscountPrice(dto.getDiscountPrice());
        product.setDiscount(dto.getDiscount());
        product.setStock(dto.getStock());
        product.setImageNames(convertListToJsonString(dto.getImageNames()));
        product.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        
        // Tìm category
        var category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy danh mục có ID: " + dto.getCategoryId()));
        product.setCategory(category);
        
        // Validate
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {
            Map<String, Object> errors = new LinkedHashMap<>();
            violations.forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
            throw new ValidationException("Có lỗi khi thêm sản phẩm mới!", errors);
        }

        productRepository.save(product);
        return convertToDTO(product);
    }

    @Transactional
    @Override
    public ProductDTO updateFromDTO(int id, CreateProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy sản phẩm có ID: " + id));
        
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscountPrice(dto.getDiscountPrice());
        product.setDiscount(dto.getDiscount());
        product.setStock(dto.getStock());
        product.setImageNames(convertListToJsonString(dto.getImageNames()));
        product.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        
        // Tìm category
        var category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy danh mục có ID: " + dto.getCategoryId()));
        product.setCategory(category);
        
        // Validate
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {
            Map<String, Object> errors = new LinkedHashMap<>();
            violations.forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
            throw new ValidationException("Có lỗi khi cập nhật sản phẩm!", errors);
        }

        productRepository.save(product);
        return convertToDTO(product);
    }

    @Transactional
    @Override
    public ProductDTO saveProductWithImages(String title, String description, Double price, Double discountPrice,
                                           Integer discount, Integer stock, Boolean isActive, Integer categoryId,
                                           MultipartFile[] images) throws IOException {
        // Upload ảnh nếu có
        List<String> uploadedImages = new ArrayList<>();
        if (images != null && images.length > 0) {
            uploadedImages = fileUploadUtil.uploadMultipleFiles(images);
        }

        // Tạo sản phẩm
        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setDiscountPrice(discountPrice);
        product.setDiscount(discount);
        product.setStock(stock);
        product.setImageNames(convertListToJsonString(uploadedImages));
        product.setIsActive(isActive != null ? isActive : true);

        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy danh mục có ID: " + categoryId));
        product.setCategory(category);

        // Validate
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {
            Map<String, Object> errors = new LinkedHashMap<>();
            violations.forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
            throw new ValidationException("Có lỗi khi thêm sản phẩm mới!", errors);
        }

        productRepository.save(product);
        return convertToDTO(product);
    }

    @Transactional
    @Override
    public ProductDTO updateProductWithImages(int id, String title, String description, Double price, Double discountPrice,
                                             Integer discount, Integer stock, Boolean isActive, Integer categoryId,
                                             MultipartFile[] images, Boolean keepExistingImages) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy sản phẩm có ID: " + id));

        // Cập nhật thông tin cơ bản
        if (title != null) product.setTitle(title);
        if (description != null) product.setDescription(description);
        if (price != null) product.setPrice(price);
        if (discountPrice != null) product.setDiscountPrice(discountPrice);
        if (discount != null) product.setDiscount(discount);
        if (stock != null) product.setStock(stock);
        if (isActive != null) product.setIsActive(isActive);

        if (categoryId != null) {
            var category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy danh mục có ID: " + categoryId));
            product.setCategory(category);
        }

        // Xử lý ảnh
        if (images != null && images.length > 0) {
            List<String> newImages = fileUploadUtil.uploadMultipleFiles(images);
            
            if (keepExistingImages != null && keepExistingImages) {
                // Giữ ảnh cũ, thêm ảnh mới
                List<String> existingImages = convertJsonStringToList(product.getImageNames());
                existingImages.addAll(newImages);
                product.setImageNames(convertListToJsonString(existingImages));
            } else {
                // Xóa ảnh cũ, thêm ảnh mới
                List<String> oldImages = convertJsonStringToList(product.getImageNames());
                fileUploadUtil.deleteMultipleFiles(oldImages);
                product.setImageNames(convertListToJsonString(newImages));
            }
        }

        // Validate
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {
            Map<String, Object> errors = new LinkedHashMap<>();
            violations.forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
            throw new ValidationException("Có lỗi khi cập nhật sản phẩm!", errors);
        }

        productRepository.save(product);
        return convertToDTO(product);
    }

    @Transactional
    @Override
    public ProductDTO uploadProductImages(int id, MultipartFile[] images, Boolean replaceExisting) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy sản phẩm có ID: " + id));

        List<String> newImages = fileUploadUtil.uploadMultipleFiles(images);

        if (replaceExisting != null && replaceExisting) {
            // Xóa ảnh cũ
            List<String> oldImages = convertJsonStringToList(product.getImageNames());
            fileUploadUtil.deleteMultipleFiles(oldImages);
            product.setImageNames(convertListToJsonString(newImages));
        } else {
            // Thêm ảnh mới vào ảnh cũ
            List<String> existingImages = convertJsonStringToList(product.getImageNames());
            existingImages.addAll(newImages);
            product.setImageNames(convertListToJsonString(existingImages));
        }

        productRepository.save(product);
        return convertToDTO(product);
    }

    /**
     * Convert List<String> to JSON string
     */
    private String convertListToJsonString(List<String> imageNames) {
        if (imageNames == null || imageNames.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(imageNames);
        } catch (Exception e) {
            return "[]";
        }
    }

    /**
     * Convert JSON string to List<String>
     */
    private List<String> convertJsonStringToList(String json) {
        if (json == null || json.isEmpty() || "[]".equals(json)) {
            return new ArrayList<>();
        }
        try {
            return Arrays.asList(objectMapper.readValue(json, String[].class));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public ProductDTO deleteProductImages(int id, List<String> fileNames) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy sản phẩm có ID: " + id));
        
        if (fileNames == null || fileNames.isEmpty()) {
            return convertToDTO(product);
        }
        
        // Lấy danh sách ảnh hiện tại
        List<String> currentImages = convertJsonStringToList(product.getImageNames());
        
        // Xóa các file từ hệ thống và danh sách
        for (String fileName : fileNames) {
            try {
                fileUploadUtil.deleteFile(fileName);
                currentImages.remove(fileName);
            } catch (Exception e) {
                // Log nhưng tiếp tục xóa file khác
            }
        }
        
        // Cập nhật danh sách ảnh
        product.setImageNames(convertListToJsonString(currentImages));
        productRepository.save(product);
        
        return convertToDTO(product);
    }

}