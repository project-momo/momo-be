package com.example.momobe.reservation.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class CheckReservationStatusService {
    private final ReservationRepository reservationRepository;

    public void of(LocalDate localDate,
                   LocalTime startTime,
                   LocalTime endTime) {
    }
}
