package com.example.momobe.question.integration;

import com.example.momobe.answer.domain.Answer;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.question.domain.Content;
import com.example.momobe.question.domain.Question;
import com.example.momobe.question.domain.Writer;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.domain.Email;
import com.example.momobe.user.domain.Nickname;
import com.example.momobe.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.enums.MeetingConstants.generateMeeting;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest(value = {
        "jwt.secretKey=only_test_secret_Key_value_gn..rlfdlrkqnwhrgkekspdy",
        "jwt.refreshKey=only_test_refresh_key_value_gn..rlfdlrkqnwhrgkekspdy"
})
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
public class GetQuestions_IntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Test
    @DisplayName("")
    void getQuestion() throws Exception {
        //given
        Meeting meeting = generateMeeting();
        entityManager.persist(meeting);

        User user = User.builder()
                .email(new Email(EMAIL1))
                .nickname(new Nickname(NICKNAME1))
                .avatar(null)
                .build();

        entityManager.persist(user);

        Question question1 = Question.builder()
                .content(new Content(CONTENT1))
                .meeting(new com.example.momobe.question.domain.Meeting(meeting.getId()))
                .writer(new Writer(user.getId()))
                .build();

        Question question2 = Question.builder()
                .content(new Content(CONTENT2))
                .meeting(new com.example.momobe.question.domain.Meeting(meeting.getId()))
                .writer(new Writer(user.getId()))
                .build();

        entityManager.persist(question1);
        entityManager.persist(question2);

        Answer answer1 = Answer.builder()
                .question(new com.example.momobe.answer.domain.Question(question1.getId()))
                .content(new com.example.momobe.answer.domain.Content(CONTENT1))
                .writer(new com.example.momobe.answer.domain.Writer(user.getId()))
                .meeting(new com.example.momobe.answer.domain.Meeting(meeting.getId())).build();

        Answer answer2 = Answer.builder()
                .question(new com.example.momobe.answer.domain.Question(question1.getId()))
                .content(new com.example.momobe.answer.domain.Content(CONTENT1))
                .writer(new com.example.momobe.answer.domain.Writer(user.getId()))
                .meeting(new com.example.momobe.answer.domain.Meeting(meeting.getId())).build();

        entityManager.persist(answer1);
        entityManager.persist(answer2);

        //when
        ResultActions result = mockMvc.perform(get("/meetings/{meeting-id}/qna", meeting.getId()));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[1].questionId", is(question2.getId().intValue())))
                .andExpect(jsonPath("$[0].questionId", is(question1.getId().intValue())))
                .andDo(print());
    }
}
