package com.example.momobe.meeting.dto.out;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class MeetingResponseDto {
    private final Long meetingId;
    private final String category;
    private final MeetingUserResponseWithEmailDto host;
    private final String title;
    private final String content;
    private final AddressDto address;
    private final String meetingState;
    private final Boolean isOpen;
    private final DateTimeDto dateTime;
    private final Long price;

    @Getter
    @RequiredArgsConstructor
    public static class AddressDto {
        private List<String> addresses;
        private final String addressInfo;
    }

    @Getter
    @RequiredArgsConstructor
    public static class DateTimeDto {
        private final DatePolicy datePolicy;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final LocalTime startTime;
        private final LocalTime endTime;
        private final Integer maxTime;
        private List<Integer> dayWeeks;
        private List<LocalDate> dates;
    }

    public void init(List<String> addresses, List<Integer> dayWeeks, List<LocalDate> dates) {
        this.address.addresses = addresses;
        this.dateTime.dayWeeks = dayWeeks;
        this.dateTime.dates = dates;
    }

    @QueryProjection
    public MeetingResponseDto(Long meetingId, Category category, Long hostId,
                              String hostNickname, String hostImageUrl, String hostEmail,
                              String title, String content, String addressInfo,
                              MeetingState meetingState, DatePolicy datePolicy,
                              LocalDate startDate, LocalDate endDate,
                              LocalTime startTime, LocalTime endTime, Integer maxTime,
                              Long price) {
        this.meetingId = meetingId;
        this.category = category.getDescription();
        this.host = new MeetingUserResponseWithEmailDto(hostId, hostNickname, hostImageUrl, hostEmail);
        this.title = title;
        this.content = content;
        this.address = new AddressDto(addressInfo);
        this.meetingState = meetingState.getDescription();
        this.isOpen = meetingState == MeetingState.OPEN;
        this.dateTime = new DateTimeDto(datePolicy, startDate, endDate, startTime, endTime, maxTime);
        this.price = price;
    }
}
