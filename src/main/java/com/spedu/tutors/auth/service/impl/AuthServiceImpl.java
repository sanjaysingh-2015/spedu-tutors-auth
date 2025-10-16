package com.spedu.tutors.auth.service.impl;

import com.spedu.tutors.auth.config.JwtService;
import com.spedu.tutors.auth.dto.request.LoginRequest;
import com.spedu.tutors.auth.dto.request.RegisterRequest;
import com.spedu.tutors.auth.dto.response.AuthResponse;
import com.spedu.tutors.auth.entity.*;
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
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
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
    private final OnboardingStepRepository onboardingStepRepository;
    private final StudentOnboardingStepRepository studentOnboardingStepRepository;
    private final TutorOnboardingStepRepository tutorOnboardingStepRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodeGenerationUtils codeUtils;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        Role role = roleRepository.findByCode(request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("RegisterRequest", "Role", request.getRole()));
        Boolean profileCompleted = Boolean.TRUE;
        if(role.getCode().equalsIgnoreCase("TUTOR") || role.getCode().equalsIgnoreCase("STUDENT") ) {
            profileCompleted = Boolean.FALSE;
        }
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhoneNo())
                .role(role) // or default role
                .profileCompleted(profileCompleted)
                .status("ACTIVE")
                .build();

        User saved = userRepository.save(user);

        addOnboardingSteps(saved);

        String token = jwtService.generateToken(saved);
        String refreshToken = jwtService.generateRefreshToken(saved);
        return getResponse(user);
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
        return getResponse(user);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.isValidRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String email = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return getResponse(user);
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

    public void addOnboardingSteps(User user) {
        String roleCode = user.getRole().getCode();
        List<OnboardingStep> onboardingSteps = onboardingStepRepository.findAllByStatusAndOnboardingType("ACTIVE", roleCode);
        if("STUDENT".equalsIgnoreCase(roleCode)) {
            List<StudentOnboardingStep> studentSteps = onboardingSteps.stream()
                    .map(step -> StudentOnboardingStep.builder()
                            .user(user)
                            .onboardingStep(step)
                            .status("PENDING")
                            .createdAt(LocalDateTime.now())
                            .createdBy(user.getId())
                            .build())
                    .toList();
            studentOnboardingStepRepository.saveAll(studentSteps);
        } else if("TUTOR".equalsIgnoreCase(roleCode)) {
            List<TutorOnboardingStep> studentSteps = onboardingSteps.stream()
                    .map(step -> TutorOnboardingStep.builder()
                            .user(user)
                            .onboardingStep(step)
                            .status("PENDING")
                            .createdAt(LocalDateTime.now())
                            .createdBy(user.getId())
                            .build())
                    .toList();
            tutorOnboardingStepRepository.saveAll(studentSteps);
        }
    }

    private AuthResponse getResponse(User user) {
        return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getRole().getName(), dateFormat.format(new Date()),
                jwtService.generateToken(user),
                jwtService.generateRefreshToken(user),
                user.getProfileCompleted(),
                ObjectUtils.isEmpty(user.getCountry())? "": user.getCountry().getCode(),
                ObjectUtils.isEmpty(user.getCountry())? "": user.getCountry().getName(),
                null
        );
    }
}

