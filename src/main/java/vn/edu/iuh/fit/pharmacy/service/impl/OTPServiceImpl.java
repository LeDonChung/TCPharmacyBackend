package vn.edu.iuh.fit.pharmacy.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import vn.edu.iuh.fit.pharmacy.exceptions.OTPException;
import vn.edu.iuh.fit.pharmacy.exceptions.UserException;
import vn.edu.iuh.fit.pharmacy.repositories.UserRepository;
import vn.edu.iuh.fit.pharmacy.service.OTPService;
import vn.edu.iuh.fit.pharmacy.utils.SystemConstraints;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OTPServiceImpl implements OTPService {
    private static final Integer EXPIRE_SECONDS = 60;
    private final LoadingCache<String, Integer> otpCache;
    @Autowired
    private UserRepository userRepository;

    public OTPServiceImpl(){
        super();
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_SECONDS, TimeUnit.SECONDS)
                .build(new CacheLoader<>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }
    //This method is used to push the opt number against Key. Rewrite the OTP if it exists
    //Using user id  as key
    @Override
    public int generateOTP(String key) throws UserException {
//        if(userRepository.findByUsername(key).isPresent()){
//            throw new UserException(SystemConstraints.PHONE_EXISTS);
//        }
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(key, otp);

        log.info("OTP generated for {}: {}", key, otp);
        return otp;
    }
    //This method is used to return the OPT number against Key->Key values is username
    @Override
    public int getOtp(String key){
        try{
            Integer value = otpCache.get(key);
            if(ObjectUtils.isEmpty(value)) {
                throw new OTPException(SystemConstraints.OTP_EXPIRED);
            }
            return value;
        }catch (Exception e){
            return 0;
        }
    }
    //This method is used to clear the OTP catched already
    @Override
    public void clearOTP(String key){
        otpCache.invalidate(key);
    }

}