package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.domain.DateTime;
//import com.example.momobe.meeting.domain.QDateTime;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.example.momobe.meeting.domain.QDateTime.*;

@Repository
@RequiredArgsConstructor
public class MeetingDateQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<DateTime> findMeetingDateBy(Long meetingId,
                                            LocalDate localDate,
                                            LocalTime startTime,
                                            LocalTime endTime) {
        return jpaQueryFactory.select(dateTime1)
                .from(dateTime1)
                .where(dateTime1.meeting.id.eq(meetingId)
                        .and(dateTime1.dateTime.eq(LocalDateTime.of(localDate, startTime))
                                .and(dateTime1.dateTime.goe(LocalDateTime.of(localDate, endTime)))))
                .fetch();
    }
}
