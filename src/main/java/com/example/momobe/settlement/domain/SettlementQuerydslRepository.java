package com.example.momobe.settlement.infrastructure;

import com.example.momobe.meeting.domain.QMeeting;
import com.example.momobe.reservation.domain.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.momobe.meeting.domain.QMeeting.meeting;
import static com.example.momobe.reservation.domain.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class SettlementQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public List<Reservation> findReservationForMeetingClosedBefore3days(){
        return queryFactory.select(reservation)
                .from(reservation)
                .join(meeting)
                .on(meeting.dateTimeInfo.endDate.eq(LocalDate.now().minusDays(3)))
                .where(reservation.meetingId.eq(reservation.meetingId))
                .fetch();
    }

}
