package iuh.fit.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDTO {
    private String title;
    private String description;
    private Double price;
    private Double discountPrice;
    private Integer discount;
    private Integer stock;
    private List<String> imageNames; // JSON array của tên ảnh
    private Boolean isActive;
    private Integer categoryId;
}
