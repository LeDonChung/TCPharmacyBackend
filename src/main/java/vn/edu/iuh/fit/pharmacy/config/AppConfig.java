package vn.edu.iuh.fit.pharmacy.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import vn.edu.iuh.fit.pharmacy.config.filter.CustomAuthenticationProvider;
import vn.edu.iuh.fit.pharmacy.config.filter.JwtTokenAuthenticationFilter;
import vn.edu.iuh.fit.pharmacy.config.filter.JwtUsernamePasswordAuthenticationFilter;
import vn.edu.iuh.fit.pharmacy.exceptions.CustomAccessDeniedHandler;
import vn.edu.iuh.fit.pharmacy.jwt.JwtConfig;
import vn.edu.iuh.fit.pharmacy.jwt.JwtService;
import vn.edu.iuh.fit.pharmacy.jwt.impl.JwtServiceImpl;
import vn.edu.iuh.fit.pharmacy.service.security.UserDetailsServiceCustom;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfig {


    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    JwtService jwtService;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceCustom();
    }

    @Autowired
    public void configGlobal(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());

        AuthenticationManager manager = builder.build();

        http
                .cors().configurationSource(request -> {
                    CorsConfiguration cf = new CorsConfiguration();
                    cf.setAllowedOrigins(
                            Arrays.asList("*")
                    );
                    cf.setAllowedMethods(Collections.singletonList("*"));
                    cf.setAllowCredentials(true);
                    cf.setAllowedHeaders(Collections.singletonList("*"));
                    cf.setExposedHeaders(Arrays.asList("Authorization"));
                    cf.setMaxAge(3600L);

                    return cf;
                })
                .and()
                .csrf().disable()
                .formLogin().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/register").permitAll()
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/otp/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/orders").hasAnyAuthority("ROLE_PATIENT", "ROLE_ADMIN")
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .authenticationManager(manager)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        ((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                )
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .addFilterBefore(new JwtUsernamePasswordAuthenticationFilter(manager, jwtConfig, jwtService), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig, jwtService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
