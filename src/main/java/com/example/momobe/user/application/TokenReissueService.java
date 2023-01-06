package com.example.momobe.user.application;

import com.example.momobe.common.domain.RedisStore;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.domain.TokenNotFoundException;
import com.example.momobe.user.dto.JwtTokenDto;
import com.example.momobe.user.dto.RedisUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.momobe.common.exception.enums.ErrorCode.DATA_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TokenReissueService {
    private final RedisStore<RedisUserDto> redisStore;
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenGenerateService tokenGenerateService;

    public JwtTokenDto reIssueToken(String refreshToken) {
        validateToken(refreshToken);
        RedisUserDto user = findValueOrThrowException(refreshToken);

        return tokenGenerateService.getJwtToken(user.getId());
    }

    private void validateToken(String refreshToken) {
        jwtTokenUtil.parseRefreshToken(refreshToken);
    }

    private RedisUserDto findValueOrThrowException(String refreshToken) {
        return redisStore.getDataAndDelete(refreshToken, RedisUserDto.class).orElseThrow(() -> new TokenNotFoundException(DATA_NOT_FOUND));
    }
}
