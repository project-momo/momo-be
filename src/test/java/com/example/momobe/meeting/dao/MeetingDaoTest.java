package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.DateTimeInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.dto.in.MeetingRequestDto;
import com.example.momobe.meeting.dto.out.ResponseMeetingDatesDto;
import com.example.momobe.meeting.mapper.DateTimeMapper;
import com.example.momobe.meeting.mapper.DateTimeMapperImpl;
import com.example.momobe.reservation.domain.Money;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.reservation.domain.ReservationDate;
import com.example.momobe.reservation.domain.enums.ReservationState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.domain.enums.Category.*;
import static com.example.momobe.meeting.domain.enums.DatePolicy.*;
import static com.example.momobe.meeting.domain.enums.DatePolicy.PERIOD;
import static com.example.momobe.meeting.domain.enums.MeetingState.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MeetingDaoTest {
    @Autowired
    private MeetingDao meetingDao;

    private DateTimeMapper dateTimeMapper;

    @Autowired
    private EntityManager em;

    private Meeting freeMeeting;
    private Meeting dayMeeting;

    private LocalDate startDate;

    private List<Reservation> freeReservations = new ArrayList<>();
    private List<Reservation> dayReservations = new ArrayList<>();

    @BeforeEach
    void init() {
        dateTimeMapper = new DateTimeMapperImpl();

        List<LocalDate> localDates = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            localDates.add(LocalDate.of(2022,1,i));
        }

        startDate = LocalDate.of(2022, 1, 1);
        MeetingRequestDto.DateTimeDto free = MeetingRequestDto.DateTimeDto.builder()
                .datePolicy(FREE)
                .maxTime(4)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(14, 0))
                .startDate(startDate)
                .endDate(LocalDate.of(2022, 1, 20))
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
                        .endTime(LocalTime.of(22, 0))
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
                .startDate(LocalDate.of(2022, 1, 1))
                .endDate(LocalDate.of(2022, 1, 20))
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
                        .startDate(LocalDate.of(2022, 1, 1))
                        .endDate(LocalDate.of(2022, 1, 20))
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
                            .date(LocalDate.of(2022, 1, i))
                            .startTime(LocalTime.of(10, 0))
                            .endTime(LocalTime.of(12, 0))
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
                            .date(LocalDate.of(2022, 1, i))
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
    @DisplayName("10시부터 2시까지 20일간의 일정을 생성했을 때, 4시간 * 20일 총 80개의 결과를 반환한다.")
    void getTimesTest_free1() {
        //given
        //when
        List<ResponseMeetingDatesDto> result = meetingDao.getMonthlyReservationSchedule(freeMeeting.getId(), startDate.getMonthValue());

        //then
        for (ResponseMeetingDatesDto t : result) {
            System.out.println(t);
        }
        assertThat(result.size()).isEqualTo(80);
    }

    @Test
    @DisplayName("정원이 가득찬 경우 해당 시간에 예약 가능 여부는 false를 반환한다.")
    void getTimesTest_day2() {
        //given
        //when
        List<ResponseMeetingDatesDto> result = meetingDao.getMonthlyReservationSchedule(freeMeeting.getId(), startDate.getMonthValue());

        //then
        for (ResponseMeetingDatesDto responseMeetingDatesDto : result) {
            if (responseMeetingDatesDto.getTime().isAfter(LocalTime.of(9,59)) && responseMeetingDatesDto.getTime().isBefore(LocalTime.of(12,0))) {
                assertThat(responseMeetingDatesDto.getCurrentStaff()).isEqualTo(1);
                assertThat(responseMeetingDatesDto.getAvailability()).isEqualTo("false");
            } else {
                assertThat(responseMeetingDatesDto.getCurrentStaff()).isEqualTo(0);
                assertThat(responseMeetingDatesDto.getAvailability()).isEqualTo("true");
            }
        }
    }

    @Test
    @DisplayName("20일간의 일정을 생성했을 때, day meeting은 1일 1회 이루어지므로 총 20개의 결과를 반환한다.")
    void getTimesTest_day1() {
        //given
        //when
        List<ResponseMeetingDatesDto> result = meetingDao.getMonthlyReservationSchedule(dayMeeting.getId(), startDate.getMonthValue());

        //then
        assertThat(result.size()).isEqualTo(20);
    }

    @Test
    @DisplayName("모든 날짜에 예약자가 존재해도 정원이 가득차지 않으면 예약 가능 여부 true를 반환한다.")
    void getTimesTest_free2() {
        //given
        //when
        List<ResponseMeetingDatesDto> result = meetingDao.getMonthlyReservationSchedule(dayMeeting.getId(), startDate.getMonthValue());

        //then
        for (ResponseMeetingDatesDto responseMeetingDatesDto : result) {
            assertThat(responseMeetingDatesDto.getCurrentStaff()).isEqualTo(1);
            assertThat(responseMeetingDatesDto.getAvailability()).isEqualTo("true");
        }
    }
}