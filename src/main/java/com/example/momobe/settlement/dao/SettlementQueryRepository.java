package com.example.momobe.settlement.dao;

import com.example.momobe.reservation.domain.enums.ReservationState;
import com.example.momobe.settlement.dto.out.SettlementResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.momobe.meeting.domain.QMeeting.meeting;
import static com.example.momobe.reservation.domain.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class SettlementQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<SettlementResponseDto.Reservation> findReservationForMeetingClosed(){
        return queryFactory.select(Projections.constructor(SettlementResponseDto.Reservation.class,
                        reservation.paymentId,
                        reservation.meetingId,
                        reservation.id,
                        reservation.amount.won,
                        meeting.hostId))
                .from(reservation)
                .innerJoin(meeting)
                .on(reservation.meetingId.eq(meeting.id))
                .on(meeting.dateTimeInfo.endDate.between(LocalDate.now(),LocalDate.now().minusMonths(1)))
                .where(reservation.reservationState.eq(ReservationState.PAYMENT_SUCCESS))
                .fetch();
    }

}
