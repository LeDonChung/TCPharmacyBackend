package vn.edu.iuh.fit.pharmacy.utils.response;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OTPResponse {
    private String phoneNumber;
    private boolean valid;
}
