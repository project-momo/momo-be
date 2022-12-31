package com.example.momobe.meeting.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
        Boolean result = meeting.checkIfCanReservation(10L);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("주어진 인자값이 정원보다 크거나 같으면 false 반환 (2)")
    void checkIfCanReservation2() {
        //given
        //when
        Boolean result = meeting.checkIfCanReservation(11L);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("주어진 인자가 정원보다 작으면 true 반환 (1)")
    void checkIfCanReservation3() {
        //given
        //when
        Boolean result = meeting.checkIfCanReservation(9L);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("주어진 인자가 정원보다 작으면 true 반환 (2)")
    void checkIfCanReservation4() {
        //given
        //when
        Boolean result = meeting.checkIfCanReservation(0L);

        //then
        assertThat(result).isTrue();
    }
}