package vn.edu.iuh.fit.pharmacy.POJOs;

public enum OrderStatus {
    PENDING("PENDING"),     // Đơn hàng đang chờ xử lý
    PROCESSING("PROCESSING"),  // Đơn hàng đang được xử lý
    SHIPPED("SHIPPED"),     // Đơn hàng đã được vận chuyển
    DELIVERED("DELIVERED"),   // Đơn hàng đã được giao
    CANCELED("CANCELED"),    // Đơn hàng đã bị hủy
    RETURNED("RETURNED");     // Đơn hàng đã được trả lại

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}