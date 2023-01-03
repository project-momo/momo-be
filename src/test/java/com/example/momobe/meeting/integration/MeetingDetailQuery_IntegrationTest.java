package com.example.momobe.meeting.integration;


import com.example.momobe.address.domain.Address;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.user.domain.Avatar;
import com.example.momobe.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.enums.MeetingConstants.generateMeeting;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
public class MeetingDetailQuery_IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EntityManager em;

    @Test
    public void meetingDetailQuery() throws Exception {
        // given
        User host = new User(EMAIL1, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(host);
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
        Meeting meeting = generateMeeting(host.getId(), List.of(address1.getId(), address2.getId()));
        em.persist(meeting);

        // when
        ResultActions actions = mockMvc.perform(
                get("/meetings/{meeting-id}", meeting.getId()));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.meetingId").value(meeting.getId()))
                .andExpect(jsonPath("$.category").value(meeting.getCategory().getDescription()))
                .andExpect(jsonPath("$.host.userId").value(host.getId()))
                .andExpect(jsonPath("$.host.nickname").value(host.getNickname().getNickname()))
                .andExpect(jsonPath("$.host.imageUrl").value(host.getAvatar().getRemotePath()))
                .andExpect(jsonPath("$.title").value(meeting.getTitle()))
                .andExpect(jsonPath("$.content").value(meeting.getContent()))
                .andExpect(jsonPath("$.address.addresses[0]").value(address1.getSi() + " " + address1.getGu()))
                .andExpect(jsonPath("$.address.addresses[1]").value(address2.getSi() + " " + address2.getGu()))
                .andExpect(jsonPath("$.address.addressInfo").value(meeting.getAddress().getAddressInfo()))
                .andExpect(jsonPath("$.meetingState").value(meeting.getMeetingState().getDescription()))
                .andExpect(jsonPath("$.isOpen").value(meeting.getMeetingState() == MeetingState.OPEN))
                .andExpect(jsonPath("$.dateTime.datePolicy").value(meeting.getDateTimeInfo().getDatePolicy().toString()))
                .andExpect(jsonPath("$.dateTime.startDate").value(meeting.getDateTimeInfo().getStartDate().toString()))
                .andExpect(jsonPath("$.dateTime.endDate").value(meeting.getDateTimeInfo().getEndDate().toString()))
                .andExpect(jsonPath("$.dateTime.startTime").value(meeting.getDateTimeInfo().getStartTime().format(DateTimeFormatter.ISO_LOCAL_TIME)))
                .andExpect(jsonPath("$.dateTime.endTime").value(meeting.getDateTimeInfo().getEndTime().format(DateTimeFormatter.ISO_LOCAL_TIME)))
                .andExpect(jsonPath("$.dateTime.maxTime").value(meeting.getDateTimeInfo().getMaxTime()))
                .andExpect(jsonPath("$.price").value(meeting.getPrice()));
    }

}
