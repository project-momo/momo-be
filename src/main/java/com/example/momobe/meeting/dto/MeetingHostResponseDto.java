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
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
public class MeetingHostResponseDto extends MeetingResponseDto {
    private ApplicationDto applications;

    @QueryProjection
    public MeetingHostResponseDto(Long meetingId, Category category, Long hostId, String hostNickname, String hostImageUrl, String title, String content, String addressInfo, MeetingState meetingState, DatePolicy datePolicy, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, Integer maxTime, Long price) {
        super(meetingId, category, hostId, hostNickname, hostImageUrl, title, content, addressInfo, meetingState, datePolicy, startDate, endDate, startTime, endTime, maxTime, price);
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
        private final DateTimeInfo dateTimeInfo;
        private final String message;

        @QueryProjection
        public RequestDto(Long userId, String nickname, String imageUrl, ReservationState reservationState,
                          LocalDate date, LocalTime startTime, LocalTime endTime, String message) {
            super(userId, nickname, imageUrl);
            this.reservationState = reservationState;
            this.dateTimeInfo = new DateTimeInfo(date, startTime, endTime);
            this.message = message;
        }
    }

    @Getter
    public static class RequestConfirmedDto extends MeetingUserResponseDto {
        private final ReservationState reservationState;
        private final String email;
        private final DateTimeInfo dateTimeInfo;
        private final String message;

        @QueryProjection
        public RequestConfirmedDto(Long userId, String nickname, String imageUrl,
                                   ReservationState reservationState, String email,
                                   LocalDate date, LocalTime startTime, LocalTime endTime, String message) {
            super(userId, nickname, imageUrl);
            this.reservationState = reservationState;
            this.email = email;
            this.dateTimeInfo = new DateTimeInfo(date, startTime, endTime);
            this.message = message;
        }
    }

    @Getter
    public static class DateTimeInfo {
        private final LocalDate date;
        private final String time;

        public DateTimeInfo(LocalDate date, LocalTime startTime, LocalTime endTime) {
            this.date = date;
            this.time = startTime + " - " + endTime + " (" + startTime.until(endTime, ChronoUnit.HOURS) + "시간)";
        }
    }

}
