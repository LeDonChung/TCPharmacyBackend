package vn.edu.iuh.fit.pharmacy.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.pharmacy.exceptions.OTPException;
import vn.edu.iuh.fit.pharmacy.exceptions.UserException;
import vn.edu.iuh.fit.pharmacy.service.OTPService;
import vn.edu.iuh.fit.pharmacy.utils.SystemConstraints;
import vn.edu.iuh.fit.pharmacy.utils.response.BaseResponse;


@RestController
@RequestMapping("/otp")
@Slf4j
public class OTPResource {

    @Autowired
    public OTPService otpService;

    @GetMapping("/generate")
    public ResponseEntity<BaseResponse<Object>> generateOTP(@RequestParam("phoneNumber") String phoneNumber) throws UserException {

        log.info("Generate OTP for phone number: " + phoneNumber);
        otpService.generateOTP(phoneNumber);
        BaseResponse<Object> response = BaseResponse
                        .builder()
                        .code(String.valueOf(HttpStatus.OK.value())).success(true).data(SystemConstraints.OTP_GENERATED_SUCCESSFULLY)
                        .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }

    @GetMapping("/validate")
    public ResponseEntity<BaseResponse<Object>> validateOtp(@RequestParam("otp") int otp, @RequestParam("phoneNumber") String phoneNumber) throws OTPException {

        log.info("Validate OTP for phone number: " + phoneNumber);
        log.info("OTP: " + otp);
        int serverOpt = otpService.getOtp(phoneNumber);
        if(serverOpt == otp) {
            otpService.clearOTP(phoneNumber);
        } else {
            throw new OTPException(SystemConstraints.INVALID_OTP);
        }

        //Validate the Otp
        BaseResponse<Object> response = BaseResponse
                .builder()
                .code(String.valueOf(HttpStatus.OK.value())).success(true).data(SystemConstraints.VALID_OTP)
                .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }
}