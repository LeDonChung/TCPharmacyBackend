package vn.edu.iuh.fit.pharmacy.POJOs;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "points_histories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PointHistory {
    @Id
    @Column(name = "point_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int pointsChange;

    private int pointsBalance;

    @Enumerated(EnumType.STRING)
    private ChangeTypePoint changeType;

    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "point_id")
    private Point point;

    // Order
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
