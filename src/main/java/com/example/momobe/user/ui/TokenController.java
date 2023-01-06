package com.example.momobe.user.ui;

import com.example.momobe.user.application.LogoutService;
import com.example.momobe.user.application.TokenReissueService;
import com.example.momobe.user.dto.JwtTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.momobe.security.enums.SecurityConstants.*;

@RestController
@RequestMapping("/auth/token")
@RequiredArgsConstructor
public class TokenController {
    private final LogoutService logoutService;
    private final TokenReissueService tokenReissueService;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteToken(@RequestHeader(REFRESH_HEADER) String refreshToken) {
        logoutService.logout(refreshToken);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JwtTokenDto> reissueToken(@RequestHeader(REFRESH_HEADER) String refreshToken) {
        JwtTokenDto jwtTokenDto = tokenReissueService.reIssueToken(refreshToken);

        return ResponseEntity
                .status(200)
                .header(ACCESS_TOKEN, jwtTokenDto.getAccessToken())
                .header(REFRESH_HEADER, jwtTokenDto.getRefreshToken())
                .build();
    }
}
