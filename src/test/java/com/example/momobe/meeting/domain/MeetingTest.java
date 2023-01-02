package com.example.momobe.meeting.domain;

import com.example.momobe.meeting.domain.enums.DatePolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
        Boolean result = meeting.verifyRemainingReservations(10L);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("주어진 인자값이 정원보다 크거나 같으면 false 반환 (2)")
    void checkIfCanReservation2() {
        //given
        //when
        Boolean result = meeting.verifyRemainingReservations(11L);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("주어진 인자가 정원보다 작으면 true 반환 (1)")
    void checkIfCanReservation3() {
        //given
        //when
        Boolean result = meeting.verifyRemainingReservations(9L);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("주어진 인자가 정원보다 작으면 true 반환 (2)")
    void checkIfCanReservation4() {
        //given
        //when
        Boolean result = meeting.verifyRemainingReservations(0L);

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
}