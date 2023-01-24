package com.example.momobe.reservation.application;

import com.example.momobe.reservation.domain.CanNotFoundReservationException;
import com.example.momobe.reservation.domain.Reservation;
import com.example.momobe.reservation.domain.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.momobe.common.exception.enums.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationCommonService {
    private final ReservationRepository reservationRepository;

    public Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new CanNotFoundReservationException(DATA_NOT_FOUND));
    }
}
