package com.example.momobe.reservation.domain;

import lombok.*;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static lombok.AccessLevel.*;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ReservationDate {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Builder
    public ReservationDate(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;

        this.startDateTime = LocalDateTime.of(date, startTime);
        this.endDateTime = LocalDateTime.of(date, endTime);
    }
}
