package com.example.momobe.user.ui;

import com.example.momobe.security.enums.SecurityConstants;
import com.example.momobe.user.application.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.momobe.security.enums.SecurityConstants.*;

@RestController
@RequestMapping("/auth/token")
@RequiredArgsConstructor
public class TokenController {
    private final LogoutService logoutService;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteToken(@RequestHeader(REFRESH_HEADER) String refreshToken) {
        logoutService.logout(refreshToken);
    }
}
