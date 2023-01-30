package com.example.momobe.reservation.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetReservationsAtSameTimeService {
    private final CustomReservationRepository customReservationRepository;

    public List<Reservation> getReservations(Long meetingId,
                                             LocalDate localDate,
                                             LocalTime startTime,
                                             LocalTime endTime) {
        List<Reservation> reservations = customReservationRepository.findReservationBetween(meetingId, localDate, startTime, endTime);

        return reservations.stream()
                .filter(reservation -> !reservation.isCanceledReservation())
                .collect(Collectors.toList());
    }
}
