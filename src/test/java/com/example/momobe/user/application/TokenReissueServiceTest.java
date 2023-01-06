package com.example.momobe.user.application;

import com.example.momobe.common.domain.RedisStore;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.domain.TokenNotFoundException;
import com.example.momobe.user.dto.JwtTokenDto;
import com.example.momobe.user.dto.RedisUserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TokenReissueServiceTest {
    @Mock
    RedisStore<RedisUserDto> redisStore;

    @Mock
    JwtTokenUtil jwtTokenUtil;

    @Mock
    TokenGenerateService tokenGenerateService;

    @InjectMocks
    TokenReissueService tokenReissueService;

    @Test
    @DisplayName("리프레시 토큰으로 조회된 값이 없을 경우 TokenNotFoundException 발생")
    void reIssue_failed1() {
        //given
        given(redisStore.getDataAndDelete(BEARER_REFRESH_TOKEN, RedisUserDto.class)).willReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> tokenReissueService.reIssueToken(BEARER_REFRESH_TOKEN))
                .isInstanceOf(TokenNotFoundException.class);
    }

    @Test
    @DisplayName("로직이 정상 수행될 경우 generateTokenService.getToken()가 반환한 값을 반환한다.")
    void reIssue_success() {
        //given
        RedisUserDto data = RedisUserDto.builder().id(ID1).build();
        JwtTokenDto jwtTokenDto = new JwtTokenDto(ACCESS_TOKEN, REFRESH_TOKEN);
        given(redisStore.getDataAndDelete(BEARER_REFRESH_TOKEN, RedisUserDto.class)).willReturn(Optional.ofNullable(data));
        given(tokenGenerateService.getJwtToken(ID1)).willReturn(jwtTokenDto);
        //when
        JwtTokenDto result = tokenReissueService.reIssueToken(BEARER_REFRESH_TOKEN);
        //then
        assertThat(result.getAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(result.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
    }
}