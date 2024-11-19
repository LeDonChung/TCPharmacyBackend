package vn.edu.iuh.fit.pharmacy.service.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import vn.edu.iuh.fit.pharmacy.POJOs.User;
import vn.edu.iuh.fit.pharmacy.exceptions.UserException;
import vn.edu.iuh.fit.pharmacy.repositories.UserRepository;
import vn.edu.iuh.fit.pharmacy.utils.SystemConstraints;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsServiceCustom implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetailsCustom userDetailsCustom = getUserDetails(username);

        if (ObjectUtils.isEmpty(userDetailsCustom)) {
            try {
                throw new UserException(SystemConstraints.INVALID_USERNAME_OR_PASSWORD);
            } catch (UserException e) {
                throw new RuntimeException(e);
            }
        }

        return userDetailsCustom;
    }

    public UserDetailsCustom getUserDetails(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(SystemConstraints.INVALID_USERNAME_OR_PASSWORD);
        }
        User userDetails = user.get();
        return new UserDetailsCustom(
                userDetails.getPhoneNumber(),
                userDetails.getPassword(),
                userDetails.getRoles().stream().map(
                        r -> new SimpleGrantedAuthority(r.getCode())
                ).collect(Collectors.toList())
        );
    }
}
