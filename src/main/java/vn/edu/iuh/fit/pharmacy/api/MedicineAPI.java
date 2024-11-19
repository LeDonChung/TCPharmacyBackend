package vn.edu.iuh.fit.pharmacy.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MedicineAPI {
    String sku;
    String webName;
    String name;
    String shortName;
    String headingText;
    String primaryImage;
    List<String> secondaryImages;
    String specification;
    String shortDescription;
    String description;

    List<PriceAPI> prices;
}
