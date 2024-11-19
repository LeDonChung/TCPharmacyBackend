package vn.edu.iuh.fit.pharmacy.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PriceAPI {
    String measureUnitCode;
    String measureUnitName;
    double price;
}
