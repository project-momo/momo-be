package com.example.momobe.meeting.integration;

import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.domain.Avatar;
import com.example.momobe.user.domain.User;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
public class MeetingCommand_IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EntityManager em;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @DisplayName("모임 닫기 204 반환")
    public void deleteMeeting() throws Exception {
        // given
        User host = new User(EMAIL1, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(host);
        String accessToken = jwtTokenUtil.createAccessToken(EMAIL1, host.getId(), ROLE_USER_LIST, NICKNAME1);
        Meeting meeting = generateMeeting(host.getId());
        em.persist(meeting);

        // when
        ResultActions actions = mockMvc.perform(
                delete("/meetings/{meeting-id}", meeting.getId())
                        .header(JWT_HEADER, accessToken)
        );

        // then
        actions.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("모임 닫기 hostId가 다를 경우 403 반환")
    public void deleteMeetingFail() throws Exception {
        // given
        User host = new User(EMAIL1, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(host);
        User otherUser = new User(EMAIL1, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(otherUser);
        String accessToken = jwtTokenUtil.createAccessToken(EMAIL1, otherUser.getId(), ROLE_USER_LIST, NICKNAME1);
        Meeting meeting = generateMeeting(host.getId());
        em.persist(meeting);

        // when
        ResultActions actions = mockMvc.perform(
                delete("/meetings/{meeting-id}", meeting.getId())
                        .header(JWT_HEADER, accessToken)
        );

        // then
        actions.andExpect(status().isForbidden());
    }
}
