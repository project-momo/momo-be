package com.example.momobe.meeting.dao;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import static com.example.momobe.meeting.domain.QMeeting.meeting;
import static com.example.momobe.user.domain.QAvatar.avatar;
import static com.example.momobe.user.domain.QUser.user;

@Component
public class MeetingQueryFactoryUtil {

    public JPAQuery<?> generateMeetingQuery(JPAQueryFactory queryFactory, Pageable pageable) {
        return queryFactory.from(meeting)
                .leftJoin(user).on(meeting.hostId.eq(user.id))
                .leftJoin(user.avatar, avatar)
                .groupBy(meeting.id)
                .orderBy(meeting.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

}
