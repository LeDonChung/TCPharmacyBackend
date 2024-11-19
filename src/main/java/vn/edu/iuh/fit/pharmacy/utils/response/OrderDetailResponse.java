package vn.edu.iuh.fit.pharmacy.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long orderId;

    private Long medicineId;

    private int quantity;

    private double price;
    private double discount;

}
