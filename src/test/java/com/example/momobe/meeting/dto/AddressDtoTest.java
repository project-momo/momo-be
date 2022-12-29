package com.example.momobe.meeting.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.example.momobe.common.util.ValidatorUtil.getArray;
import static org.assertj.core.api.Assertions.assertThat;

public class AddressDtoTest {

    @Test
    @DisplayName("LocationDto의 addressIds.size()가 1 미만이면 예외가 발생한다.")
    public void addressIds_failed_1() throws Exception {
        // given
        MeetingRequestDto.AddressDto addressDto = 
                MeetingRequestDto.AddressDto.builder().addressIds(List.of()).build();

        // when
        Object[] arr = getArray(addressDto, "addressIds");

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("LocationDto의 addressIds가 null이면 예외가 발생한다.")
    public void addressIds_failed_2() throws Exception {
        // given
        MeetingRequestDto.AddressDto addressDto = MeetingRequestDto.AddressDto.builder().build();

        // when
        Object[] arr = getArray(addressDto, "addressIds");

        // then
        assertThat(arr).isNotEmpty();
    }

}
