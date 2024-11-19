package vn.edu.iuh.fit.pharmacy.utils.response;

import lombok.*;
import vn.edu.iuh.fit.pharmacy.POJOs.Brand;

import java.io.Serializable;
import java.util.Collection;

/**
 * DTO for {@link Brand}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BrandResponse implements Serializable {
    Long id;
    String title;
    String image;
    String imageProduct;
}