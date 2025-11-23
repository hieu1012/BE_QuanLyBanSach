package iuh.fit.controllers;

import iuh.fit.dtos.CategoryDTO;
import iuh.fit.entities.Category;
import iuh.fit.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final static Logger logger = LoggerFactory.getLogger(CategoryController.class.getName());

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories/{id}")
    public ResponseEntity<Map<String, Object>> getCategoryById(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", categoryService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/categories")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> saveCategory(@RequestBody Category category) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("data", categoryService.save(category));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/categories/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable int id, @RequestBody Category category) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", categoryService.update(id, category));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", categoryService.delete(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories(@RequestParam(required = false) String keyword) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());

        if (keyword == null || keyword.isEmpty()) {
            response.put("data", categoryService.findAll());
        } else {
            response.put("data", categoryService.search(keyword));
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/categoriesHasPage")
    public ResponseEntity<Page<CategoryDTO>> getCategories(@ParameterObject Pageable pageable) {
        Page<CategoryDTO> response = categoryService.findAllWithPaging(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
