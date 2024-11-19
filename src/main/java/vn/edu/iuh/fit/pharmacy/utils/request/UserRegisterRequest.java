package vn.edu.iuh.fit.pharmacy.utils.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserRegisterRequest {
    public String phoneNumber;
    public String password;
    public int otp;
}
