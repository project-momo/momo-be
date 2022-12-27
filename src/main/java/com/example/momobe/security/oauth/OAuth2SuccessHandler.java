package com.example.momobe.security.oauth;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.user.application.GenerateTokenService;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserNotFoundException;
import com.example.momobe.user.domain.UserRepository;
import com.example.momobe.user.dto.JwtTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.example.momobe.common.exception.enums.ErrorCode.*;
import static com.example.momobe.security.enums.SecurityConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final GenerateTokenService generateTokenService;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (response.isCommitted()) return;

        User user = findUserBy(authentication);
        JwtTokenDto jwtTokenDto = generateTokenService.getJwtToken(user.getId());

        String responseUrl = createResponseUrl(jwtTokenDto.getAccessToken(), jwtTokenDto.getRefreshToken());
        getRedirectStrategy().sendRedirect(request, response, responseUrl);
    }

    private User findUserBy(Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String email = (String)((Map)user.getAttributes().get("kakao_account")).get("email");

        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(DATA_NOT_FOUND));
    }

    private void setTokenAndRedirect(HttpServletResponse response, JwtTokenDto jwtTokenDto) throws IOException {
        response.setHeader(ACCESS_TOKEN, jwtTokenDto.getAccessToken());
        response.setHeader(REFRESH_HEADER, jwtTokenDto.getAccessToken());
        response.sendRedirect(REDIRECT_URL_OAUTH2);
    }

    private String createResponseUrl(String accessToken, String refreshToken) {
        return UriComponentsBuilder.fromUriString(REDIRECT_URL_OAUTH2)
                .queryParam(REFRESH_HEADER, refreshToken)
                .queryParam(ACCESS_TOKEN, accessToken)
                .build()
                .toUriString();
    }
}
