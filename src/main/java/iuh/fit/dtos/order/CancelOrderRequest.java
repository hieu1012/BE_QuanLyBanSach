package iuh.fit.dtos.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelOrderRequest {
    @NotBlank(message = "Vui lòng cung cấp lý do hủy đơn hàng")
    private String reason;
}
