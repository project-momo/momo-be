package com.example.momobe.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CustomReservationRepository {
    List<Reservation> findReservationBetween(Long meetingId,
                                             LocalDate date,
                                             LocalTime startTime,
                                             LocalTime endTime);

    List<Reservation> findPaymentCompletedReservation(Long meetingId);
    List<Long> findReservationAmounts(Long meetingId);
}
