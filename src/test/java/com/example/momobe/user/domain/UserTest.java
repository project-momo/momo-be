package com.example.momobe.user.domain;

import com.example.momobe.common.enums.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    @DisplayName("picture가 없어도 예외가 발생하지 않음")
    void user_constructor_test1() {
        //given
        //when
        //then
        User user = new User(EMAIL1, NICKNAME1, PASSWORD1, null,new UserState(null,null));
    }
}