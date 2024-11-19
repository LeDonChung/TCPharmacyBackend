package vn.edu.iuh.fit.pharmacy.jwt;

import io.jsonwebtoken.Claims;
import vn.edu.iuh.fit.pharmacy.service.security.UserDetailsCustom;

import java.security.Key;

public interface JwtService {

    Claims extractClaims(String token);

    Key getKey();

    String generateToken(UserDetailsCustom userDetailsCustom);

    boolean isValidToken(String token);
    String getCurrentUser();

    String getUsernameFromToken(String token);
}
