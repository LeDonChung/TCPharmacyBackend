package vn.edu.iuh.fit.pharmacy.POJOs;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "points")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Point {
    @Id
    @Column(name = "point_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int currentPoint;

    private Timestamp updateAt;

    private Rank rank;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "point", cascade = CascadeType.ALL)
    private Collection<PointHistory> pointHistories;
}
