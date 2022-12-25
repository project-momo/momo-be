package com.example.momobe.user.integration;

import com.example.momobe.common.domain.RedisStore;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.domain.Email;
import com.example.momobe.user.domain.Nickname;
import com.example.momobe.user.domain.Password;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.dto.RedisUserDto;
import com.example.momobe.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest(value = {
        "jwt.secretKey=only_test_secret_Key_value_gn..rlfdlrkqnwhrgkekspdy",
        "jwt.refreshKey=only_test_refresh_key_value_gn..rlfdlrkqnwhrgkekspdy"
})
@AutoConfigureMockMvc
public class Reissue_IntegrationTest {
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RedisStore<RedisUserDto> redisStore;

    @Autowired
    UserMapper userMapper;

    @Autowired
    EntityManager entityManager;

    private String KEY;
    private String MALFORMED_KEY;
    private final Long EXPIRATION = 10000L;

    @BeforeEach
    void init() {
        KEY = jwtTokenUtil.createRefreshToken(EMAIL1, ID1, ROLE_USER_LIST, NICKNAME1);
        MALFORMED_KEY = INVALID_REFRESH_TOKEN;
        redisStore.deleteData(KEY);
    }

    @Test
    @DisplayName("해당 리프레시 토큰이 저장되어 있지 않을 경우 404 반환")
    void reissue_failed1() throws Exception {
        //given
        //when
        ResultActions perform = mockMvc.perform(put("/auth/token")
                .header(REFRESH_TOKEN, KEY));

        //then
        perform.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("해당 리프레시 만료되어거나 유효하지 않은 형태의 토큰인 경우 401 반환")
    void reissue_failed2() throws Exception {
        //given
        //when
        ResultActions perform = mockMvc.perform(put("/auth/token")
                .header(REFRESH_TOKEN, MALFORMED_KEY));

        //then
        perform.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("해당 리프레시 토큰이 유효하고 저장되어 있는 경우 새로운 토큰 세트 반환")
    void reissue_success() throws Exception {
        //given
        User user = User.builder()
                .email(new Email(EMAIL1))
                .nickname(new Nickname(NICKNAME))
                .password(new Password(PASSWORD1))
                .build();

        entityManager.persist(user);

        RedisUserDto dto = userMapper.of(user);
        redisStore.saveData(KEY, dto, EXPIRATION);

        //when
        ResultActions perform = mockMvc.perform(put("/auth/token")
                .header(REFRESH_TOKEN, KEY));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString());
    }
}
