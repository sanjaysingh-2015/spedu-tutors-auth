package com.spedu.tutors.auth.service.impl;

import com.spedu.tutors.auth.config.JwtService;
import com.spedu.tutors.auth.dto.request.LoginRequest;
import com.spedu.tutors.auth.dto.request.RegisterRequest;
import com.spedu.tutors.auth.dto.response.AuthResponse;
import com.spedu.tutors.auth.dto.response.MenuResponse;
import com.spedu.tutors.auth.entity.*;
import com.spedu.tutors.auth.enums.EnumStatus;
import com.spedu.tutors.auth.exceptions.ResourceNotFoundException;
import com.spedu.tutors.auth.repository.*;
import com.spedu.tutors.auth.service.AuthService;
import com.spedu.tutors.auth.utils.CodeGenerationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodeGenerationUtils codeUtils;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        Role role = roleRepository.findByCode(request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("RegisterRequest", "Role", request.getRole()));
        String codePrefix = request.getRole().substring(0,3);
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(role) // or default role
                .status("ACTIVE")
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new AuthResponse(token, refreshToken, null);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new AuthResponse(token, refreshToken, null);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.isValidRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String email = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new AuthResponse(
                jwtService.generateToken(user),
                jwtService.generateRefreshToken(user),
                null
        );
    }



    public void logout(String token) {
        Date expiration = jwtService.extractExpiration(token);
        LocalDateTime expiresAt = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        if(!isTokenBlacklisted(token)) {
            blacklistedTokenRepository.save(BlacklistedTokenEntity.builder()
                    .token(token)
                    .expiryTime(expiresAt)
                    .build());
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }

}

