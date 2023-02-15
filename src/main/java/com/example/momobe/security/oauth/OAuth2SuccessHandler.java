package com.example.momobe.security.oauth;

import com.example.momobe.user.application.TokenGenerateService;
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
import static com.example.momobe.security.constants.SecurityConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final TokenGenerateService tokenGenerateService;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (response.isCommitted()) return;

        String redirectURL = getRedirectURI(request);
        System.out.println(request.getRequestURL());

        User user = findUserBy(authentication);
        JwtTokenDto jwtTokenDto = tokenGenerateService.getJwtToken(user.getId());

        String responseUrl = createResponseUrl(jwtTokenDto.getAccessToken(), jwtTokenDto.getRefreshToken(), redirectURL);
        getRedirectStrategy().sendRedirect(request, response, responseUrl);
    }

    private String getRedirectURI(HttpServletRequest request) {
        String referer = request.getHeader("Referer");

        if (referer.contains(LOCAL_URL)) {
            return REDIRECT_URL_LOCAL;
        }

        if (referer.contains(SERVER_URL)) {
            return REDIRECT_URL_SERVER;
        }

        throw new SecurityException(String.valueOf(INVALID_URL));
    }

    private User findUserBy(Authentication authentication) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String email = (String)((Map)user.getAttributes().get("kakao_account")).get("email");

        return userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(DATA_NOT_FOUND));
    }

    private void setTokenAndRedirect(HttpServletResponse response, JwtTokenDto jwtTokenDto) throws IOException {
        response.setHeader(ACCESS_TOKEN, jwtTokenDto.getAccessToken());
        response.setHeader(REFRESH_HEADER, jwtTokenDto.getAccessToken());
        response.sendRedirect(REDIRECT_URL_SERVER);
    }

    private String createResponseUrl(String accessToken, String refreshToken, String redirectURL) {
        return UriComponentsBuilder.fromUriString(redirectURL)
                .queryParam(REFRESH_HEADER, refreshToken)
                .queryParam(ACCESS_TOKEN, accessToken)
                .build()
                .toUriString();
    }
}
