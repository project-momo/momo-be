package com.example.momobe.meeting.dto;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.reservation.domain.enums.ReservationState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
public class MeetingParticipantResponseDto extends MeetingResponseDto {
    private final ApplicationDto application;

    @QueryProjection
    public MeetingParticipantResponseDto(Long meetingId, Category category, Long hostId, String hostNickname, String hostImageUrl, String title, String content, String addressInfo, MeetingState meetingState, DatePolicy datePolicy, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, Integer maxTime, Long price,
                                         ApplicationDto application) {
        super(meetingId, category, hostId, hostNickname, hostImageUrl, title, content, addressInfo, meetingState, datePolicy, startDate, endDate, startTime, endTime, maxTime, price);
        this.application = application;
    }


    @Getter
    public static class ApplicationDto extends MeetingUserResponseDto {
        private final ReservationState reservationState;
        private final String email;
        private final String message;
        private final DateTimeInfo dateTimeInfo;

        @QueryProjection
        public ApplicationDto(Long userId, String nickname, String imageUrl,
                              ReservationState reservationState, String email, String message, DateTimeInfo dateTimeInfo) {
            super(userId, nickname, imageUrl);
            this.reservationState = reservationState;
            this.email = email;
            this.message = message;
            this.dateTimeInfo = dateTimeInfo;
        }
    }


    @Getter
    public static class DateTimeInfo {
        private final LocalDate date;
        private final String time;

        @QueryProjection
        public DateTimeInfo(LocalDate date, LocalTime startTime, LocalTime endTime) {
            this.date = date;
            this.time = startTime + " - " + endTime + " (" + startTime.until(endTime, ChronoUnit.HOURS) + "시간)";
        }
    }

}
