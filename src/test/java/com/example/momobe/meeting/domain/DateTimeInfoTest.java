package com.example.momobe.meeting.domain;

import com.example.momobe.meeting.domain.enums.DatePolicy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.*;
import static org.assertj.core.api.Assertions.*;

class DateTimeInfoTest {
    DateTimeInfo dateTimeInfo;

    LocalDate startDate = LocalDate.of(2022, 1, 1);
    LocalDate endDate = LocalDate.of(2022, 1, 10);
    LocalTime startTime = LocalTime.of(10, 0, 0);
    LocalTime endTime = LocalTime.of(18, 0, 0);
    int maxTime = 4;

    @BeforeEach
    void init() {
        dateTimeInfo = DateTimeInfo.builder()
                .dateTimes(null)
                .startDate(startDate)
                .endDate(endDate)
                .startTime(startTime)
                .endTime(endTime)
                .maxTime(maxTime)
                .datePolicy(DatePolicy.FREE)
                .build();
    }

    @Test
    @DisplayName("인자값이 startDate보다 빠르다면 false를 반환한다")
    void matchTest1() {
        //given
        //when
        Boolean result = dateTimeInfo.match(startDate.minus(1, DAYS), startTime, startTime.plus(1, HOURS));

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("인자값이 endDate보다 나중이라면 false를 반환한다")
    void matchTest2() {
        //given
        //when
        Boolean result = dateTimeInfo.match(endDate.plus(1, DAYS), startTime, startTime.plus(1, HOURS));

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("인자값이 startDate와 같다면 true를 반환한다.")
    void matchTest3() {
        //given
        //when
        Boolean result = dateTimeInfo.match(startDate, startTime, startTime.plus(1, HOURS));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인자값이 endDate와 같다면 true를 반환한다.")
    void matchTest4() {
        //given
        //when
        Boolean result = dateTimeInfo.match(endDate, startTime, startTime.plus(1, HOURS));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인자값이 startDate와 endDate 범위 내라면 true를 반환한다")
    void matchTest5() {
        //given
        //when
        Boolean result = dateTimeInfo.match(startDate.plus(1, DAYS), startTime, startTime.plus(1, HOURS));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인자값이 startTime보다 이른 시간이라면 False를 반환한다")
    void matchTest6() {
        //given
        //when
        Boolean result = dateTimeInfo.match(startDate.plus(1, DAYS), startTime.minus(1, HOURS), startTime.plus(1, HOURS));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인자값이 endTime보다 늦은 시간이라면 False를 반환한다")
    void matchTest7() {
        //given
        //when
        Boolean result = dateTimeInfo.match(startDate.plus(1, DAYS), endTime, endTime.plus(1, HOURS));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인자값이 startTime과 endTime 범위 내라면 true를 반환한다")
    void matchTest8() {
        //given
        //when
        Boolean result = dateTimeInfo.match(startDate.plus(1, DAYS), startTime.plus(2, HOURS), startTime.plus(4, HOURS));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인자값의 startTime과 endTime의 차가 maxTime과 같다면 true 반환한다")
    void matchTest9() {
        //given
        //when
        Boolean result = dateTimeInfo.match(startDate.plus(1, DAYS), startTime, startTime.plus(4, HOURS));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인자값의 startTime과 endTime의 차가 maxTime을 초과한다면 false를 반환한다")
    void matchTest10() {
        //given
        //when
        Boolean result = dateTimeInfo.match(startDate.plus(1, DAYS), startTime, startTime.plus(5, HOURS));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("datePolicy가 Free가 아닌 경우 시작 시간과 끝나는 시간이 정확히 일치하지 않으면 false을 반환한다(1)")
    void matchTest11() {
        //given
        ReflectionTestUtils.setField(dateTimeInfo, "datePolicy", DatePolicy.ONE_DAY);
        //when
        Boolean result = dateTimeInfo.match(startDate, startTime, endTime.minus(1, HOURS));
        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("datePolicy가 Free가 아닌 경우 시작 시간과 끝나는 시간이 정확히 일치하지 않으면 false을 반환한다(2)")
    void matchTest12() {
        //given
        ReflectionTestUtils.setField(dateTimeInfo, "datePolicy", DatePolicy.ONE_DAY);
        //when
        Boolean result = dateTimeInfo.match(startDate, startTime.plus(1, HOURS), endTime);
        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("datePolicy가 Free가 아닌 경우 시작 시간과 끝나는 시간이 정확히 일치하면 true을 반환한다(1)")
    void matchTest13() {
        //given
        ReflectionTestUtils.setField(dateTimeInfo, "datePolicy", DatePolicy.ONE_DAY);
        //when
        Boolean result = dateTimeInfo.match(startDate, startTime, endTime);
        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("datePolicy가 Free가 아닌 경우 시작 시간과 끝나는 시간이 정확히 일치하면 true을 반환한다(2)")
    void matchTest14() {
        //given
        ReflectionTestUtils.setField(dateTimeInfo, "datePolicy", DatePolicy.ONE_DAY);
        //when
        Boolean result = dateTimeInfo.match(endDate, startTime, endTime);
        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("datePolicy가 Free가 아닌 경우 시작 시간과 끝나는 시간이 정확히 일치하면 true을 반환한다(1)")
    void matchTest15() {
        //given
        ReflectionTestUtils.setField(dateTimeInfo, "datePolicy", DatePolicy.ONE_DAY);
        //when
        Boolean result = dateTimeInfo.match(endDate.minus(2, DAYS), startTime, endTime);
        //then
        assertThat(result).isTrue();
    }
}