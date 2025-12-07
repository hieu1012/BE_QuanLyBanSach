package iuh.fit.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.fit.dtos.CreateProductDTO;
import iuh.fit.dtos.ProductDTO;
import iuh.fit.entities.Product;
import iuh.fit.exceptions.ItemNotFoundException;
import iuh.fit.exceptions.ValidationException;
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
    private final ModelMapper modelMapper;
    private final FileUploadUtil fileUploadUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private ProductDTO convertToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    private Product convertToEntity(ProductDTO dto) {
        return modelMapper.map(dto, Product.class);
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
        product.setImageName(dto.getImageName());
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
        product.setImageName(dto.getImageName());
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

}
