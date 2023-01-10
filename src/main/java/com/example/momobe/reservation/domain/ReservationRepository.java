package com.example.momobe.reservation.domain;

import com.example.momobe.reservation.domain.enums.ReservationState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByMeetingIdAndReservationStateIn(Long meetingId, List<ReservationState> reservationState);
}
