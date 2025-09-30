package com.spedu.tutors.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String name;
    private String role;
    private String loginAt;
    private String accessToken;
    private String refreshToken;
    private List<MenuResponse> menus;
}

