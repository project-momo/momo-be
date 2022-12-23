package com.example.momobe.meeting.enums;

import com.example.momobe.meeting.domain.PricePolicy;
import com.example.momobe.meeting.dto.MeetingRequestDto;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static com.example.momobe.common.enums.TestConstants.*;

public class MeetingConstant {
    public static final MeetingRequestDto.PriceDto PRICE_DTO = MeetingRequestDto.PriceDto.builder()
            .pricePolicy(PricePolicy.HOUR)
            .price(1000L)
            .build();
    public static final MeetingRequestDto.LocationDto LOCATION_DTO = MeetingRequestDto.LocationDto.builder()
            .address1(ADDRESS1)
            .address2(ADDRESS2)
            .dateTimes(List.of(NOW_TIME, NOW_TIME.plus(1L, ChronoUnit.HOURS)))
            .build();
    public static final MeetingRequestDto MEETING_REQUEST_DTO = MeetingRequestDto.builder()
            .categoryId(ID1)
            .title(TITLE1)
            .content(CONTENT1)
            .tagIds(Set.of(ID1, ID2))
            .priceInfo(PRICE_DTO)
            .locations(List.of(LOCATION_DTO))
            .notice("전달 사항")
            .build();
}
