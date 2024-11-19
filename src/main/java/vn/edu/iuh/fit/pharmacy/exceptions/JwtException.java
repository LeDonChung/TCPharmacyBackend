package vn.edu.iuh.fit.pharmacy.exceptions;

public class JwtException extends RuntimeException{
    public JwtException(String message) {
        super(message);
    }
}
