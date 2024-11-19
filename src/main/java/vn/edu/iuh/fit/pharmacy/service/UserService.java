package vn.edu.iuh.fit.pharmacy.service;

import vn.edu.iuh.fit.pharmacy.utils.request.UserRegisterRequest;
import vn.edu.iuh.fit.pharmacy.utils.response.UserResponse;

public interface UserService {
    public UserResponse register(UserRegisterRequest request);

    UserResponse getMe(String token);
}
