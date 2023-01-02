package com.example.momobe.reservation.dto.in;

import lombok.*;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

import static lombok.AccessLevel.*;

@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class RequestReservationDto {
    @NotNull
    private ReservationDateDto dateInfo;
    @NotNull
    private Long amount;
    private String reservationMemo;

    @AssertFalse
    boolean isReservationDateEmpty() {
        return dateInfo == null || dateInfo.getReservationDate() == null;
    }

    @AssertFalse
    boolean isStartTimeEmpty() {
        return dateInfo == null || dateInfo.getStartTime() == null;
    }

    @AssertFalse
    boolean isEndTimeEmpty() {
        return dateInfo == null || dateInfo.getEndTime() == null;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReservationDateDto {
        @NotBlank
        private LocalDate reservationDate;
        @NotBlank
        private LocalTime startTime;
        @NotBlank
        private LocalTime endTime;
    }
}
