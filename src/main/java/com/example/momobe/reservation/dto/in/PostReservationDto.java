package com.example.momobe.reservation.dto.in;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
public class PostReservationDto {
    @Valid
    @NotNull
    private final ReservationDateDto dateInfo;
    @NotNull
    private final Long amount;
    private final String reservationMemo;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ReservationDateDto {
        @NotNull
        private final LocalDate reservationDate;
        @NotNull
        private final LocalTime startTime;
        @NotNull
        private final LocalTime endTime;
    }
}
