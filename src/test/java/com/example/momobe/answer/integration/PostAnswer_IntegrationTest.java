package com.example.momobe.answer.integration;

import com.example.momobe.answer.domain.Answer;
import com.example.momobe.answer.domain.AnswerRepository;
import com.example.momobe.answer.domain.Meeting;
import com.example.momobe.answer.dto.AnswerDto;
import com.example.momobe.question.domain.*;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.common.enums.TestConstants.ID1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest(value = {
        "jwt.secretKey=only_test_secret_Key_value_gn..rlfdlrkqnwhrgkekspdy",
        "jwt.refreshKey=only_test_refresh_key_value_gn..rlfdlrkqnwhrgkekspdy"
})
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
public class PostAnswer_IntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    EntityManager em;

    private String accessToken;

    @BeforeEach
    void init() {
        User user = User.builder().build();
        em.persist(user);
        accessToken = jwtTokenUtil.createAccessToken(EMAIL1, user.getId(), ROLE_USER_LIST, NICKNAME1);
    }

    @Test
    @DisplayName("유효성 검사 실패시 400 반환")
    void postQuestion_failed() throws Exception {
        // given
        AnswerDto request = AnswerDto.builder().content(" ").build();
        String json = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(post("/meetings/1/questions")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유효성 검사 성공시 201반환하고 db에 해당 값이 저장된다.")
    void postQuestion_success() throws Exception {
        // given
        AnswerDto request = AnswerDto.builder().content(CONTENT1).build();
        String json = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(post("/meetings/1/questions/1/answers")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .header(JWT_HEADER, accessToken));
        Answer result = answerRepository.findAll().get(0);

        // then
        perform.andExpect(status().isCreated());
        assertThat(result.getContent()).isEqualTo(new com.example.momobe.answer.domain.Content(CONTENT1));
        assertThat(result.getMeeting()).isEqualTo(new Meeting(ID1));
    }
}
