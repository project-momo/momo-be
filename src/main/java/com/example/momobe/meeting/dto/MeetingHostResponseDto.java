package com.example.momobe.meeting.dto;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.reservation.domain.enums.ReservationState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class MeetingHostResponseDto extends MeetingResponseDto {
    private ApplicationDto applications;

    @QueryProjection
    public MeetingHostResponseDto(Long meetingId, Category category, Long hostId, String hostNickname, String hostImageUrl, String hostEmail, String title, String content, String addressInfo, MeetingState meetingState, DatePolicy datePolicy, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, Integer maxTime, Long price) {
        super(meetingId, category, hostId, hostNickname, hostImageUrl, hostEmail, title, content, addressInfo, meetingState, datePolicy, startDate, endDate, startTime, endTime, maxTime, price);
    }

    public void init(List<String> addresses, List<Integer> dayWeeks, List<LocalDate> dates, ApplicationDto applications) {
        super.init(addresses, dayWeeks, dates);
        this.applications = applications;
    }

    @Getter
    @AllArgsConstructor
    public static class ApplicationDto {
        private final List<RequestDto> requests;
        private final List<RequestConfirmedDto> confirmed;
    }

    @Getter
    public static class RequestDto extends MeetingUserResponseDto {
        private final ReservationState reservationState;
        private final String message;
        private final MeetingDateTimeDto dateTimeInfo;

        public RequestDto(Long userId, String nickname, String imageUrl, ReservationState reservationState,
                          LocalDate date, LocalTime startTime, LocalTime endTime, String message) {
            super(userId, nickname, imageUrl);
            this.reservationState = reservationState;
            this.message = message;
            this.dateTimeInfo = new MeetingDateTimeDto(date, startTime, endTime);
        }
    }

    @Getter
    public static class RequestConfirmedDto extends MeetingUserResponseWithEmailDto {
        private final ReservationState reservationState;
        private final String message;
        private final MeetingDateTimeDto dateTimeInfo;

        public RequestConfirmedDto(Long userId, String nickname, String imageUrl, String email,
                                   ReservationState reservationState,
                                   LocalDate date, LocalTime startTime, LocalTime endTime, String message) {
            super(userId, nickname, imageUrl, email);
            this.reservationState = reservationState;
            this.message = message;
            this.dateTimeInfo = new MeetingDateTimeDto(date, startTime, endTime);
        }
    }

}
