package iuh.fit.controllers;

import iuh.fit.dtos.CreateCategoryDTO;
import iuh.fit.dtos.CategoryDTO;
import iuh.fit.services.CategoryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCategoryById(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", categoryService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> saveCategory(@RequestBody CreateCategoryDTO dto) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("data", categoryService.saveFromDTO(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable int id, @RequestBody CreateCategoryDTO dto) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", categoryService.updateFromDTO(id, dto));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = {"/{id}", ""})
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteCategory(
            @PathVariable(required = false) Integer id,
            @RequestParam(required = false) Integer deleteId,
            @RequestParam(required = false) List<Integer> ids) {
        Map<String, Object> response = new LinkedHashMap<>();
        
        if (ids != null && !ids.isEmpty()) {
            response.put("status", HttpStatus.OK.value());
            response.put("data", categoryService.deleteMultiple(ids));
        } else if (deleteId != null) {
            response.put("status", HttpStatus.OK.value());
            response.put("data", categoryService.delete(deleteId));
        } else if (id != null) {
            response.put("status", HttpStatus.OK.value());
            response.put("data", categoryService.delete(id));
        } else {
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("message", "ID hoặc IDs không được cung cấp");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getCategories() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", categoryService.findAll());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/hasPage")
    public ResponseEntity<Page<CategoryDTO>> getCategories(@ParameterObject Pageable pageable, @RequestParam(required = false) String keyword) {
        Page<CategoryDTO> response;
        if (keyword == null || keyword.isEmpty()) {
            response = categoryService.findAllWithPaging(pageable);
        } else {
            response = categoryService.searchWithPaging(keyword, pageable);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
