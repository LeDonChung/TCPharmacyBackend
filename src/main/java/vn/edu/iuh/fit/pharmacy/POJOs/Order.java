package vn.edu.iuh.fit.pharmacy.POJOs;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders")
@Entity
@Builder
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    private Timestamp orderDate;

    @Column(name = "status")
    private OrderStatus status;

    private int point;

    @Column(name = "use_point")
    private boolean usePoint;

    @Column(name = "fee_ship")
    private Double feeShip;

    @Column(name = "export_invoice")
    private boolean exportInvoice;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    private String note;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<OrderDetail> orderDetails = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
}
