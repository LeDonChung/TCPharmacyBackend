package vn.edu.iuh.fit.pharmacy.utils.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailRequest {
    Long orderId;

    Long medicineId;

    private int quantity;

    private double price;

    private double discount;

}
