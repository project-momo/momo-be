package com.example.momobe.meeting.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.momobe.common.util.ValidatorUtil.getArray;
import static org.assertj.core.api.Assertions.assertThat;

public class LocationDtoTest {

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

}
