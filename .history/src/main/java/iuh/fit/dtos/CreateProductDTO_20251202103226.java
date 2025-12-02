package iuh.fit.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String imageName;
    private Boolean isActive;
    private Integer categoryId;
}
