package vn.edu.iuh.fit.pharmacy.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import vn.edu.iuh.fit.pharmacy.POJOs.User;
import vn.edu.iuh.fit.pharmacy.exceptions.ErrorDetail;
import vn.edu.iuh.fit.pharmacy.jwt.JwtConfig;
import vn.edu.iuh.fit.pharmacy.jwt.JwtService;
import vn.edu.iuh.fit.pharmacy.service.security.UserDetailsCustom;
import vn.edu.iuh.fit.pharmacy.utils.HelperUtils;
import vn.edu.iuh.fit.pharmacy.utils.SystemConstraints;
import vn.edu.iuh.fit.pharmacy.utils.response.BaseResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
public class JwtUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    @Autowired
    private JwtService jwtService;

    private final ObjectMapper objectMapper;

    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager manager,
                                                   JwtConfig jwtConfig,
                                                   JwtService jwtService){
        super(new AntPathRequestMatcher(jwtConfig.getUrl(), "POST"));
        setAuthenticationManager(manager);
        this.objectMapper = new ObjectMapper();
        this.jwtService = jwtService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        log.info("Start attempt to authentication");
        User loginRequest = objectMapper.readValue(request.getInputStream(), User.class);
        log.info("End attempt to authentication");

        return getAuthenticationManager()
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.getPhoneNumber(),
                            loginRequest.getPassword(),
                            Collections.emptyList()));


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        UserDetailsCustom userDetailsCustom = (UserDetailsCustom) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(userDetailsCustom);
        BaseResponse<Object> responseObject = BaseResponse
                .builder()
                .success(true)
                .code(String.valueOf(HttpStatus.OK.value()))
                .data(accessToken)
                .build();
        String json = HelperUtils.JSON_WRITER.writeValueAsString(responseObject);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(json);
        log.info("End success authentication: {}", accessToken);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        ErrorDetail errorDetail = ErrorDetail
                .builder()
                .error(SystemConstraints.INVALID_USERNAME_OR_PASSWORD)
                .message(SystemConstraints.INVALID_USERNAME_OR_PASSWORD)
                .timeStamp(null)
                .build();
        String json = HelperUtils.JSON_WRITER.writeValueAsString(errorDetail);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(json);
    }


}
