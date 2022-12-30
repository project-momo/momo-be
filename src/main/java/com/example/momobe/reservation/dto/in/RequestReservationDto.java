package com.example.momobe.reservation.dto.in;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static lombok.AccessLevel.*;

@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class RequestReservationDto {
    private ReservationDateDto dateInfo;
    private Long amount;
    private String reservationMemo;

    @Getter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class ReservationDateDto {
        private LocalDate reservationDate;
        private LocalTime startTime;
        private LocalTime endTime;
    }
}
