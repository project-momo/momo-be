package com.example.momobe.reservation.domain;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalTime;

import static lombok.AccessLevel.*;

@Embeddable
@RequiredArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ReservationDate {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
