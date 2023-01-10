package com.example.momobe.meeting.integration;

import com.example.momobe.address.domain.Address;
import com.example.momobe.meeting.dto.in.MeetingRequestDto;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.tag.domain.Tag;
import com.example.momobe.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.domain.enums.Tag.valueOf;
import static com.example.momobe.meeting.enums.MeetingConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
class MeetingRegistration_IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    EntityManager em;

    private String accessToken;
    private MeetingRequestDto meetingRequestDto;
    private Address address1;
    private Address address2;

    @BeforeEach
    void init() {
        User user = User.builder().build();
        em.persist(user);
        accessToken = jwtTokenUtil.createAccessToken(EMAIL1, user.getId(), ROLE_USER_LIST, NICKNAME1);
        address1 = Address.builder().si("서울").gu("강남구").build();
        address2 = Address.builder().si("서울").gu("강동구").build();
        em.persist(address1);
        em.persist(address2);
        TAGS.stream()
                .filter(tag -> em.createQuery("select t.id from Tag t where t.engName = '" + tag.name() + "'", Long.class)
                        .getResultList().isEmpty())
                .forEach(tag -> em.persist(new Tag(tag.getDescription(), tag.name())));
    }

    @Test
    @DisplayName("모임 등록 (하루 일정) 201 반환")
    void meetingRegistrationWithOneDay() throws Exception {
        // given
        meetingRequestDto = generateMeetingRequestDtoWithOneDay(
                TAGS, List.of(address1.getId(), address2.getId())
        );
        String content = objectMapper.writeValueAsString(meetingRequestDto);

        // when
        ResultActions actions = mockMvc.perform(
                post("/meetings")
                        .header(JWT_HEADER, accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("모임 등록 (정기 일정) 201 반환")
    void meetingRegistrationWithPeriod() throws Exception {
        // given
        meetingRequestDto = generateMeetingRequestDtoWithPeriod(
                TAGS, List.of(address1.getId(), address2.getId())
        );
        String content = objectMapper.writeValueAsString(meetingRequestDto);

        // when
        ResultActions actions = mockMvc.perform(
                post("/meetings")
                        .content(content)
                        .header(JWT_HEADER, accessToken)
                        .contentType(APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("모임 등록 (자유 일정) 201 반환")
    void meetingRegistrationWithFree() throws Exception {
        // given
        meetingRequestDto = generateMeetingRequestDtoWithFree(
                TAGS, List.of(address1.getId(), address2.getId())
        );
        String content = objectMapper.writeValueAsString(meetingRequestDto);

        // when
        ResultActions actions = mockMvc.perform(
                post("/meetings")
                        .content(content)
                        .header(JWT_HEADER, accessToken)
                        .contentType(APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("유효성 검사 실패시 400 반환")
    void meetingRegistrationFail() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(" ");

        // when
        ResultActions actions = mockMvc.perform(
                post("/meetings")
                        .content(content)
                        .header(JWT_HEADER, accessToken)
                        .contentType(APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isBadRequest());
    }

}
