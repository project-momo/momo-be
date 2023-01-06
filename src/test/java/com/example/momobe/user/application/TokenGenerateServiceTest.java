package com.example.momobe.user.application;

import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.domain.*;
import com.example.momobe.user.dto.JwtTokenDto;
import com.example.momobe.user.dto.RedisUserDto;
import com.example.momobe.user.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.user.domain.enums.RoleName.ROLE_USER;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TokenGenerateServiceTest {
    @Mock
    JwtTokenUtil jwtTokenUtil;

    @Mock
    UserRepository userRepository;

    @Mock
    UserRedisStore userRedisStore;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    TokenGenerateService tokenGenerateService;

    @Test
    @DisplayName("유저가 존재하지 않을 경우 UserNotFoudException이 발생한다")
    void getJwtToken_failed1() {
        //given
        given(userRepository.findById(any())).willReturn(Optional.empty());

        //when //then
        assertThatThrownBy(() -> tokenGenerateService.getJwtToken(ID1))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("어떤 예외도 발생하지 않을 경우 userRepository가 1회 실행된다.")
    void getJwtToken_verify() {
        //given
        User user = User.builder().email(new Email(EMAIL1))
                .id(ID1)
                .nickname(new Nickname(NICKNAME1))
                .role(new Role(List.of(ROLE_USER)))
                .build();

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(jwtTokenUtil.createAccessToken(EMAIL1, ID1, ROLE_USER_LIST, NICKNAME1)).willReturn(ACCESS_TOKEN);
        given(jwtTokenUtil.createRefreshToken(EMAIL1, ID1, ROLE_USER_LIST, NICKNAME1)).willReturn(REFRESH_TOKEN);
        given(userMapper.of(user)).willReturn(RedisUserDto.builder().email(new Email(EMAIL1)).build());

        //when
        tokenGenerateService.getJwtToken(ID1);

        //then
        verify(userRepository,times(1)).findById(1L);
    }

    @Test
    @DisplayName("어떤 예외도 발생하지 않을 경우 jwtTokenUtil이 총 2회 실행된다.")
    void getJwtToken_verify2() {
        //given
        User user = User.builder().email(new Email(EMAIL1))
                .id(ID1)
                .nickname(new Nickname(NICKNAME1))
                .role(new Role(List.of(ROLE_USER)))
                .build();

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(jwtTokenUtil.createAccessToken(EMAIL1, ID1, ROLE_USER_LIST, NICKNAME1)).willReturn(ACCESS_TOKEN);
        given(jwtTokenUtil.createRefreshToken(EMAIL1, ID1, ROLE_USER_LIST, NICKNAME1)).willReturn(REFRESH_TOKEN);
        given(userMapper.of(user)).willReturn(RedisUserDto.builder().email(new Email(EMAIL1)).build());

        //when
        tokenGenerateService.getJwtToken(ID1);

        //then
        verify(jwtTokenUtil, times(1)).createAccessToken(EMAIL1, ID1, ROLE_USER_LIST, NICKNAME1);
        verify(jwtTokenUtil, times(1)).createRefreshToken(EMAIL1, ID1, ROLE_USER_LIST, NICKNAME1);
    }

    @Test
    @DisplayName("어떤 예외도 발생하지 않을 경우 userRedisStore가 1회 실행된다.")
    void getJwtToken_verify3() {
        //given
        User user = User.builder().email(new Email(EMAIL1))
                .id(ID1)
                .nickname(new Nickname(NICKNAME1))
                .role(new Role(List.of(ROLE_USER)))
                .build();

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(jwtTokenUtil.createAccessToken(EMAIL1, ID1, ROLE_USER_LIST, NICKNAME1)).willReturn(ACCESS_TOKEN);
        given(jwtTokenUtil.createRefreshToken(EMAIL1, ID1, ROLE_USER_LIST, NICKNAME1)).willReturn(REFRESH_TOKEN);
        given(userMapper.of(user)).willReturn(RedisUserDto.builder().email(new Email(EMAIL1)).build());

        //when
        tokenGenerateService.getJwtToken(ID1);

        //then
        verify(userRedisStore, times(1)).saveData(anyString(), any(RedisUserDto.class), anyLong());
    }

    @Test
    @DisplayName("어떤 예외도 발생하지 않을 경우 최종적으로 TokenDto를 반환한다.")
    void getJwtToken_Success() {
        //given
        User user = User.builder().email(new Email(EMAIL1))
                .id(ID1)
                .nickname(new Nickname(NICKNAME1))
                .role(new Role(List.of(ROLE_USER)))
                .build();

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(jwtTokenUtil.createAccessToken(EMAIL1, ID1, ROLE_USER_LIST, NICKNAME1)).willReturn(ACCESS_TOKEN);
        given(jwtTokenUtil.createRefreshToken(EMAIL1, ID1, ROLE_USER_LIST, NICKNAME1)).willReturn(REFRESH_TOKEN);
        given(userMapper.of(user)).willReturn(RedisUserDto.builder().email(new Email(EMAIL1)).build());

        //when
        JwtTokenDto result = tokenGenerateService.getJwtToken(ID1);

        //then
        assertThat(result.getAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(result.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
    }
}