package com.spedu.tutors.auth.config;

import java.util.Date;

public interface JwtService {
    String generateToken(com.spedu.tutors.auth.entity.User user);
    String generateRefreshToken(com.spedu.tutors.auth.entity.User user);
    boolean isTokenValid(String token, com.spedu.tutors.auth.entity.User user);
    boolean isValidRefreshToken(String token);
    String extractUsername(String token);
    Date extractExpiration(String token);
}

