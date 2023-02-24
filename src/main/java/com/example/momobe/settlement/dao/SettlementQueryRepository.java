package com.example.momobe.settlement.dao;

import com.example.momobe.reservation.domain.enums.ReservationState;
import com.example.momobe.settlement.domain.Settlement;
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

    public List<Settlement> findReservationForMeetingClosed(){
        return queryFactory.select(Projections.fields(Settlement.class,
                        meeting.hostId.as("host"),
                        reservation.amount.won.as("amount"),
                        reservation.paymentId,
                        reservation.meetingId,
                        reservation.id.as("reservationId")
                        ))
                .from(reservation)
                .innerJoin(meeting)
                .on(meeting.id.eq(reservation.meetingId))
                .where(reservation.reservationState.eq(ReservationState.PAYMENT_SUCCESS)
                        .and(meeting.dateTimeInfo.endDate.between(LocalDate.now().minusMonths(1),LocalDate.now())))
                .fetch();
    }
}
