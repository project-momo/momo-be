package com.example.momobe.meeting.integration;

import com.example.momobe.common.config.RedisTestConfig;
import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingRankingStore;
import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.meeting.dto.out.MeetingRankDto;
import com.example.momobe.meeting.ui.MeetingQueryController;
import com.example.momobe.user.domain.*;
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

import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
public class MeetingRank_IntegrationTest extends RedisTestConfig {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    @Autowired
    MeetingQueryController meetingQueryController;

    @Autowired
    MeetingRankingStore<MeetingRankDto> meetingRankingStore;

    Meeting meeting1;
    Meeting meeting2;

    @BeforeEach
    void init () {
        meetingRankingStore.deleteAll();

        User user = User.builder()
                .email(new Email(EMAIL1))
                .password(new Password(PASSWORD1))
                .nickname(new Nickname(NICKNAME1))
                .avatar(new Avatar(TISTORY_URL))
                .build();

        entityManager.persist(user);

        meeting1 = Meeting.builder()
                .hostId(ID1)
                .dateTimeInfo(DateTimeInfo.builder()
                        .endDate(LocalDate.of(2022, 01, 20))
                        .startDate(LocalDate.of(2022, 01, 01))
                        .startTime(LocalTime.of(10, 0, 0))
                        .endTime(LocalTime.of(22, 0, 0))
                        .datePolicy(DatePolicy.FREE)
                        .maxTime(4)
                        .build())
                .title(TITLE1)
                .meetingState(MeetingState.OPEN)
                .address(null)
                .category(Category.AI)
                .price(10000L)
                .content(CONTENT1)
                .personnel(10)
                .build();

        meeting2 = Meeting.builder()
                .hostId(ID2)
                .dateTimeInfo(DateTimeInfo.builder()
                        .endDate(LocalDate.of(2022, 01, 20))
                        .startDate(LocalDate.of(2022, 01, 01))
                        .startTime(LocalTime.of(10, 0, 0))
                        .endTime(LocalTime.of(22, 0, 0))
                        .datePolicy(DatePolicy.FREE)
                        .maxTime(4)
                        .build())
                .title(TITLE1)
                .meetingState(MeetingState.OPEN)
                .address(null)
                .category(Category.AI)
                .price(10000L)
                .content(CONTENT1)
                .personnel(10)
                .build();

        entityManager.persist(meeting1);
        entityManager.persist(meeting2);
    }

    @Test
    @DisplayName("?????? ????????? ???????????? 0??? ??? ???????????? ???????????? ??????")
    void rankTest1() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(get("/ranks"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("meeting1??? ???????????? 1??? ??? meeting1??? ?????? ????????? ?????????")
    void rankTest2() throws Exception {
        //given
        meetingQueryController.meetingDetailQuery(meeting1.getId());

        //when
        ResultActions result = mockMvc.perform(get("/ranks"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].meetingId").value(meeting1.getId()));
    }

    @Test
    @DisplayName("meeting2??? ???????????? 2, meeting1??? ???????????? 1??? ??? 0?????? ????????? meeting2, 1?????? ????????? meeting1??? ????????? ?????????")
    void rankTest3() throws Exception {
        //given
        meetingQueryController.meetingDetailQuery(meeting1.getId());
        meetingQueryController.meetingDetailQuery(meeting2.getId());
        meetingQueryController.meetingDetailQuery(meeting2.getId());

        //when
        ResultActions result = mockMvc.perform(get("/ranks"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].meetingId").value(meeting2.getId()))
                .andExpect(jsonPath("$[1].meetingId").value(meeting1.getId()));
    }
}
