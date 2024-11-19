package vn.edu.iuh.fit.pharmacy.utils.request;


import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.fit.pharmacy.POJOs.Address;
import vn.edu.iuh.fit.pharmacy.POJOs.OrderDetail;
import vn.edu.iuh.fit.pharmacy.POJOs.OrderStatus;
import vn.edu.iuh.fit.pharmacy.POJOs.User;
import vn.edu.iuh.fit.pharmacy.utils.response.UserResponse;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateOrderRequest {

    private int point;

    private boolean usePoint;

    private Double feeShip;

    private boolean exportInvoice;

    private Long user;

    private String note;

    private Collection<OrderDetailRequest> orderDetails = new HashSet<>();

    private AddressRequest address;
}

