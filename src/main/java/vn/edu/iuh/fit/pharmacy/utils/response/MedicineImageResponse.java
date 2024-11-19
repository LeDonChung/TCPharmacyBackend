package vn.edu.iuh.fit.pharmacy.utils.response;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.fit.pharmacy.POJOs.Medicine;
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MedicineImageResponse {
    private Long id;

    private String url;
}
