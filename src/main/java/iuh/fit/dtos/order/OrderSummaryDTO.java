package iuh.fit.dtos.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDTO {
    private Integer id;
    private String orderId;
    private LocalDateTime orderDate;
    private String status;
    private String paymentType;
    private Double totalPrice;

    private String firstProductTitle;
    private Integer totalItem;
    private String userEmail;
}
