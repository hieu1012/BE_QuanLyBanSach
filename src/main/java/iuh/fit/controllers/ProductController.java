package iuh.fit.controllers;

import iuh.fit.dtos.ProductDTO;
import iuh.fit.entities.Product;
import iuh.fit.services.ProductService;
import org.springdoc.core.annotations.ParameterObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class ProductController {

    private final static Logger logger = LoggerFactory.getLogger(ProductController.class.getName());

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // create product (receives Product entity like your EmployeeController)
    @PostMapping("/products")
    public ResponseEntity<Map<String, Object>> saveProduct(@RequestBody Product product) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.save(product));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.update(id, product));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.delete(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * GET /products?keyword=xxx
     * If keyword omitted returns all products; otherwise returns search results.
     */
    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getProducts(@RequestParam(required = false) String keyword,
                                                           @RequestParam(required = false) Long categoryId) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());

        // if categoryId is present, prefer category filter
        if (categoryId != null) {
            response.put("data", productService.findByCategory(categoryId));
        } else if (keyword == null || keyword.isEmpty()) {
            response.put("data", productService.findAll());
        } else {
            response.put("data", productService.search(keyword));
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // GET with paging: /productsHasPage?page=0&size=10&sort=title,asc
    @GetMapping("/productsHasPage")
    public ResponseEntity<Page<ProductDTO>> getProductsWithPage(@ParameterObject Pageable pageable) {
        Page<ProductDTO> response = productService.findAllWithPaging(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}