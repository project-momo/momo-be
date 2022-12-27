package com.example.momobe.meeting.enums;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.PricePolicy;
import com.example.momobe.meeting.domain.enums.Tag;
import com.example.momobe.meeting.dto.MeetingRequestDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;

public class MeetingConstant {
    public static final LocalTime START_TIME = LocalTime.of(1, 0);
    public static final LocalTime END_TIME = LocalTime.of(5, 0);

    public static final MeetingRequestDto.PriceDto PRICE_DTO = MeetingRequestDto.PriceDto.builder()
            .pricePolicy(PricePolicy.HOUR)
            .price(1000L)
            .build();
    public static final MeetingRequestDto.LocationDto LOCATION_DTO1 = MeetingRequestDto.LocationDto.builder()
            .address1(ADDRESS1)
            .address2(SUB_ADDRESS1)
            .build();
    public static final MeetingRequestDto.LocationDto LOCATION_DTO2 = MeetingRequestDto.LocationDto.builder()
            .address1(ADDRESS2)
            .address2(SUB_ADDRESS2)
            .build();

    public static final MeetingRequestDto.DateTimeDto DATE_TIME_DTO_WITH_ONE_DAY = MeetingRequestDto.DateTimeDto.builder()
            .datePolicy(DatePolicy.ONE_DAY)
            .startDate(LocalDate.now())
            .startTime(START_TIME)
            .endTime(END_TIME)
            .build();

    public static final MeetingRequestDto MEETING_REQUEST_DTO_WITH_ONE_DAY = MeetingRequestDto.builder()
            .category(Category.MENTORING)
            .title(TITLE1)
            .content(CONTENT1)
            .tags(List.of(Tag.LIFESTYLE, Tag.MEDIA, Tag.EDU))
            .priceInfo(PRICE_DTO)
            .locations(List.of(LOCATION_DTO1, LOCATION_DTO2))
            .dateTime(DATE_TIME_DTO_WITH_ONE_DAY)
            .notice("전달 사항")
            .build();

    public static final MeetingRequestDto.DateTimeDto DATE_TIME_DTO_WITH_PERIOD = MeetingRequestDto.DateTimeDto.builder()
            .datePolicy(DatePolicy.PERIOD)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(6))
            .dayWeeks(new LinkedHashSet<>(List.of(1, 2, 7)))
            .startTime(START_TIME)
            .endTime(END_TIME)
            .build();

    public static final MeetingRequestDto MEETING_REQUEST_DTO_WITH_PERIOD = MeetingRequestDto.builder()
            .category(Category.MENTORING)
            .title(TITLE1)
            .content(CONTENT1)
            .tags(List.of(Tag.LIFESTYLE, Tag.MEDIA, Tag.EDU))
            .priceInfo(PRICE_DTO)
            .locations(List.of(LOCATION_DTO1, LOCATION_DTO2))
            .dateTime(DATE_TIME_DTO_WITH_PERIOD)
            .notice("전달 사항")
            .build();

    public static final MeetingRequestDto.DateTimeDto DATE_TIME_DTO_WITH_FREE = MeetingRequestDto.DateTimeDto.builder()
            .datePolicy(DatePolicy.FREE)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(10))
            .startTime(START_TIME)
            .endTime(END_TIME)
            .dates(List.of(LocalDate.now(), LocalDate.now().plusDays(2),
                    LocalDate.now().plusDays(5), LocalDate.now().plusDays(10)))
            .build();

    public static final MeetingRequestDto MEETING_REQUEST_DTO_WITH_FREE = MeetingRequestDto.builder()
            .category(Category.MENTORING)
            .title(TITLE1)
            .content(CONTENT1)
            .tags(List.of(Tag.LIFESTYLE, Tag.MEDIA, Tag.EDU))
            .priceInfo(PRICE_DTO)
            .locations(List.of(LOCATION_DTO1, LOCATION_DTO2))
            .dateTime(DATE_TIME_DTO_WITH_FREE)
            .notice("전달 사항")
            .build();

    public static final MeetingRequestDto.DateTimeDto DATE_TIME_DTO_WITH_ALL = MeetingRequestDto.DateTimeDto.builder()
            .datePolicy(DatePolicy.FREE)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(10))
            .startTime(START_TIME)
            .endTime(END_TIME)
            .dayWeeks(new LinkedHashSet<>(List.of(1, 2, 7)))
            .dates(List.of(LocalDate.now(), LocalDate.now().plusDays(2),
                    LocalDate.now().plusDays(5), LocalDate.now().plusDays(10)))
            .build();

    public static final MeetingRequestDto MEETING_REQUEST_DTO_WITH_ALL = MeetingRequestDto.builder()
            .category(Category.MENTORING)
            .title(TITLE1)
            .content(CONTENT1)
            .tags(List.of(Tag.LIFESTYLE, Tag.MEDIA, Tag.EDU))
            .priceInfo(PRICE_DTO)
            .locations(List.of(LOCATION_DTO1, LOCATION_DTO2))
            .dateTime(DATE_TIME_DTO_WITH_ALL)
            .notice("전달 사항")
            .build();
}
