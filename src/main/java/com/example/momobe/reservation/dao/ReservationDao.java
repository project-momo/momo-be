package com.example.momobe.reservation.dao;

import com.example.momobe.reservation.domain.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.momobe.reservation.domain.QReservation.*;
import static com.example.momobe.reservation.domain.enums.ReservationState.*;

@Repository
@RequiredArgsConstructor
public class ReservationDao {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Reservation> findOverdueUnapprovedReservations() {
        return jpaQueryFactory.select(reservation)
                .from(reservation)
                .where(reservation.reservationDate.startDateTime.before(LocalDateTime.now())
                        .and(reservation.reservationState.eq(PAYMENT_SUCCESS)))
                .fetch();
    }
}
