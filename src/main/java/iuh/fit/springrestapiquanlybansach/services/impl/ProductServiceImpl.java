package iuh.fit.springrestapiquanlybansach.services.impl;

import iuh.fit.springrestapiquanlybansach.dtos.ProductDTO;
import iuh.fit.springrestapiquanlybansach.entities.Product;
import iuh.fit.springrestapiquanlybansach.exceptions.ItemNotFoundException;
import iuh.fit.springrestapiquanlybansach.exceptions.ValidationException;
import iuh.fit.springrestapiquanlybansach.repositories.ProductRepository;
import iuh.fit.springrestapiquanlybansach.services.ProductService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    private ProductDTO toDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    private void validateProduct(Product product) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {
            Map<String, Object> errors = new LinkedHashMap<>();
            violations.forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
            throw new ValidationException("Validation failed for product", errors);
        }
    }

    @Override
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Product not found with id: " + id));
        return toDTO(product);
    }

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> findAllWithPaging(@ParameterObject Pageable pageable) {
        return productRepository.findAll(pageable).map(this::toDTO);
    }

    @Transactional
    @Override
    public ProductDTO save(Product product) {
        // compute discountPrice if possible (optional behavior)
        if (product.getPrice() != null && product.getDiscount() != null) {
            product.setDiscountPrice(product.getPrice() - product.getDiscount());
        }

        validateProduct(product);

        Product saved = productRepository.save(product);
        return toDTO(saved);
    }

    @Override
    public ProductDTO update(Long id, Product product) {
        // ensure exists
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Product not found with id: " + id));

        // validate incoming entity
        if (product.getPrice() != null && product.getDiscount() != null) {
            product.setDiscountPrice(product.getPrice() - product.getDiscount());
        }

        validateProduct(product);

        // preserve id and save (you can also copy fields manually if you want partial update)
        product.setId(existing.getId());
        Product saved = productRepository.save(product);
        return toDTO(saved);
    }

    @Override
    public boolean delete(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Product not found with id: " + id));
        productRepository.deleteById(id);
        return true;
    }

    @Override
    public List<ProductDTO> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        return productRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findByCategory(Long categoryId) {
        if (categoryId == null) return Collections.emptyList();
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}