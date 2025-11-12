package iuh.fit.dtos.order;

import iuh.fit.dtos.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Integer id;
    private ProductDTO product;
    private Integer quantity;
    private Double price;
}
