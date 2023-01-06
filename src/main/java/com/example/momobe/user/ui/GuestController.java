package com.example.momobe.user.ui;

import com.example.momobe.user.application.TokenGenerateService;
import com.example.momobe.user.application.GuestTokenGenerateService;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.dto.JwtTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.momobe.security.enums.SecurityConstants.*;

@RestController
@RequestMapping("/auth/token")
@RequiredArgsConstructor
public class GuestController {
    private final GuestTokenGenerateService guestTokenGenerateService;
    private final TokenGenerateService tokenGenerateService;

    @GetMapping("/guest")
    public ResponseEntity<JwtTokenDto> getGuestUserToken() {
        User guestUser = guestTokenGenerateService.generate();
        JwtTokenDto jwtToken = tokenGenerateService.getJwtToken(guestUser.getId());

        return ResponseEntity.status(201)
                .header(ACCESS_TOKEN, jwtToken.getAccessToken())
                .header(REFRESH_HEADER, jwtToken.getRefreshToken())
                .build();
    }
}
