package com.example.momobe.security.domain;

import io.jsonwebtoken.Claims;

import java.util.List;

public interface JwtTokenUtil {
    String createAccessToken(String email, Long id, List<String> roles, String nickname);
    String createRefreshToken(String email, Long id, List<String> roles, String nickname);
    Claims parseAccessToken(String token);
    Claims parseRefreshToken(String token);
}
