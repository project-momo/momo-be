package com.example.momobe.meeting.dto.out;

import com.example.momobe.reservation.domain.enums.ReservationState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Getter
public class MeetingInfoDto {
    List<ReservationDto> reservations;
    Set<String> addresses;
    List<LocalDateTime> dateTimes;

    @QueryProjection
    public MeetingInfoDto(Set<String> addresses, List<LocalDateTime> dateTimes) {
        this.addresses = addresses;
        this.dateTimes = dateTimes;
    }

    @QueryProjection
    public MeetingInfoDto(List<ReservationDto> reservations, Set<String> addresses, List<LocalDateTime> dateTimes) {
        this.reservations = reservations;
        this.addresses = addresses;
        this.dateTimes = dateTimes;
    }

    @Getter
    public static class ReservationDto {
        private final Long userId;
        private final String nickname;
        private final String imageUrl;
        private final String email;
        private final ReservationState reservationState;
        private final LocalDate reservationDate;
        private final LocalTime startTime;
        private final LocalTime endTime;
        private final String message;

        @QueryProjection
        public ReservationDto(Long userId, String nickname, String imageUrl, String email, ReservationState reservationState, LocalDate reservationDate, LocalTime startTime, LocalTime endTime, String message) {
            this.userId = userId;
            this.nickname = nickname;
            this.imageUrl = imageUrl;
            this.email = email;
            this.reservationState = reservationState;
            this.reservationDate = reservationDate;
            this.startTime = startTime;
            this.endTime = endTime;
            this.message = message;
        }
    }

}
