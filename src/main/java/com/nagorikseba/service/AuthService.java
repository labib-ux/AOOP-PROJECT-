package com.nagorikseba.service;

import com.nagorikseba.dto.AuthRequest;
import com.nagorikseba.dto.AuthResponse;
import com.nagorikseba.dto.RefreshTokenRequest;
import com.nagorikseba.dto.UserRegistrationDTO;
import com.nagorikseba.entity.User;
import com.nagorikseba.entity.Ward;
import com.nagorikseba.enums.UserRole;
import com.nagorikseba.exception.InvalidCredentialsException;
import com.nagorikseba.repository.UserRepository;
import com.nagorikseba.repository.WardRepository;
import com.nagorikseba.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final WardRepository wardRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       WardRepository wardRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.wardRepository = wardRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponse register(UserRegistrationDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new IllegalArgumentException("Phone number is already registered");
        }

        Ward ward = null;
        if (dto.getWardId() != null) {
            ward = wardRepository.findById(dto.getWardId()).orElse(null);
        }

        User user = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(UserRole.CITIZEN)
                .ward(ward)
                .isActive(true)
                .build();
        userRepository.save(user);

        return issueTokens(user);
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmailOrPhone())
                .or(() -> userRepository.findByPhone(request.getEmailOrPhone()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email/phone or password"));

        String principal = user.getEmail() != null ? user.getEmail() : user.getPhone();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(principal, request.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException("Invalid email/phone or password");
        }

        return issueTokens(user);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String username = jwtTokenProvider.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtTokenProvider.isRefreshTokenValid(refreshToken, userDetails)) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        User user = userRepository.findByEmail(username)
                .or(() -> userRepository.findByPhone(username))
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        return issueTokens(user);
    }

    private AuthResponse issueTokens(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(
                user.getEmail() != null ? user.getEmail() : user.getPhone()
        );
        return AuthResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(userDetails))
                .refreshToken(jwtTokenProvider.generateRefreshToken(userDetails))
                .tokenType("Bearer")
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }
}
