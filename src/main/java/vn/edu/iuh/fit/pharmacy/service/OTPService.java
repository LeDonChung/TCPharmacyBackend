package vn.edu.iuh.fit.pharmacy.service;

import vn.edu.iuh.fit.pharmacy.exceptions.UserException;

public interface OTPService {
    public int generateOTP(String key) throws UserException;
    public int getOtp(String key);
    public void clearOTP(String key);
}
