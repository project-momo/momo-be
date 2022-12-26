package com.example.momobe.meeting.enums;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.PricePolicy;
import com.example.momobe.meeting.domain.enums.Tag;
import com.example.momobe.meeting.dto.MeetingRequestDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;

public class MeetingConstant {
    public static final MeetingRequestDto.PriceDto PRICE_DTO = MeetingRequestDto.PriceDto.builder()
            .pricePolicy(PricePolicy.HOUR)
            .price(1000L)
            .build();
    public static final MeetingRequestDto.LocationDto LOCATION_DTO = MeetingRequestDto.LocationDto.builder()
            .address1(ADDRESS1)
            .address2(ADDRESS2)
            .build();
    public static final MeetingRequestDto.DateTimeDto DATE_TIME_DTO = MeetingRequestDto.DateTimeDto.builder()
            .datePolicy(DatePolicy.FREE)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(10))
            .startTime(LocalTime.of(2, 0))
            .endTime(LocalTime.of(5, 0))
            .dates(List.of(LocalDate.now(), LocalDate.now().plusDays(2),
                    LocalDate.now().plusDays(5), LocalDate.now().plusDays(10)))
            .build();

    public static final MeetingRequestDto MEETING_REQUEST_DTO = MeetingRequestDto.builder()
            .category(Category.MENTORING)
            .title(TITLE1)
            .content(CONTENT1)
            .tags(List.of(Tag.LIFESTYLE, Tag.MEDIA, Tag.EDU))
            .priceInfo(PRICE_DTO)
            .locations(List.of(LOCATION_DTO))
            .dateTime(DATE_TIME_DTO)
            .notice("전달 사항")
            .build();
}
