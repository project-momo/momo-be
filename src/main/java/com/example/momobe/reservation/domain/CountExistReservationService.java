package com.example.momobe.reservation.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountExistReservationService {
    private final CustomReservationRepository customReservationRepository;

    public Long countOf(Long meetingId,
                        LocalDate localDate,
                        LocalTime startTime,
                        LocalTime endTime) {
        List<Reservation> reservations = customReservationRepository.findReservationBetween(meetingId, localDate, startTime, endTime);

        return reservations.stream()
                .filter(reservation -> !reservation.isCanceledReservation())
                .count();
    }
}
