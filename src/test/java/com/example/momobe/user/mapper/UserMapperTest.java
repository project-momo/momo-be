package com.example.momobe.user.mapper;

import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.user.domain.Email;
import com.example.momobe.user.domain.Nickname;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.dto.RedisUserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    UserMapper userMapper;

    @BeforeEach
    void init() {
        userMapper = new UserMapperImpl();
    }

    @Test
    @DisplayName("user를 RedisUserDto로 매핑하는 경우 테스트")
    void userToRedisUserDto() {
        //given
        Email email = new Email(EMAIL1);
        Nickname nickname = new Nickname(NICKNAME1);
        User user = User.builder()
                .id(1L)
                .email(email)
                .nickname(nickname)
                .build();

        //when
        RedisUserDto dto = userMapper.of(user);

        //then
        assertThat(dto.getEmail()).isEqualTo(email);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNickname()).isEqualTo(nickname);
    }
}