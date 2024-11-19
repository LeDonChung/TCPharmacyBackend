package vn.edu.iuh.fit.pharmacy.jwt;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * url web http://localhost:4200
 */
@CrossOrigin(origins = "http://localhost:8081")
@Data
@Component
public class JwtConfig {
    @Value("${jwt.url:/auth/login}")
    private String url;

    @Value("${jwt.header:Authorization}")
    private String header;

    @Value("${jwt.prefix:Bearer}")
    private String prefix;

    @Value("${jwt.expiration:#{60*60}}")
    private int expiration;

    @Value("${jwt.secret:3979244226452948404D6251655468576D5A7134743777217A25432A462D4A61}")
    private String secret;
}
