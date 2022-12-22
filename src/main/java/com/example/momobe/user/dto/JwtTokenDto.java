package com.example.momobe.user.dto;

import lombok.Getter;

@Getter
public class JwtTokenDto {
    private final String accessToken;
    private final String refreshToken;

    public JwtTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
