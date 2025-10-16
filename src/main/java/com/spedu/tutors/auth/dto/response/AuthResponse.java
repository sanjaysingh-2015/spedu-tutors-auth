package com.spedu.tutors.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private Long userId;
    private String name;
    private String role;
    private String loginAt;
    private String accessToken;
    private String refreshToken;
    private Boolean profileCompleted;
    private String countryCode;
    private String countryName;
    private List<MenuResponse> menus;
}

