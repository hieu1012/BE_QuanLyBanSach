package iuh.fit.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Integer id;
    private String title;
    private String description;
    private Double price;
    private Double discountPrice;
    private Integer discount;
    private Integer stock;
    private List<String> imageNames; // JSON array của tên ảnh (public_id)
    private List<String> imageUrls; // URL đầy đủ của ảnh
    private Boolean isActive;

    // Hiển thị thông tin danh mục mà không cần lồng entity đầy đủ
    private Integer categoryId;
    private String categoryName;
}
