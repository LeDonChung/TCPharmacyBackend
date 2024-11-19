package vn.edu.iuh.fit.pharmacy.utils.response;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.fit.pharmacy.POJOs.Brand;
import vn.edu.iuh.fit.pharmacy.POJOs.Category;
import vn.edu.iuh.fit.pharmacy.POJOs.MedicineImage;
import vn.edu.iuh.fit.pharmacy.POJOs.Tag;

import java.util.Collection;
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MedicineResponse {
    private Long id;

    private Double price;

    private String init;

    private String specifications;

    private String desShort;

    private String name;

    private int star;

    private int reviews;

    private int discount;

    private int quantity;

    private String des;

    private int status;

    private Collection<MedicineImageResponse> medicineImages;

    private String sku;

    private String slug;

    private String primaryImage;

    private BrandResponse brand;

    private Collection<TagResponse> tags;

    private CategoryResponse category;
}
