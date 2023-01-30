package com.example.momobe.meeting.integration;

import com.example.momobe.meeting.dao.MonthlyMeetingScheduleInquiry;
import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.dto.in.MeetingRequestDto;
import com.example.momobe.meeting.mapper.DateTimeMapper;
import com.example.momobe.meeting.mapper.DateTimeMapperImpl;
import com.example.momobe.reservation.domain.Money;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.reservation.domain.ReservationDate;
import com.example.momobe.reservation.domain.enums.ReservationState;
import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.domain.Avatar;
import com.example.momobe.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.common.enums.TestConstants.ID1;
import static com.example.momobe.meeting.domain.enums.Category.AI;
import static com.example.momobe.meeting.domain.enums.Category.DESIGN;
import static com.example.momobe.meeting.domain.enums.DatePolicy.FREE;
import static com.example.momobe.meeting.domain.enums.DatePolicy.PERIOD;
import static com.example.momobe.meeting.domain.enums.MeetingState.OPEN;
import static java.time.temporal.ChronoUnit.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MeetingDatesQuery_IntegrationTest {
    @Autowired
    private MonthlyMeetingScheduleInquiry monthlyMeetingScheduleInquiry;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mockMvc;

    private DateTimeMapper dateTimeMapper;
    private Meeting freeMeeting;
    private Meeting dayMeeting;
    private LocalDate startDate;
    private List<Reservation> freeReservations = new ArrayList<>();
    private List<Reservation> dayReservations = new ArrayList<>();

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    String accessToken;

    @BeforeEach
    void init() {
        User user = new User(EMAIL1, NICKNAME, PASSWORD1, new Avatar(REMOTE_PATH));
        em.persist(user);

        accessToken = jwtTokenUtil.createAccessToken(EMAIL1, user.getId(), ROLE_USER_LIST, NICKNAME1);

        dateTimeMapper = new DateTimeMapperImpl();
        List<LocalDate> localDates = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            localDates.add(LocalDate.of(2022,1,i));
        }

        startDate = LocalDate.now();
        MeetingRequestDto.DateTimeDto free = MeetingRequestDto.DateTimeDto.builder()
                .datePolicy(FREE)
                .maxTime(4)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(14, 0))
                .startDate(startDate)
                .endDate(LocalDate.now().plus(7, DAYS))
                .dayWeeks(Set.of(1,2,3,4,5,6,7))
                .dates(localDates)
                .build();
        List<DateTime> dateTimes = dateTimeMapper.toDateTime(free);

        freeMeeting = Meeting.builder()
                .meetingState(OPEN)
                .address(null)
                .content(CONTENT1)
                .price(10000L)
                .personnel(1)
                .category(AI)
                .title(TITLE1)
                .hostId(ID1)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(FREE)
                        .maxTime(4)
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(14, 0))
                        .startDate(startDate)
                        .endDate(LocalDate.of(2022, 1, 20))
                        .dateTimes(dateTimes)
                        .build())
                .tagIds(null)
                .build();

        em.persist(freeMeeting);

        MeetingRequestDto.DateTimeDto date = MeetingRequestDto.DateTimeDto.builder()
                .datePolicy(PERIOD)
                .maxTime(4)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(14, 0))
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plus(7, DAYS))
                .dayWeeks(Set.of(1,2,3,4,5,6,7))
                .dates(localDates)
                .build();
        List<DateTime> dateTimes2 = dateTimeMapper.toDateTime(date);

        dayMeeting = Meeting.builder()
                .meetingState(OPEN)
                .address(null)
                .content(CONTENT2)
                .price(10000L)
                .personnel(4)
                .category(DESIGN)
                .title(TITLE2)
                .hostId(ID1)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(PERIOD)
                        .maxTime(4)
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(15, 0))
                        .startDate(startDate)
                        .endDate(LocalDate.now().plus(7, DAYS))
                        .dateTimes(dateTimes2)
                        .build())
                .tagIds(null)
                .build();

        em.persist(dayMeeting);

        for (int i=1; i<=20; i++) {
            Reservation reservation = Reservation.builder()
                    .meetingId(freeMeeting.getId())
                    .reservationMemo(null)
                    .reservationDate(ReservationDate.builder()
                            .date(startDate)
                            .startTime(LocalTime.of(10, 0))
                            .endTime(LocalTime.of(15, 0))
                            .build())
                    .amount(new Money(10000L))
                    .reservationState(ReservationState.ACCEPT)
                    .build();

            freeReservations.add(reservation);
            em.persist(reservation);
        }

        for (int i=1; i<=20; i++) {
            Reservation reservation = Reservation.builder()
                    .meetingId(dayMeeting.getId())
                    .reservationMemo(null)
                    .reservationDate(ReservationDate.builder()
                            .date(startDate)
                            .startTime(LocalTime.of(10, 0))
                            .endTime(LocalTime.of(15, 0))
                            .build())
                    .amount(new Money(10000L))
                    .reservationState(ReservationState.PAYMENT_SUCCESS)
                    .build();

            dayReservations.add(reservation);
            em.persist(reservation);
        }
    }

    @Test
    @DisplayName("datePolicy가 period일 때, 정원이 가득 차지 않으면 availability는 true를 반환한다.")
    void test1() throws Exception {
        //given
        //when
        ResultActions perform = mockMvc.perform(get("/meetings/{meetingId}/reservations/dates/{date}", dayMeeting.getId(), startDate)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currentStaff").value(1))
                .andExpect(jsonPath("$[1].currentStaff").value(1))
                .andExpect(jsonPath("$[2].currentStaff").value(1))
                .andExpect(jsonPath("$[3].currentStaff").value(1))
                .andExpect(jsonPath("$[4].currentStaff").value(1))
                .andExpect(jsonPath("$[5].currentStaff").value(1))
                .andExpect(jsonPath("$[7].currentStaff").value(1))
                .andExpect(jsonPath("$[8].currentStaff").value(1))
                .andExpect(jsonPath("$[9].currentStaff").value(1))
                .andExpect(jsonPath("$[10].currentStaff").value(1))
                .andExpect(jsonPath("$[1].availability").value("true"))
                .andExpect(jsonPath("$[2].availability").value("true"))
                .andExpect(jsonPath("$[3].availability").value("true"))
                .andExpect(jsonPath("$[4].availability").value("true"))
                .andExpect(jsonPath("$[5].availability").value("true"))
                .andExpect(jsonPath("$[6].availability").value("true"))
                .andExpect(jsonPath("$[7].availability").value("true"))
                .andExpect(jsonPath("$[8].availability").value("true"))
                .andExpect(jsonPath("$[9].availability").value("true"))
                .andExpect(jsonPath("$[10].availability").value("true"))
                .andDo(print());
    }

    @Test
    @DisplayName("datePolicy가 free일 때, 해당 시간에 예약이 있다면 false를 반환한다")
    void test2() throws Exception {
        //given
        //when
        ResultActions perform = mockMvc.perform(get("/meetings/{meetingId}/reservations/dates/{date}", freeMeeting.getId(), startDate)
                .header(JWT_HEADER, accessToken));

        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currentStaff").value(1))
                .andExpect(jsonPath("$[1].currentStaff").value(1))
                .andExpect(jsonPath("$[2].currentStaff").value(0))
                .andExpect(jsonPath("$[3].currentStaff").value(0))
                .andExpect(jsonPath("$[4].currentStaff").value(1))
                .andExpect(jsonPath("$[5].currentStaff").value(1))
                .andExpect(jsonPath("$[6].currentStaff").value(0))
                .andExpect(jsonPath("$[7].currentStaff").value(0))
                .andExpect(jsonPath("$[8].currentStaff").value(1))
                .andExpect(jsonPath("$[9].currentStaff").value(1))
                .andExpect(jsonPath("$[10].currentStaff").value(0))
                .andExpect(jsonPath("$[0].availability").value("false"))
                .andExpect(jsonPath("$[1].availability").value("false"))
                .andExpect(jsonPath("$[2].availability").value("true"))
                .andExpect(jsonPath("$[3].availability").value("true"))
                .andExpect(jsonPath("$[4].availability").value("false"))
                .andExpect(jsonPath("$[5].availability").value("false"))
                .andExpect(jsonPath("$[6].availability").value("true"))
                .andExpect(jsonPath("$[7].availability").value("true"))
                .andExpect(jsonPath("$[8].availability").value("false"))
                .andExpect(jsonPath("$[9].availability").value("false"))
                .andExpect(jsonPath("$[10].availability").value("true"))
                .andDo(print());
    }
}
