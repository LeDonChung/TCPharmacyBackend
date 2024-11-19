package vn.edu.iuh.fit.pharmacy.config.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.edu.iuh.fit.pharmacy.exceptions.ErrorDetail;
import vn.edu.iuh.fit.pharmacy.jwt.JwtConfig;
import vn.edu.iuh.fit.pharmacy.jwt.JwtService;
import vn.edu.iuh.fit.pharmacy.utils.HelperUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    private final JwtService jwtService;

    private Claims claims = null;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader(jwtConfig.getHeader());

        log.info("Start do filter once per request, {}", request.getRequestURI());

        if (!ObjectUtils.isEmpty(accessToken) && accessToken.startsWith(jwtConfig.getPrefix() + " ")) {
            accessToken = accessToken.substring((jwtConfig.getPrefix() + " ").length());

            try {
                if (jwtService.isValidToken(accessToken)) {
                    this.claims = jwtService.extractClaims(accessToken);

                    String username = claims.getSubject();

                    List<String> authorities = claims.get("authorities", List.class);

                    if (!ObjectUtils.isEmpty(username)) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        username,
                                        null,
                                        authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception e) {
                log.error("Error on filter once per request, path {}, error: {}", request.getRequestURI(), e.getMessage());

                ErrorDetail errorDetail = ErrorDetail
                        .builder()
                        .error(e.getLocalizedMessage())
                        .message(e.getLocalizedMessage())
                        .timeStamp(null)
                        .build();

                String json = HelperUtils.JSON_WRITER.writeValueAsString(errorDetail);

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(json);
                return;
            }
        }
        log.info("end do filter: {}", request.getRequestURI());
        filterChain.doFilter(request, response);
    }

}
