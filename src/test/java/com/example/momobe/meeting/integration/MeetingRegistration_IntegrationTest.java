package com.example.momobe.meeting.integration;

import com.example.momobe.security.domain.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.enums.MeetingConstants.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MeetingRegistration_IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private String accessToken;

    @BeforeEach
    void init() {
        accessToken = jwtTokenUtil.createAccessToken(EMAIL1, ID1, ROLE_USER_LIST, NICKNAME1);
    }

    @Test
    @DisplayName("모임 등록 (하루 일정) 201 반환")
    public void meetingRegistrationWithOneDay() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(MEETING_REQUEST_DTO_WITH_ONE_DAY);

        // when
        ResultActions actions = mockMvc.perform(
                post("/meetings")
                        .content(content)
                        .header(JWT_HEADER, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("모임 등록 (정기 일정) 201 반환")
    public void meetingRegistrationWithPeriod() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(MEETING_REQUEST_DTO_WITH_PERIOD);

        // when
        ResultActions actions = mockMvc.perform(
                post("/meetings")
                        .content(content)
                        .header(JWT_HEADER, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("모임 등록 (자유 일정) 201 반환")
    public void meetingRegistrationWithFree() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(MEETING_REQUEST_DTO_WITH_FREE);

        // when
        ResultActions actions = mockMvc.perform(
                post("/meetings")
                        .content(content)
                        .header(JWT_HEADER, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("유효성 검사 실패시 400 반환")
    public void meetingRegistrationFail() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(" ");

        // when
        ResultActions actions = mockMvc.perform(
                post("/meetings")
                        .content(content)
                        .header(JWT_HEADER, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isBadRequest());
    }

}
