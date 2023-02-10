package com.example.momobe.meeting.domain;

import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.MeetingState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;

class MeetingTest {
    Meeting meeting;

    @BeforeEach
    void init() {
        meeting = Meeting.builder()
                .personnel(10)
                .dateTimeInfo(DateTimeInfo.builder()
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .build();
    }

    @Test
    @DisplayName("주어진 인자값이 정원보다 크거나 같으면 false 반환 (1)")
    void checkIfCanReservation1() {
        //given
        //when
        Boolean result = meeting.hasRemainingReservations(10L);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("주어진 인자값이 정원보다 크거나 같으면 false 반환 (2)")
    void checkIfCanReservation2() {
        //given
        //when
        Boolean result = meeting.hasRemainingReservations(11L);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("주어진 인자가 정원보다 작으면 true 반환 (1)")
    void checkIfCanReservation3() {
        //given
        //when
        Boolean result = meeting.hasRemainingReservations(9L);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("주어진 인자가 정원보다 작으면 true 반환 (2)")
    void checkIfCanReservation4() {
        //given
        //when
        Boolean result = meeting.hasRemainingReservations(0L);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("datePolicy가 free일 때, 인자로 같은 금액과 1시간이 들어오면 true 반환한다")
    void matchAmountTest1() {
        //given
        Meeting meeting = Meeting.builder()
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(DatePolicy.FREE)
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .build();

        //when
        Boolean result = meeting.matchPrice(meeting.getPrice(), LocalTime.of(10, 0), LocalTime.of(11, 0));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("datePolicy가 free일 때, 인자로 같은 금액과 2시간이 들어오면 false 반환한다")
    void matchAmountTest2() {
        //given
        Meeting meeting = Meeting.builder()
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(DatePolicy.FREE)
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .build();

        //when
        Boolean result = meeting.matchPrice(meeting.getPrice(), LocalTime.of(10, 0), LocalTime.of(12, 0));

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("datePolicy가 free가 아닐 때, 기간 관계 없이 금액만 같으면 true를 반환한다.")
    void matchAmountTest3() {
        //given
        Meeting meeting = Meeting.builder()
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(DatePolicy.ONE_DAY)
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .build();

        //when
        Boolean result = meeting.matchPrice(meeting.getPrice(), LocalTime.of(10, 0), LocalTime.of(15, 0));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("datePolicy가 free가 아닐 때, 기간 관계 없이 금액이 다르면 false를 반환한다.")
    void matchAmountTest4() {
        //given
        Meeting meeting = Meeting.builder()
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(DatePolicy.ONE_DAY)
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .build();

        //when
        Boolean result = meeting.matchPrice(meeting.getPrice() - 500, LocalTime.of(10, 0), LocalTime.of(15, 0));

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("datePolicy가 free일 때, 인자로 2배의 금액과 2시간이 들어오면 true 반환한다")
    void matchAmountTest5() {
        //given
        Meeting meeting = Meeting.builder()
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(DatePolicy.FREE)
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .build();

        //when
        Boolean result = meeting.matchPrice(meeting.getPrice() * 2, LocalTime.of(10, 0), LocalTime.of(12, 0));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("datePolicy가 free일 때, 인자로 3배의 금액과 3시간이 들어오면 true 반환한다")
    void matchAmountTest6() {
        //given
        Meeting meeting = Meeting.builder()
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(DatePolicy.FREE)
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .build();

        //when
        Boolean result = meeting.matchPrice(meeting.getPrice() * 2, LocalTime.of(10, 0), LocalTime.of(12, 0));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("meeting의 state가 closed가 아니라면 false를 반환한다")
    void isClosedTest1() {
        //given
        Meeting meeting = Meeting.builder()
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(DatePolicy.FREE)
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .meetingState(MeetingState.OPEN)
                .build();

        //when
        Boolean result = meeting.isClosed();

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("meeting의 state가 closed라면 true를 반환한다")
    void isClosedTest2() {
        //given
        Meeting meeting = Meeting.builder()
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(DatePolicy.FREE)
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .meetingState(MeetingState.CLOSE)
                .build();

        //when
        Boolean result = meeting.isClosed();

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("hostId가 동일하지 않다면 false를 반환한다.")
    void matchHostIdTest1() {
        //given
        Meeting meeting = Meeting.builder()
                .hostId(ID1)
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(DatePolicy.FREE)
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .meetingState(MeetingState.CLOSE)
                .build();

        //when
        Boolean result = meeting.matchHostId(ID2);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("hostId가 동일하다면 true를 반환한다.")
    void matchHostIdTest2() {
        //given
        Meeting meeting = Meeting.builder()
                .hostId(ID1)
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(DatePolicy.FREE)
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .meetingState(MeetingState.CLOSE)
                .build();

        //when
        Boolean result = meeting.matchHostId(ID1);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("해당 HostId가 null이라면 false를 반환한다.")
    void matchHostIdTest3() {
        //given
        Meeting meeting = Meeting.builder()
                .hostId(ID1)
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(DatePolicy.FREE)
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .meetingState(MeetingState.CLOSE)
                .build();

        //when
        Boolean result = meeting.matchHostId(null);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Meeting 객체의 HostId가 null이라면 false를 반환한다.")
    void matchHostIdTest4() {
        //given
        Meeting meeting = Meeting.builder()
                .hostId(null)
                .price(10000L)
                .dateTimeInfo(DateTimeInfo.builder()
                        .datePolicy(DatePolicy.FREE)
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .meetingState(MeetingState.CLOSE)
                .build();

        //when
        Boolean result = meeting.matchHostId(ID1);

        //then
        assertThat(result).isFalse();
    }
}