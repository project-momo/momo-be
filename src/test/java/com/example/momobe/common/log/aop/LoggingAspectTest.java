package com.example.momobe.common.log.aop;

import com.example.momobe.common.log.entity.LogHistory;
import com.example.momobe.common.log.repository.LogHistoryRepository;
import com.example.momobe.question.dto.in.QuestionDto;
import com.example.momobe.question.dto.out.ResponseQuestionDto;
import com.example.momobe.question.infrastructure.QuestionQueryRepository;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.domain.Email;
import com.example.momobe.user.domain.Nickname;
import com.example.momobe.user.domain.Password;
import com.example.momobe.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
class LoggingAspectTest {
    @Autowired
    LogHistoryRepository logHistoryRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EntityManager em;

    @MockBean
    QuestionQueryRepository questionQueryRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    ObjectMapper objectMapper;

    String accessToken;

    @BeforeEach
    void init() {
        User user = User.builder()
                .nickname(new Nickname(NICKNAME))
                .email(new Email(EMAIL))
                .password(new Password(PASSWORD1))
                .build();

        em.persist(user);

        accessToken = jwtTokenUtil.createAccessToken(EMAIL, user.getId(), ROLE_USER_LIST, NICKNAME);
    }

    @Test
    @DisplayName("Http 요청 메서드가 GET일 때 로그를 저장하지 않는다.")
    void logging_test1() throws Exception {
        //given
        BDDMockito.given(questionQueryRepository.getQuestions(any()))
                .willReturn(List.of(ResponseQuestionDto.builder().build()));

        //when
        ResultActions result = mockMvc.perform(get("/meetings/{meeting-id}", 1L));

        //then
        List<LogHistory> logs = logHistoryRepository.findAll();
        Assertions.assertThat(logs.size()).isZero();
    }

    @Test
    @DisplayName("Http 요청 메서드가 POST일 때 로그를 저장한다")
    void logging_test2() throws Exception {
        //given
        QuestionDto request = QuestionDto.builder()
                .content(CONTENT1)
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        ResultActions result = mockMvc.perform(post("/meetings/{meeting-id}/questions", 1L)
                .header(JWT_HEADER, accessToken)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                );

        //then
        List<LogHistory> logs = logHistoryRepository.findAll();
        Assertions.assertThat(logs.size()).isOne();
    }
}