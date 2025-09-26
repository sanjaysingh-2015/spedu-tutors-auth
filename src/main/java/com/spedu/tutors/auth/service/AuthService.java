package com.spedu.tutors.auth.service;

import com.spedu.tutors.auth.dto.request.LoginRequest;
import com.spedu.tutors.auth.dto.request.RegisterRequest;
import com.spedu.tutors.auth.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String refreshToken);
    void logout(String userId); // Optional
}
