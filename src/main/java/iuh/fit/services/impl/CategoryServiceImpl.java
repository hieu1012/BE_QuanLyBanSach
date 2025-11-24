package iuh.fit.services.impl;

import iuh.fit.repositories.CategoryRepository;
import iuh.fit.dtos.CategoryDTO;
import iuh.fit.entities.Category;
import iuh.fit.exceptions.ItemNotFoundException;
import iuh.fit.exceptions.ValidationException;
import iuh.fit.repositories.CategoryRepository;
import iuh.fit.services.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    private CategoryDTO convertToDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    private Category convertToEntity(CategoryDTO dto) {
        return modelMapper.map(dto, Category.class);
    }

    @Override
    public CategoryDTO findById(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Không tìm thấy danh mục có ID: " + id));
        return convertToDTO(category);
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryDTO> findAllWithPaging(@ParameterObject Pageable pageable) {
        return categoryRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional
    @Override
    public CategoryDTO save(Category category) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        if (!violations.isEmpty()) {
            Map<String, Object> errors = new LinkedHashMap<>();
            violations.forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
            throw new ValidationException("Có lỗi khi thêm danh mục mới!", errors);
        }

        categoryRepository.save(category);
        return convertToDTO(category);
    }

    @Transactional
    @Override
    public CategoryDTO update(int id, Category category) {
        this.findById(id); // ném lỗi nếu không tồn tại

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Category>> violations = validator.validate(category);

        if (!violations.isEmpty()) {
            Map<String, Object> errors = new LinkedHashMap<>();
            violations.forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
            throw new ValidationException("Có lỗi khi cập nhật danh mục!", errors);
        }

        category.setId(id);
        categoryRepository.save(category);
        return convertToDTO(category);
    }

    @Override
    public boolean delete(int id) {
        CategoryDTO dto = this.findById(id);
        categoryRepository.deleteById(dto.getId());
        return true;
    }

    @Override
    public List<CategoryDTO> search(String keyword) {
        return categoryRepository.search(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
