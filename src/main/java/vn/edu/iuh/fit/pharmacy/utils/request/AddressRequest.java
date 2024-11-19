package vn.edu.iuh.fit.pharmacy.utils.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.fit.pharmacy.POJOs.AddressType;
import vn.edu.iuh.fit.pharmacy.POJOs.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {
    private Long id;

    private String province;

    private String district;

    private String ward;

    private String street;

    private String fullName;

    private String phoneNumber;

    private AddressType addressType;

}
