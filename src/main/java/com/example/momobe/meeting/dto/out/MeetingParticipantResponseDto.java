package com.example.momobe.meeting.dto.out;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.example.momobe.reservation.domain.enums.ReservationState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class MeetingParticipantResponseDto extends MeetingResponseDto {
    private final ApplicationDto application;

    @QueryProjection
    public MeetingParticipantResponseDto(Long meetingId, Category category, Long hostId, String hostNickname, String hostImageUrl, String hostEmail, String title, String content, String addressInfo, MeetingState meetingState, DatePolicy datePolicy, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, Integer maxTime, Long price,
                                         ApplicationDto application, Long currentParticipants, Long reservationCapacity) {
        super(meetingId, category, hostId, hostNickname, hostImageUrl, hostEmail, title, content, addressInfo, meetingState, datePolicy, startDate, endDate, startTime, endTime, maxTime, price, currentParticipants, reservationCapacity);
        this.application = application;
    }


    @Getter
    public static class ApplicationDto extends MeetingUserResponseWithEmailDto {
        private final String reservationState;
        private final String message;
        private final String paymentKey;
        private final MeetingDateTimeDto dateTimeInfo;
        private final Long reservationId;

        @QueryProjection
        public ApplicationDto(Long userId, String nickname, String imageUrl, String email,
                              ReservationState reservationState, String message, String paymentKey,
                              MeetingDateTimeDto dateTimeInfo, Long reservationId, LocalDateTime startDateTime) {
            super(userId, nickname, imageUrl, email);
            this.message = message;
            this.paymentKey = paymentKey;
            this.dateTimeInfo = dateTimeInfo;
            this.reservationId = reservationId;

            if (reservationState.equals(ReservationState.ACCEPT) && LocalDateTime.now().isAfter(startDateTime)) {
                this.reservationState = ReservationState.FINISH.getKorType();
            } else {
                this.reservationState = reservationState.getKorType();
            }
        }
    }

}
