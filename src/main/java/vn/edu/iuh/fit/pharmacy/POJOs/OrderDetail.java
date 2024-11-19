package vn.edu.iuh.fit.pharmacy.POJOs;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders_details")
@Entity
@Builder
public class OrderDetail {

    @EmbeddedId
    private OrderDetailId id;

    private int quantity;

    private double price;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("medicineId")
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    private double discount;

    // Ghi đè equals() và hashCode() trong OrderDetail
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetail that = (OrderDetail) o;
        return Objects.equals(order, that.order) && Objects.equals(medicine, that.medicine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, medicine);
    }
}
