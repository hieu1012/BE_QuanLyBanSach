package iuh.fit.controllers;

import iuh.fit.dtos.CreateProductDTO;
import iuh.fit.dtos.ProductDTO;
import iuh.fit.services.ProductService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

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
    public ResponseEntity<Map<String, Object>> saveProduct(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam(required = false) Double discountPrice,
            @RequestParam(required = false) Integer discount,
            @RequestParam Integer stock,
            @RequestParam(required = false, defaultValue = "true") Boolean isActive,
            @RequestParam Integer categoryId,
            @RequestParam(required = false) MultipartFile[] images) throws IOException {
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("data", productService.saveProductWithImages(title, description, price, discountPrice, 
                discount, stock, isActive, categoryId, images));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable int id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Double discountPrice,
            @RequestParam(required = false) Integer discount,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) MultipartFile[] images,
            @RequestParam(required = false) Boolean keepExistingImages) throws IOException {
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.updateProductWithImages(id, title, description, price, 
                discountPrice, discount, stock, isActive, categoryId, images, keepExistingImages));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = {"/{id}", ""})
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteProduct(
            @PathVariable(required = false) Integer id,
            @RequestParam(required = false) Integer deleteId,
            @RequestParam(required = false) List<Integer> ids) {
        Map<String, Object> response = new LinkedHashMap<>();
        
        // Nếu có ids (query param) → xóa nhiều (ưu tiên nếu có)
        if (ids != null && !ids.isEmpty()) {
            boolean success = productService.deleteMultiple(ids);
            response.put("status", success ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("data", success ? "Xóa " + ids.size() + " sản phẩm thành công" : "Lỗi khi xóa sản phẩm");
            return ResponseEntity.status(success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        
        // Nếu có id (path param hoặc query param) → xóa 1
        int deleteIdFinal = id != null ? id : (deleteId != null ? deleteId : -1);
        if (deleteIdFinal > 0) {
            response.put("status", HttpStatus.OK.value());
            response.put("data", productService.delete(deleteIdFinal));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        
        // Nếu không có ID nào
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("data", "Vui lòng cung cấp id hoặc danh sách ids");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/{id}/upload-images")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> uploadProductImages(
            @PathVariable int id,
            @RequestParam(required = false, defaultValue = "false") Boolean replaceExisting,
            @RequestParam MultipartFile[] images) throws IOException {
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", productService.uploadProductImages(id, images, replaceExisting));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}/images")
    @PreAuthorize("hasAnyRole('MASTER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteProductImages(
            @PathVariable int id,
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) List<String> fileNames) {
        
        Map<String, Object> response = new LinkedHashMap<>();
        
        // Nếu có fileNames (query param) → xóa nhiều ảnh
        if (fileNames != null && !fileNames.isEmpty()) {
            ProductDTO result = productService.deleteProductImages(id, fileNames);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Xóa " + fileNames.size() + " ảnh thành công");
            response.put("data", result);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        
        // Nếu có fileName (query param) → xóa 1 ảnh
        if (fileName != null && !fileName.isEmpty()) {
            ProductDTO result = productService.deleteProductImages(id, List.of(fileName));
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Xóa ảnh thành công");
            response.put("data", result);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        
        // Không có ảnh nào để xóa
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Vui lòng cung cấp fileName hoặc fileNames");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
