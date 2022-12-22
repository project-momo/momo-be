package com.example.momobe.meeting.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.momobe.common.util.ValidatorUtil.getArray;
import static org.assertj.core.api.Assertions.assertThat;

public class PriceDtoTest {

    @Test
    @DisplayName("PriceDto의 pricePolicy가 null이면 예외가 발생한다.")
    public void pricePolicy_failed() throws Exception {
        // given
        MeetingRequestDto.PriceDto priceDto = MeetingRequestDto.PriceDto.builder().build();

        // when
        Object[] arr = getArray(priceDto, "pricePolicy");

        // then
        assertThat(arr).isNotEmpty();
    }

    @Test
    @DisplayName("PriceDto의 price가 null이면 예외가 발생한다.")
    public void price_failed() throws Exception {
        // given
        MeetingRequestDto.PriceDto priceDto = MeetingRequestDto.PriceDto.builder().build();

        // when
        Object[] arr = getArray(priceDto, "price");

        // then
        assertThat(arr).isNotEmpty();
    }

}
