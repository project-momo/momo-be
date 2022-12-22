package com.example.momobe.common.domain;

import com.example.momobe.user.domain.Email;
import com.example.momobe.user.domain.Nickname;
import com.example.momobe.user.domain.Role;
import com.example.momobe.user.domain.enums.RoleName;
import com.example.momobe.user.dto.RedisUserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.example.momobe.common.enums.TestConstants.*;

@SpringBootTest
class RedisStoreTest {
    @Autowired
    RedisStore<RedisUserDto> redisStore;

    private final String KEY = "TEST_KEY_ONLY";
    private final Long EXPIRATION = 10000L;

    @BeforeEach
    void deleteData() {
        redisStore.deleteData(KEY);
    }

    @Test
    @DisplayName("특정 데이터를 저장하고 조회할 경우 같은 값을 반환한다.")
    void getDataTest() {
        //given
        Email email = new Email(EMAIL1);
        Nickname nickname = new Nickname(NICKNAME);
        Role role = new Role(List.of(RoleName.ROLE_USER));
        RedisUserDto data = RedisUserDto.builder()
                .id(ID1)
                .email(email)
                .nickname(nickname)
                .role(role)
                .build();

        redisStore.saveData(KEY, data, EXPIRATION);

        //when
        RedisUserDto result = redisStore.getData(KEY, RedisUserDto.class).get();

        //then
        Assertions.assertThat(result.getId()).isEqualTo(ID1);
        Assertions.assertThat(result.getEmail()).isEqualTo(email);
        Assertions.assertThat(result.getNickname()).isEqualTo(nickname);
        Assertions.assertThat(result.getRole().getRoles()).isEqualTo(role.getRoles());
    }

    @Test
    @DisplayName("특정 데이터를 저장하고 getData()로 2회 조회해도 같은 값을 반환한다")
    void getDataTest3() {
        //given
        Email email = new Email(EMAIL1);
        Nickname nickname = new Nickname(NICKNAME);
        Role role = new Role(List.of(RoleName.ROLE_USER));
        RedisUserDto data = RedisUserDto.builder()
                .id(ID1)
                .email(email)
                .nickname(nickname)
                .role(role)
                .build();

        redisStore.saveData(KEY, data, EXPIRATION);

        //when
        redisStore.getData(KEY, RedisUserDto.class).get();
        RedisUserDto result = redisStore.getData(KEY, RedisUserDto.class).get();

        //then
        Assertions.assertThat(result.getId()).isEqualTo(ID1);
        Assertions.assertThat(result.getEmail()).isEqualTo(email);
        Assertions.assertThat(result.getNickname()).isEqualTo(nickname);
        Assertions.assertThat(result.getRole().getRoles()).isEqualTo(role.getRoles());
    }

    @Test
    @DisplayName("특정 데이터를 저장하고 getDataAndDelete()로 조회할 경우 같은 값을 반환한다.")
    void getDataAndDeleteTest1() {
        //given
        Email email = new Email(EMAIL1);
        Nickname nickname = new Nickname(NICKNAME);
        Role role = new Role(List.of(RoleName.ROLE_USER));
        RedisUserDto data = RedisUserDto.builder()
                .id(ID1)
                .email(email)
                .nickname(nickname)
                .role(role)
                .build();

        redisStore.saveData(KEY, data, EXPIRATION);

        //when
        RedisUserDto result = redisStore.getDataAndDelete(KEY, RedisUserDto.class).get();

        //then
        Assertions.assertThat(result.getId()).isEqualTo(ID1);
        Assertions.assertThat(result.getEmail()).isEqualTo(email);
        Assertions.assertThat(result.getNickname()).isEqualTo(nickname);
        Assertions.assertThat(result.getRole().getRoles()).isEqualTo(role.getRoles());
    }

    @Test
    @DisplayName("특정 데이터를 저장하고 getDataAndDelete()로 2회 조회할 경우 Optional.empty()를 반환한다.")
    void getDataAndDeleteTest2() {
        //given
        Email email = new Email(EMAIL1);
        Nickname nickname = new Nickname(NICKNAME);
        Role role = new Role(List.of(RoleName.ROLE_USER));
        RedisUserDto data = RedisUserDto.builder()
                .id(ID1)
                .email(email)
                .nickname(nickname)
                .role(role)
                .build();

        redisStore.saveData(KEY, data, EXPIRATION);

        //when
        redisStore.getDataAndDelete(KEY, RedisUserDto.class).get();
        Optional<RedisUserDto> result = redisStore.getDataAndDelete(KEY, RedisUserDto.class);

        //then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("특정 데이터를 저장하고 삭제한 뒤 다시 조회할 경우 Optional.empty()를 반환한다.")
    void deleteTest() {
        //given
        Email email = new Email(EMAIL1);
        Nickname nickname = new Nickname(NICKNAME);
        Role role = new Role(List.of(RoleName.ROLE_USER));
        RedisUserDto data = RedisUserDto.builder()
                .id(ID1)
                .email(email)
                .nickname(nickname)
                .role(role)
                .build();

        redisStore.saveData(KEY, data, EXPIRATION);

        //when
        redisStore.deleteData(KEY);
        Optional<RedisUserDto> result = redisStore.getData(KEY, RedisUserDto.class);

        //then
        Assertions.assertThat(result).isEmpty();
    }
}