package com.example.momobe.reservation.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

class ReservationDateTest {
    ReservationDate reservationDate;

    @BeforeEach
    void init()  {
        reservationDate = ReservationDate.builder()
                .date(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plus(1, ChronoUnit.HOURS))
                .build();
    }

    @Test
    @DisplayName("인자의 날짜가 나중이라면 시간과 관계 없이 true 반환(1)")
    void isBeforeThen1() {
        //given
        LocalDateTime dateTime = reservationDate.getStartDateTime().plus(1, ChronoUnit.DAYS);

        //when
        boolean result = reservationDate.isBeforeThen(dateTime);

        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인자의 날짜가 나중이라면 시간과 관계 없이 true 반환(1)")
    void isBeforeThen2() {
        //given
        LocalDate day = reservationDate.getDate().plus(1, ChronoUnit.DAYS);
        LocalTime time = reservationDate.getStartTime();
        LocalDateTime dateTime = LocalDateTime.of(day, time);

        //when
        boolean result = reservationDate.isBeforeThen(dateTime);

        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인자의 날짜가 같고, 시간이 startTime 이후라면 true 반환")
    void isBeforeThen3() {
        //given
        LocalDate day = reservationDate.getDate();
        LocalTime time = reservationDate.getStartTime().plus(1, ChronoUnit.MINUTES);
        LocalDateTime dateTime = LocalDateTime.of(day, time);

        //when
        boolean result = reservationDate.isBeforeThen(dateTime);

        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인자의 날짜가 같고, 시간이 startTime 이전이라면 false 반환")
    void isBeforeThen4() {
        //given
        LocalDate day = reservationDate.getDate();
        LocalTime time = reservationDate.getStartTime().minus(1, ChronoUnit.MINUTES);
        LocalDateTime dateTime = LocalDateTime.of(day, time);

        //when
        boolean result = reservationDate.isBeforeThen(dateTime);

        //then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("인자의 날짜가 이전이라면 시간과 무관하게 false 반환(1)")
    void isBeforeThen5() {
        //given
        LocalDateTime dateTime = reservationDate.getEndDateTime();

        //when
        boolean result = reservationDate.isBeforeThen(dateTime.minus(1, ChronoUnit.DAYS));

        //then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("인자의 날짜가 이전이라면 시간과 무관하게 false 반환(2)")
    void isBeforeThen6() {
        //given
        LocalDate day = reservationDate.getDate().minus(1, ChronoUnit.DAYS);
        LocalTime time = reservationDate.getStartTime().plus(1, ChronoUnit.MINUTES);
        LocalDateTime dateTime = LocalDateTime.of(day, time);

        //when
        boolean result = reservationDate.isBeforeThen(dateTime);

        //then
        Assertions.assertThat(result).isFalse();
    }
}