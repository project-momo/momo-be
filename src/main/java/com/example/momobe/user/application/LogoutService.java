package com.example.momobe.user.application;

import com.example.momobe.common.domain.RedisStore;
import com.example.momobe.user.dto.RedisUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {
    private final RedisStore<RedisUserDto> redisStore;

    public void logout(String refreshToken) {
        redisStore.deleteData(refreshToken);
    }
}
