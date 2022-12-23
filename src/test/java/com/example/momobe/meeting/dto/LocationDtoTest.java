package com.example.momobe.meeting.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.momobe.common.util.ValidatorUtil.getArray;
import static org.assertj.core.api.Assertions.assertThat;

public class LocationDtoTest {
    private final String DATE_TIMES = "dateTimes";

    @Test
    @DisplayName("LocationDto의 address1이 null이면 예외가 발생한다.")
    public void address1_failed() throws Exception {
        // given
        MeetingRequestDto.LocationDto locationDto = MeetingRequestDto.LocationDto.builder().build();

        // when
        Object[] arr = getArray(locationDto, "address1");

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("LocationDto의 dateTimes.size()가 1 미만이면 예외가 발생한다.")
    public void dateTimes_failed_1() throws Exception {
        // given
        MeetingRequestDto.LocationDto locationDto =
                MeetingRequestDto.LocationDto.builder().dateTimes(List.of()).build();

        // when
        Object[] arr = getArray(locationDto, DATE_TIMES);

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("LocationDto의 dateTimes가 null이면 예외가 발생한다.")
    public void dateTimes_failed_2() throws Exception {
        // given
        MeetingRequestDto.LocationDto locationDto =
                MeetingRequestDto.LocationDto.builder().build();

        // when
        Object[] arr = getArray(locationDto, DATE_TIMES);

        // then
        assertThat(arr).isNotEmpty();
    }

}
