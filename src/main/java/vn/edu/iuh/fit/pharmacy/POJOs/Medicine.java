package vn.edu.iuh.fit.pharmacy.POJOs;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "medicines")
@Entity
@Builder
public class Medicine {
    @Id
    @Column(name = "medicine_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;

    private String init;

    private String specifications;

    @Column(name = "des_short", columnDefinition = "TEXT")
    private String desShort;

    private String name;

    private int star;

    private int reviews;

    private int discount;

    private int quantity;

    @Column(name = "des", columnDefinition = "TEXT")
    private String des;

    private int status;

    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL)
    private Collection<MedicineImage> medicineImages;

    @Column(unique = true)
    private String sku;

    private String slug;

    private String primaryImage;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToMany
    @JoinTable(
            name = "medicines_tags",
            joinColumns = @JoinColumn(name = "medicine_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Collection<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
