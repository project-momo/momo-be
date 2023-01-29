package com.example.momobe.reservation.dto.in;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostReservationDto {
    @Valid
    @NotNull
    private ReservationDateDto dateInfo;
    @NotNull
    private Long amount;
    private String reservationMemo;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ReservationDateDto {
        @NotNull
        private LocalDate reservationDate;
        @NotNull
        private LocalTime startTime;
        @NotNull
        private LocalTime endTime;
    }
}
