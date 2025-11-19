package iuh.fit.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String imageName;
    private Boolean isActive;

    // Hiển thị thông tin danh mục mà không cần lồng entity đầy đủ
    private Integer categoryId;
    private String categoryName;
}
