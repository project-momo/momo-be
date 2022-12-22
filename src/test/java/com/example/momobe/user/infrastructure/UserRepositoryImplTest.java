package com.example.momobe.user.infrastructure;

import com.example.momobe.common.config.JpaQueryFactoryConfig;
import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.common.exception.CustomException;
import com.example.momobe.user.domain.Avatar;
import com.example.momobe.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

import javax.persistence.EntityManager;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureDataJpa
@Import(JpaQueryFactoryConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepositoryImplTest {
    @Autowired
    EntityManager em;

    UserRepositoryImpl userRepository;

    PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        userRepository = new UserRepositoryImpl(new JPAQueryFactory(em));
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    @DisplayName("중복된 이메일이 존재할 경우 CustomException 발생")
    void findUserByEmail_exception() {
        //given
        em.persist(new User(EMAIL1, NICKNAME1, PASSWORD1, new Avatar(REMOTE_PATH)));
        em.persist(new User(EMAIL1, NICKNAME2, PASSWORD2, new Avatar(REMOTE_PATH)));

        //when //then
        assertThatThrownBy(() -> userRepository.findUserByEmail(EMAIL1))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("중복된 이메일이 존재하지 않을 경우 같은 이메일을 가진 유저를 반환한다.")
    void findUserByEmail_success() {
        //given
        User user = new User(EMAIL1, NICKNAME1, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(user);

        //when
        User findUser = userRepository.findUserByEmail(EMAIL1).get();

        //then
        assertThat(user).isEqualTo(findUser);
    }
}