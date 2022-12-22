package com.example.momobe.common.util;

import com.example.momobe.user.domain.RandomPasswordGenerator;
import com.example.momobe.user.infrastructure.RandomPasswordGeneratorImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.example.momobe.security.enums.SecurityConstants.*;

class RandomPasswordGeneratorTest {
    RandomPasswordGenerator randomPasswordGenerator;

    @BeforeEach
    void init() {
        randomPasswordGenerator = new RandomPasswordGeneratorImpl();
    }

    @Test
    @DisplayName("generateTemporaryPassword 메서드로 매번 다른 임시 비밀번호를 생성한다.")
    void generateTemporaryPassword() {
        //given
        int size = 20;
        Set<String> temporaryPasswordSet = new HashSet<>();

        //when
        for (int i = 0; i < size; i++) {
            temporaryPasswordSet.add(randomPasswordGenerator.generateTemporaryPassword());
        }

        //then
        Assertions.assertThat(temporaryPasswordSet.size()).isEqualTo(size);
    }

    @Test
    @DisplayName("generateTemporaryPassword 생성되는 임시번호의 길이는 늘 일정하다.")
    void generateTemporaryPassword_length() {
        //given
        int size = 20;
        Set<String> temporaryPasswordSet = new HashSet<>();

        //when
        for (int i=0; i<size; i++) {
            temporaryPasswordSet.add(randomPasswordGenerator.generateTemporaryPassword());
        }

        //then
        temporaryPasswordSet.forEach(
                e -> Assertions.assertThat(e.length()).isEqualTo(TEMPORARY_PASSWORD_LENGTH)
        );
    }
}