package iuh.fit.dtos;

import iuh.fit.dtos.order.OrderAddressDTO;
import iuh.fit.entities.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {

    // Thông tin địa chỉ giao hàng mới (hoặc từ danh sách địa chỉ đã lưu)
    @NotNull(message = "Thông tin địa chỉ không được để trống")
    private OrderAddressDTO orderAddress;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    private PaymentType paymentType;
}