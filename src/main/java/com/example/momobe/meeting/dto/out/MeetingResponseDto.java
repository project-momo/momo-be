package com.example.momobe.meeting.dto.out;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static lombok.AccessLevel.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class MeetingResponseDto {
    private Long meetingId;
    private String category;
    private MeetingUserResponseWithEmailDto host;
    private String title;
    private String content;
    private AddressDto address;
    private String meetingState;
    private Boolean isOpen;
    private DateTimeDto dateTime;
    private Long price;
    private String detailState;

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
                              Long price, Long currentParticipants, Long reservationCapacity) {
        this.meetingId = meetingId;
        this.category = category.getDescription();
        this.host = new MeetingUserResponseWithEmailDto(hostId, hostNickname, hostImageUrl, hostEmail);
        this.title = title;
        this.content = content;
        this.address = new AddressDto(addressInfo);
        this.meetingState = meetingState.getDescription();
        this.dateTime = new DateTimeDto(datePolicy, startDate, endDate, startTime, endTime, maxTime);
        this.price = price;
        this.isOpen = meetingState.equals(MeetingState.OPEN);
        this.detailState = setDetailState(endDate, endTime, currentParticipants, reservationCapacity);
    }

    private String setDetailState(LocalDate endDate, LocalTime endTime, Long currentParticipants, Long reservationCapacity) {
        if (LocalDateTime.now().isAfter(LocalDateTime.of(endDate, endTime))) {
            return "모집 종료";
        }

        if (reservationCapacity - currentParticipants <= 0) {
            return "정원 마감";
        }

        return "모집 중";
    }
}
