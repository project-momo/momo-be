package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.dto.MeetingResponseDto;
import com.example.momobe.meeting.dto.QMeetingResponseDto;
import com.querydsl.core.group.Group;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.momobe.address.domain.QAddress.address;
import static com.example.momobe.meeting.domain.QDateTime.dateTime1;
import static com.example.momobe.meeting.domain.QMeeting.meeting;
import static com.example.momobe.reservation.domain.QReservation.reservation;
import static com.example.momobe.user.domain.QAvatar.avatar;
import static com.example.momobe.user.domain.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final MeetingQueryFactoryUtil meetingQueryFactoryUtil;

    public Page<MeetingResponseDto> findAll(String keyword, Category category, Pageable pageable) {
        List<MeetingResponseDto> dtos = meetingQueryFactoryUtil
                .generateMeetingQuery(queryFactory, pageable)
                .select(new QMeetingResponseDto(
                        meeting.id,
                        meeting.category,
                        meeting.hostId,
                        user.nickname.nickname,
                        avatar.remotePath,
                        meeting.title,
                        meeting.content,
                        meeting.address.addressInfo,
                        meeting.meetingState,
                        meeting.dateTimeInfo.datePolicy,
                        meeting.dateTimeInfo.startDate,
                        meeting.dateTimeInfo.endDate,
                        meeting.dateTimeInfo.startTime,
                        meeting.dateTimeInfo.endTime,
                        meeting.dateTimeInfo.maxTime,
                        meeting.price))
                .where(containsKeyword(keyword), eqCategory(category))
                .fetch();

//        List<Long> meetingIds = dtos.stream()
//                .map(MeetingResponseDto::getMeetingId).collect(Collectors.toList());
//
//        Map<Long, Group> groupMap = queryFactory
//                .from(meeting)
//                .where(meeting.id.in(meetingIds))
//                .innerJoin(reservation).on(reservation.meetingId.in(meetingIds))
//                .innerJoin(user).on(reservation.reservedUser.userId.eq(user.id))
//                .innerJoin(user.avatar, avatar)
//                .innerJoin(address).on(address.id.in(meeting.address.addressIds))
//                .innerJoin(meeting.dateTimeInfo.dateTimes, dateTime1)
//                .transform(groupBy(meeting.id).as(address.gu, address.si, dateTime1.dateTime));

        dtos.forEach(dto -> dto.init(
                // 목데이터
                List.of("서울시 강남구", "서울시 강북구"),
                List.of(1, 3, 7), List.of(LocalDate.now(), LocalDate.now().plusDays(1))));

        JPAQuery<Long> countQuery = queryFactory
                .select(meeting.count())
                .from(meeting)
                .where(containsKeyword(keyword), eqCategory(category));

        return PageableExecutionUtils.getPage(dtos, pageable, countQuery::fetchOne);
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) return null;
        return meeting.title.contains(keyword).or(meeting.content.contains(keyword));
    }

    private BooleanExpression eqCategory(Category category) {
        if (category == null) return null;
        return meeting.category.eq(category);
    }

}
