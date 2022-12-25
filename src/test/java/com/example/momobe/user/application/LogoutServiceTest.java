package com.example.momobe.user.application;

import com.example.momobe.common.domain.RedisStore;
import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.user.dto.RedisUserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTest {
    @Mock
    RedisStore<RedisUserDto> redisStore;

    @InjectMocks
    LogoutService logoutService;

    @Test
    @DisplayName("메서드 실행 시 redisStore가 호출된다.")
    void logout() {
        //given
        //when
        logoutService.logout(REFRESH_TOKEN);

        //then
        verify(redisStore).deleteData(REFRESH_TOKEN);
    }
}