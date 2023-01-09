package com.example.momobe.meeting.integration;

import com.example.momobe.address.domain.Address;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.tag.domain.Tag;
import com.example.momobe.user.domain.Avatar;
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

import java.util.stream.Collectors;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.enums.MeetingConstants.*;
import static com.example.momobe.meeting.enums.MeetingConstants.MEETING_REQUEST_DTO_WITH_ONE_DAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
class MeetingCommand_IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EntityManager em;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ObjectMapper objectMapper;

    private User host;
    private Meeting meeting;

    @BeforeEach
    public void init() {
        host = new User(EMAIL1, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(host);
        MEETING_REQUEST_DTO_WITH_ONE_DAY.getTags().stream()
                .filter(tag -> em.createQuery("select t.id from Tag t where t.engName = '" + tag.name() + "'", Long.class)
                        .getSingleResult() == null)
                .forEach(tag -> em.persist(new Tag(tag.getDescription(), tag.name())));
        MEETING_REQUEST_DTO_WITH_ONE_DAY.getAddress().getAddressIds().stream()
                .filter(addressId -> em.find(Address.class, addressId) == null)
                .forEach(addressId -> em.persist(Address.builder().id(addressId).si("시").gu("구").build()));
        meeting = generateMeeting(host.getId(),
                MEETING_REQUEST_DTO_WITH_ONE_DAY.getAddress().getAddressIds());
        em.persist(meeting);
    }

    @Test
    @DisplayName("모임 수정 200 반환")
    void updateMeeting() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(MEETING_UPDATE_DTO);
        String accessToken = jwtTokenUtil.createAccessToken(EMAIL1, host.getId(), ROLE_USER_LIST, NICKNAME1);

        // when
        ResultActions actions = mockMvc.perform(
                patch("/meetings/{meeting-id}", meeting.getId())
                        .header(JWT_HEADER, accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("모임 수정 hostId가 다를 경우 403 반환")
    void updateMeetingFail() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(MEETING_UPDATE_DTO);
        User otherUser = new User(EMAIL1, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(otherUser);
        String accessToken = jwtTokenUtil.createAccessToken(EMAIL1, otherUser.getId(), ROLE_USER_LIST, NICKNAME1);

        // when
        ResultActions actions = mockMvc.perform(
                patch("/meetings/{meeting-id}", meeting.getId())
                        .header(JWT_HEADER, accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("모임 닫기 204 반환")
    void deleteMeeting() throws Exception {
        // given
        String accessToken = jwtTokenUtil.createAccessToken(EMAIL1, host.getId(), ROLE_USER_LIST, NICKNAME1);

        // when
        ResultActions actions = mockMvc.perform(
                delete("/meetings/{meeting-id}", meeting.getId())
                        .header(JWT_HEADER, accessToken)
        );
        em.flush();
        em.clear();
        Meeting savedMeeting = em.find(Meeting.class, meeting.getId());

        // then
        actions.andExpect(status().isNoContent());
        assertThat(savedMeeting.getMeetingState()).isEqualTo(MeetingState.CLOSE);
    }

    @Test
    @DisplayName("모임 닫기 hostId가 다를 경우 403 반환")
    void deleteMeetingFail() throws Exception {
        // given
        User otherUser = new User(EMAIL1, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(otherUser);
        String accessToken = jwtTokenUtil.createAccessToken(EMAIL1, otherUser.getId(), ROLE_USER_LIST, NICKNAME1);

        // when
        ResultActions actions = mockMvc.perform(
                delete("/meetings/{meeting-id}", meeting.getId())
                        .header(JWT_HEADER, accessToken)
        );

        // then
        actions.andExpect(status().isForbidden());
    }
}
