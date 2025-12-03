package iuh.fit.dtos.order;

import iuh.fit.dtos.user.UserDTO;
import iuh.fit.entities.enums.OrderStatus;
import iuh.fit.entities.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private PaymentType paymentType;
    private Double totalPrice;

    // Thông tin người đặt hàng
    private UserDTO user;

    // Thông tin địa chỉ giao hàng
    private OrderAddressDTO orderAddress;

    private List<OrderItemDTO> items;
}
