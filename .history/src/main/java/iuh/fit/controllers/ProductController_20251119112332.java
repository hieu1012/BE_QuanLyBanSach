package iuh.fit.controllers;

import iuh.fit.dtos.ProductDTO;
import iuh.fit.entities.Product;
import iuh.fit.services.ProductService;
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
public class ProductController {

    private final static Logger logger = LoggerFactory.getLogger(ProductController.class.getName());

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/products")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> saveProduct(@RequestBody Product product) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("data", productService.save(product));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/products/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable int id, @RequestBody Product product) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.update(id, product));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.delete(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getProducts(@RequestParam(required = false) String keyword) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());

        if (keyword == null || keyword.isEmpty()) {
            response.put("data", productService.findAll());
        } else {
            response.put("data", productService.search(keyword));
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/productsHasPage")
    public ResponseEntity<Page<ProductDTO>> getProducts(@ParameterObject Pageable pageable) {
        Page<ProductDTO> response = productService.findAllWithPaging(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/products/category/{id}")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.findByCategoryId(id));
        return ResponseEntity.ok(response);
    }
}
