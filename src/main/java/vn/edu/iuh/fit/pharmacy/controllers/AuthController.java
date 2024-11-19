package vn.edu.iuh.fit.pharmacy.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import vn.edu.iuh.fit.pharmacy.POJOs.User;
import vn.edu.iuh.fit.pharmacy.exceptions.OTPException;
import vn.edu.iuh.fit.pharmacy.jwt.JwtService;
import vn.edu.iuh.fit.pharmacy.service.OTPService;
import vn.edu.iuh.fit.pharmacy.service.UserService;
import vn.edu.iuh.fit.pharmacy.utils.SystemConstraints;
import vn.edu.iuh.fit.pharmacy.utils.request.UserRegisterRequest;
import vn.edu.iuh.fit.pharmacy.utils.response.BaseResponse;
import vn.edu.iuh.fit.pharmacy.utils.response.UserResponse;

import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private OTPService otpService;

    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<Object>> register(@RequestBody UserRegisterRequest request) {
        log.info("Register request: " + request);

        // verify OTP
        int serverOpt = otpService.getOtp(request.getPhoneNumber());
        if(serverOpt == request.getOtp() && serverOpt != 0){
            otpService.clearOTP(request.getPhoneNumber());
            // Register User
            UserResponse response = userService.register(request);
            response.setPassword("default");

            User user = new User();
            user.setPhoneNumber(request.getPhoneNumber());
            user.setPassword(request.getPassword());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);


            ResponseEntity<BaseResponse<String>> result = restTemplate.exchange(
                    "http://localhost:9090/tc/api/auth/login",
                    HttpMethod.POST,
                    new HttpEntity<>(user, headers),
                    new ParameterizedTypeReference<>() {
                    }
            );

            log.info("Response from TC: " + result.getBody());
//            BaseResponse<Object> result = restTemplate.postForEntity("", response, BaseResponse.class).getBody();

            return new ResponseEntity<>(
                    BaseResponse
                            .builder()
                            .code(String.valueOf(HttpStatus.OK.value()))
                            .success(true)
                            .data(Objects.requireNonNull(result.getBody()).getData())
                            .build(),
                    HttpStatus.OK
            );
        } else {
            throw new OTPException(SystemConstraints.INVALID_OTP);
        }

    }

    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserResponse>> validationToken(@RequestHeader("Authorization") String token) {
        log.info("Token: " + token);
        UserResponse user = userService.getMe(token.substring(7));
        return new ResponseEntity<>(
                BaseResponse
                        .<UserResponse>builder()
                        .code(String.valueOf(HttpStatus.OK.value()))
                        .success(true)
                        .data(user)
                        .build(),
                HttpStatus.OK
        );
    }
}
