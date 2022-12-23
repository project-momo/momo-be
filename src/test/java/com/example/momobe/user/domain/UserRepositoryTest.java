package com.example.momobe.user.domain;

import com.example.momobe.common.config.JpaQueryFactoryConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.common.enums.TestConstants.REMOTE_PATH;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureDataJpa
@Import(JpaQueryFactoryConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    @BeforeEach
    void init () {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    @DisplayName("아바타가 포함된 User 저장 시 아바타도 함께 저장된다.")
    public void saveWithAvatarTest() {
        //given
        Avatar avatar = new Avatar(REMOTE_PATH);
        User user = new User(EMAIL1, NICKNAME1, PASSWORD1, avatar,null);

        //when
        User savedUser = userRepository.save(user);

        //then
        assertThat(avatar).isEqualTo(savedUser.getAvatar());
    }
}