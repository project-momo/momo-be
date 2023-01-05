package com.example.momobe.reservation.dto.in;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

import static lombok.AccessLevel.*;

@Getter
@Builder
@AllArgsConstructor
public class RequestReservationDto {
    @Valid
    @NotNull
    private ReservationDateDto dateInfo;
    @NotNull
    private Long amount;
    private String reservationMemo;

    @Getter
    @Builder
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
