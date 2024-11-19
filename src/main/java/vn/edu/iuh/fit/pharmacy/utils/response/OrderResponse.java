package vn.edu.iuh.fit.pharmacy.utils.response;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.fit.pharmacy.POJOs.Address;
import vn.edu.iuh.fit.pharmacy.POJOs.OrderDetail;
import vn.edu.iuh.fit.pharmacy.POJOs.OrderStatus;
import vn.edu.iuh.fit.pharmacy.POJOs.User;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;

    private Timestamp orderDate;

    private OrderStatus status;

    private int point;

    private boolean usePoint;

    private Double feeShip;

    private String note;

    private boolean exportInvoice;

    private UserResponse user;

    private Collection<OrderDetailResponse> orderDetails = new HashSet<>();

    private AddressResponse address;
}
