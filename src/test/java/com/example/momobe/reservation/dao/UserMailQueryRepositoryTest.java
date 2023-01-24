package com.example.momobe.reservation.dao;

import com.example.momobe.common.config.JpaQueryFactoryConfig;
import com.example.momobe.user.domain.Email;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.infrastructure.UnableToProcessException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

import static com.example.momobe.common.enums.TestConstants.*;

@DataJpaTest
@AutoConfigureDataJpa
@Import(JpaQueryFactoryConfig.class)
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMailQueryRepositoryTest {
    @Autowired
    EntityManager em;

    private UserMailQueryRepository userMailQueryRepository;

    @BeforeEach
    void initField() {
        userMailQueryRepository = new UserMailQueryRepository(new JPAQueryFactory(em));
    }

    @Test
    @DisplayName("존재하지 않는 유저를 조회할 경우 UnableToProcessException 발생")
    void findUserMail() {
        //given //when //then
        Assertions.assertThatThrownBy(() -> userMailQueryRepository.findMailOf(-1L))
                .isInstanceOf(UnableToProcessException.class);
    }

    @Test
    @DisplayName("존재하는 유저 Id로 조회할 경우 해당 유저의 이메일 반환")
    void findUserMail2() {
        //given
        User user = User.builder()
                .email(new Email(EMAIL1))
                .build();

        em.persist(user);

        //when
        String result = userMailQueryRepository.findMailOf(user.getId());

        //then
        Assertions.assertThat(user.getEmail().getAddress()).isEqualTo(result);
    }
}