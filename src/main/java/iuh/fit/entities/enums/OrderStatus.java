package iuh.fit.entities.enums;

public enum OrderStatus {
    PENDING,        // Đang chờ xử lý
    PROCESSING,     // Đang chuẩn bị hàng
    SHIPPED,        // Đã gửi
    DELIVERED,      // Đã giao
    CANCEL_REQUESTED, // Người dùng yêu cầu hủy, chờ Admin duyệt
    CANCELLED       //Đã Hủy
}
