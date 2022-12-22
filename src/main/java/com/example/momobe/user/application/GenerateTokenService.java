package com.example.momobe.user.application;

import com.example.momobe.common.exception.CustomException;
import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.security.enums.SecurityConstants;
import com.example.momobe.user.domain.*;
import com.example.momobe.user.dto.JwtTokenDto;
import com.example.momobe.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.momobe.security.enums.SecurityConstants.*;

@RequiredArgsConstructor
public class GenerateTokenService {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final UserRedisStore userRedisStore;
    private final UserMapper userMapper;

    public JwtTokenDto getJwtToken(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.DATA_NOT_FOUND));

        JwtTokenDto jwtTokenDto = generateTokenSet(user.getEmail(), user.getId(), user.getRole(), user.getNickname());
        saveRefreshToken(jwtTokenDto,user);

        return jwtTokenDto;
    }

    private JwtTokenDto generateTokenSet(Email email, Long id, Role roles, Nickname nickname) {
        String accessToken = jwtTokenUtil.createAccessToken(email.getAddress(), id, roles.getRoles(), nickname.getNickname());
        String refreshToken = jwtTokenUtil.createRefreshToken(email.getAddress(), id, roles.getRoles(), nickname.getNickname());

        return new JwtTokenDto(accessToken, refreshToken);
    }

    private void saveRefreshToken(JwtTokenDto jwtTokenDto, User user) {
        userRedisStore.saveData(jwtTokenDto.getRefreshToken(), userMapper.of(user), REFRESH_TOKEN_EXPIRE_COUNT);
    }
}