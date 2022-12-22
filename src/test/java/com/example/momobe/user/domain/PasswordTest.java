package com.example.momobe.user.domain;

import com.example.momobe.common.enums.TestConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void init() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    @DisplayName("패스워드가 일치하지 않을 경우 false 반환")
    public void match1() {
        //given
        Password password = new Password(PASSWORD1, passwordEncoder);

        //when
        boolean result = password.match(PASSWORD2, passwordEncoder);

        //then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("패스워드가 일치할 경우 true 반환")
    public void match2() {
        //given
        Password password = new Password(PASSWORD1, passwordEncoder);

        //when
        boolean result = password.match(PASSWORD1, passwordEncoder);

        //then
        Assertions.assertThat(result).isTrue();
    }
}