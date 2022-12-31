package com.example.momobe.meeting.integration;

import com.example.momobe.address.domain.Address;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.domain.Avatar;
import com.example.momobe.user.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.enums.MeetingConstant.generateMeeting;
import static com.example.momobe.reservation.enums.ReservationConstants.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MeetingMyPageQuery_IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EntityManager em;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void meetingHostQuery() throws Exception {
        // given
        User user = new User(EMAIL1, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(user);

        Address address1 = Address.builder()
                .si("서울시")
                .gu("강남구")
                .build();
        Address address2 = Address.builder()
                .si("서울시")
                .gu("강북구")
                .build();
        em.persist(address1);
        em.persist(address2);
        Meeting meeting = generateMeeting(user.getId(), List.of(address1.getId(), address2.getId()));
        em.persist(meeting);
        String accessToken = jwtTokenUtil.createAccessToken(EMAIL1, user.getId(), ROLE_USER_LIST, NICKNAME1);
        em.persist(generateAcceptReservation(user.getId(), meeting.getId()));
        em.persist(generatePaymentSuccessReservation(user.getId(), meeting.getId()));
        em.persist(generateDenyReservation(user.getId(), meeting.getId()));

        // when
        ResultActions actions = mockMvc.perform(
                get("/mypage/meetings/hosts")
                        .header(JWT_HEADER, accessToken)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty());
    }

    @Test
    public void meetingParticipantQuery() throws Exception {
        // given
        User host1 = new User(EMAIL1, NICKNAME1, PASSWORD1, new Avatar(GITHUB_URL));
        User host2 = new User(EMAIL2, NICKNAME2, PASSWORD2, new Avatar(TISTORY_URL));
        User participant = new User(EMAIL, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(host1);
        em.persist(host2);
        em.persist(participant);

        Address address1 = Address.builder()
                .si("서울시")
                .gu("강남구")
                .build();
        Address address2 = Address.builder()
                .si("서울시")
                .gu("강북구")
                .build();
        em.persist(address1);
        em.persist(address2);
        Meeting meeting1 = generateMeeting(host1.getId(), List.of(address1.getId(), address2.getId()));
        Meeting meeting2 = generateMeeting(host2.getId(), List.of(address1.getId(), address2.getId()));
        em.persist(meeting1);
        em.persist(meeting2);
        String accessToken = jwtTokenUtil.createAccessToken(EMAIL1, participant.getId(), ROLE_USER_LIST, NICKNAME1);
        em.persist(generatePaymentSuccessReservation(participant.getId(), meeting1.getId()));
        em.persist(generateDenyReservation(participant.getId(), meeting2.getId()));

        // when
        ResultActions actions = mockMvc.perform(
                get("/mypage/meetings/participants")
                        .header(JWT_HEADER, accessToken)
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2));
    }

}
