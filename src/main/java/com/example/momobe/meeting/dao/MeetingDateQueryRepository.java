package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.domain.DateTime;
import com.example.momobe.meeting.domain.QDateTime;
import com.example.momobe.reservation.domain.QReservation;
import com.example.momobe.reservation.domain.Reservation;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.momobe.reservation.domain.QReservation.*;

@Repository
@RequiredArgsConstructor
public class MeetingDateQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    // 예약 가능한 날짜를 파악하는 방법
    // 1. meetingId와 LocalDate로 해당 월에 해당하는 데이터를 전부 불러온다.
    // 2. 같은 meetingId를 가진 reservation들을 시간대별로 left join한다.
    // 3. reservation이 null인 경우 == 예약 가능함

//    public List<DateTime> findMeetingDatesBy(LocalDateTime startDate,
//                                             LocalDateTime endDate,
//                                             Long meetingId) {
//        QDateTime dateTime = new QDateTime("dateTime");
//
//        jpaQueryFactory.select(reservation.reservationDate.startDateTime)
//                        .from(reservation)
//                .where(reservation.meetingId.eq(meetingId)
//
//
//        jpaQueryFactory.select(dateTime)
//                .from(dateTime)
//                .jo
//    }
}
