package iuh.fit.services.impl;

import iuh.fit.dtos.ProductDTO;
import iuh.fit.entities.Product;
import iuh.fit.exceptions.ItemNotFoundException;
import iuh.fit.exceptions.ValidationException;
import iuh.fit.repositories.ProductRepository;
import iuh.fit.services.ProductService;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

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
    public List<ProductDTO> findByCategoryId(int categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
