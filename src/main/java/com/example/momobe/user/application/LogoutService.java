package com.example.momobe.user.application;

import com.example.momobe.common.domain.RedisStore;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.domain.TokenNotFoundException;
import com.example.momobe.user.dto.JwtTokenDto;
import com.example.momobe.user.dto.RedisUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.momobe.common.exception.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class LogoutService {
    private final RedisStore<RedisUserDto> redisStore;

    public void logout(String refreshToken) {
        redisStore.deleteData(refreshToken);
    }
}
