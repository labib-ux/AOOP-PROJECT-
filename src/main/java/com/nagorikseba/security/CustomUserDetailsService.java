package com.nagorikseba.security;

import com.nagorikseba.entity.User;
import com.nagorikseba.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrPhone) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(emailOrPhone)
                .or(() -> userRepository.findByPhone(emailOrPhone))
                .or(() -> userRepository.findByEmailOrPhone(emailOrPhone, emailOrPhone))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + emailOrPhone));

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new UsernameNotFoundException("User account is deactivated");
        }

        String principal = user.getEmail() != null ? user.getEmail() : user.getPhone();
        return org.springframework.security.core.userdetails.User.builder()
                .username(principal)
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                .build();
    }
}
