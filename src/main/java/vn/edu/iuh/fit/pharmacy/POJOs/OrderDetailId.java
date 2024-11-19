package vn.edu.iuh.fit.pharmacy.POJOs;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
@Builder
public class OrderDetailId implements Serializable {
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "medicine_id", nullable = false)
    private Long medicineId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderDetailId that = (OrderDetailId) o;

        if (!orderId.equals(that.orderId)) return false;
        return medicineId.equals(that.medicineId);
    }

    @Override
    public int hashCode() {
        int result = orderId.hashCode();
        result = 31 * result + medicineId.hashCode();
        return result;
    }
}
