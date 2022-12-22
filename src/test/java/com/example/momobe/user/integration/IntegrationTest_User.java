package com.example.momobe.user.integration;

import com.example.momobe.common.domain.RedisStore;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.domain.Nickname;
import com.example.momobe.user.dto.RedisUserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest(value = {
        "jwt.secretKey=only_test_secret_Key_value_gn..rlfdlrkqnwhrgkekspdy",
        "jwt.refreshKey=only_test_refresh_key_value_gn..rlfdlrkqnwhrgkekspdy"
})
@AutoConfigureMockMvc
public class IntegrationTest_User {
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RedisStore<RedisUserDto> redisStore;

    private final String KEY = BEARER_REFRESH_TOKEN;
    private final Long EXPIRATION = 10000L;

    @BeforeEach
    void init() {
        redisStore.deleteData(KEY);
    }

    @Test
    @DisplayName("테스트 환경 _ 토큰을 저장하고 조회하면 값이 조회된다.")
    void ready_test() {
        //given
        RedisUserDto redisData = RedisUserDto.builder().nickname(new Nickname(NICKNAME)).build();
        redisStore.saveData(KEY, redisData, EXPIRATION);

        //when
        Optional<RedisUserDto> result = redisStore.getDataAndDelete(KEY, RedisUserDto.class);

        //then
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("로그아웃 요청 시 해당 토큰이 존재한다면 삭제하고 204를 반환한다")
    void logoutTest1() throws Exception {
        //given
        RedisUserDto redisData = RedisUserDto.builder().nickname(new Nickname(NICKNAME)).build();
        redisStore.saveData(KEY, redisData, EXPIRATION);

        //when
        ResultActions perform = mockMvc.perform(delete("/auth/token")
                .header(REFRESH_TOKEN, KEY));

        Optional<RedisUserDto> result = redisStore.getData(KEY, RedisUserDto.class);

        //then
        perform.andExpect(status().isNoContent());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("로그아웃 요청 시 해당 토큰이 존재하지 않아도 예외가 발생하지 않으며 204를 반환한다")
    void logoutTest2() throws Exception {
        //given
        //when
        ResultActions perform = mockMvc.perform(delete("/auth/token")
                .header(REFRESH_TOKEN, KEY));

        Optional<RedisUserDto> result = redisStore.getData(KEY, RedisUserDto.class);

        //then
        perform.andExpect(status().isNoContent());
        assertThat(result).isEmpty();
    }
}
