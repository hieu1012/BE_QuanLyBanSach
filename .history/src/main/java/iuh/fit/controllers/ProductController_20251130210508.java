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
@RequestMapping("/products")
public class ProductController {

    private final static Logger logger = LoggerFactory.getLogger(ProductController.class.getName());

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> saveProduct(@RequestBody Product product) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("data", productService.save(product));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable int id, @RequestBody Product product) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.update(id, product));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.delete(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getProducts() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.findAll());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/hasPage")
    public ResponseEntity<Page<ProductDTO>> getProducts(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minStock) {
        
        Page<ProductDTO> response;
        
        // Kết hợp nhiều điều kiện
        boolean hasKeyword = keyword != null && !keyword.isEmpty();
        boolean hasCategory = categoryId != null;
        boolean hasPrice = minPrice != null && maxPrice != null;
        boolean hasStock = minStock != null;
        
        // Nếu có tất cả: keyword + category + giá + stock
        if (hasKeyword && hasCategory && hasPrice && hasStock) {
            // Chưa có method kết hợp cả 4, dùng keyword + price trước
            response = productService.searchWithKeywordAndPrice(keyword, minPrice, maxPrice, pageable);
        }
        // Giá + Stock
        else if (hasPrice && hasStock && !hasKeyword && !hasCategory) {
            response = productService.findByPriceAndStockGreaterThan(minPrice, maxPrice, minStock, pageable);
        }
        // Category + Stock
        else if (hasCategory && hasStock && !hasKeyword && !hasPrice) {
            response = productService.findByCategoryAndStockGreaterThan(categoryId, minStock, pageable);
        }
        // Category + Giá
        else if (hasCategory && hasPrice && !hasKeyword && !hasStock) {
            response = productService.findByCategoryAndPriceBetween(categoryId, minPrice, maxPrice, pageable);
        }
        // Keyword + Giá
        else if (hasKeyword && hasPrice && !hasCategory && !hasStock) {
            response = productService.searchWithKeywordAndPrice(keyword, minPrice, maxPrice, pageable);
        }
        // Chỉ Stock
        else if (hasStock && !hasKeyword && !hasCategory && !hasPrice) {
            response = productService.findByStockGreaterThan(minStock, pageable);
        }
        // Chỉ Category
        else if (hasCategory && !hasKeyword && !hasPrice && !hasStock) {
            response = productService.findByCategoryIdWithPaging(categoryId, pageable);
        }
        // Chỉ Giá
        else if (hasPrice && !hasKeyword && !hasCategory && !hasStock) {
            response = productService.findByPriceBetween(minPrice, maxPrice, pageable);
        }
        // Chỉ Keyword
        else if (hasKeyword && !hasCategory && !hasPrice && !hasStock) {
            response = productService.searchWithPaging(keyword, pageable);
        }
        // Không có filter nào
        else {
            response = productService.findAllWithPaging(pageable);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.findByCategoryId(id));
        return ResponseEntity.ok(response);
    }
}
