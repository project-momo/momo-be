package com.example.momobe.meeting.dto;

import com.example.momobe.meeting.dto.in.MeetingRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.momobe.common.util.ValidatorUtil.getArray;
import static org.assertj.core.api.Assertions.assertThat;

class DateTimeDtoTest {

    @Test
    @DisplayName("DateTimeDto의 datePolicy가 null이면 예외가 발생한다.")
    void datePolicy_failed() throws Exception {
        // given
        MeetingRequestDto.DateTimeDto dateTimeDto = MeetingRequestDto.DateTimeDto.builder().build();

        // when
        Object[] arr = getArray(dateTimeDto, "datePolicy");

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("DateTimeDto의 startDate가 null이면 예외가 발생한다.")
    void startDate_failed() throws Exception {
        // given
        MeetingRequestDto.DateTimeDto dateTimeDto = MeetingRequestDto.DateTimeDto.builder().build();

        // when
        Object[] arr = getArray(dateTimeDto, "startDate");

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("DateTimeDto의 endDate가 null이면 예외가 발생한다.")
    void endDate_failed() throws Exception {
        // given
        MeetingRequestDto.DateTimeDto dateTimeDto = MeetingRequestDto.DateTimeDto.builder().build();

        // when
        Object[] arr = getArray(dateTimeDto, "endDate");

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("DateTimeDto의 startTime이 null이면 예외가 발생한다.")
    void startTime_failed() throws Exception {
        // given
        MeetingRequestDto.DateTimeDto dateTimeDto = MeetingRequestDto.DateTimeDto.builder().build();

        // when
        Object[] arr = getArray(dateTimeDto, "startTime");

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("DateTimeDto의 endTime이 null이면 예외가 발생한다.")
    void endTime_failed() throws Exception {
        // given
        MeetingRequestDto.DateTimeDto dateTimeDto = MeetingRequestDto.DateTimeDto.builder().build();

        // when
        Object[] arr = getArray(dateTimeDto, "endTime");

        // then
        assertThat(arr).isNotEmpty();
    }

}
