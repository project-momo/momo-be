package com.example.momobe.reservation.infrastructure;

import com.example.momobe.reservation.domain.CustomReservationRepository;
import com.example.momobe.reservation.domain.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.example.momobe.reservation.domain.QReservation.*;
import static java.time.temporal.ChronoUnit.*;

@Repository
@RequiredArgsConstructor
public class CustomJpaQueryRepositoryImpl implements CustomReservationRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Reservation> findReservationBetween(Long meetingId,
                                                    LocalDate date,
                                                    LocalTime startTime,
                                                    LocalTime endTime) {
        return jpaQueryFactory.select(reservation)
                .from(reservation)
                .where(reservation.meetingId.eq(meetingId)
                        .and(reservation.reservationDate.date.eq(date))
                        .and((reservation.reservationDate.startTime.between(startTime, endTime.minus(1, MINUTES)))
                        .or(reservation.reservationDate.endTime.between(startTime.plus(1, MINUTES), endTime.minus(1, MINUTES)))))
                .fetch();
    }
}
