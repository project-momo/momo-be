package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.dto.out.MeetingInfoDto;
import com.example.momobe.meeting.dto.out.MeetingResponseDto;
import com.example.momobe.meeting.dto.out.QMeetingInfoDto;
import com.example.momobe.meeting.dto.out.QMeetingResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
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
import static com.example.momobe.tag.domain.QTag.tag;
import static com.example.momobe.user.domain.QAvatar.avatar;
import static com.example.momobe.user.domain.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final MeetingQueryFactoryUtil meetingQueryFactoryUtil;

    public Page<MeetingResponseDto> findAll(String keyword, Category category, String tagName, Pageable pageable) {
        List<MeetingResponseDto> dtos = meetingQueryFactoryUtil
                .generateMeetingQuery(queryFactory, pageable)
                .select(new QMeetingResponseDto(
                        meeting.id,
                        meeting.category,
                        meeting.hostId,
                        user.nickname.nickname,
                        avatar.remotePath,
                        user.email.address,
                        meeting.title,
                        new CaseBuilder()
                                .when(meeting.content.length().gt(100))
                                .then(meeting.content.substring(0, 101).append("..."))
                                .otherwise(meeting.content),
                        meeting.address.addressInfo,
                        meeting.meetingState,
                        meeting.dateTimeInfo.datePolicy,
                        meeting.dateTimeInfo.startDate,
                        meeting.dateTimeInfo.endDate,
                        meeting.dateTimeInfo.startTime,
                        meeting.dateTimeInfo.endTime,
                        meeting.dateTimeInfo.maxTime,
                        meeting.price))
                .leftJoin(tag).on(tag.id.in(meeting.tagIds))
                .where(containsKeyword(keyword), eqCategory(category), eqTagName(tagName))
                .fetch();

        List<Long> meetingIds = dtos.stream()
                .map(MeetingResponseDto::getMeetingId).collect(Collectors.toList());

        Map<Long, MeetingInfoDto> meetingInfoDtoMap = queryFactory
                .from(meeting)
                .where(meeting.id.in(meetingIds))
                .leftJoin(reservation).on(reservation.meetingId.in(meetingIds))
                .leftJoin(user).on(reservation.reservedUser.userId.eq(user.id))
                .leftJoin(user.avatar, avatar)
                .leftJoin(address).on(address.id.in(meeting.address.addressIds))
                .leftJoin(meeting.dateTimeInfo.dateTimes, dateTime1)
                .transform(
                        groupBy(meeting.id).as(new QMeetingInfoDto(
                                set(address.si.append(" ").append(address.gu)),
                                set(dateTime1.dateTime))
                        )
                );

        MeetingInfoUtil meetingInfoUtil = new MeetingInfoUtil(dtos);
        meetingInfoUtil.updateAddressesAndDateTimes(meetingInfoDtoMap);
        meetingInfoUtil.initMeetingResponseDto(dtos);

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

    private BooleanExpression eqTagName(String tagName) {
        if (tagName == null) return null;
        return tag.name.eq(tagName);
    }

    public List<Long> findMeetingClosedBefore3days() {
        return queryFactory
                .select(meeting.id)
                .from(meeting)
//                .leftJoin(reservation)
//                .on(reservation.meetingId.eq(meeting.id))
//                .where(reservation.reservationState.eq(ReservationState.PAYMENT_SUCCESS))
                .where(endDateFilter())
                .orderBy(meeting.id.desc())
                .distinct()
                .fetch();
    }

    private BooleanExpression endDateFilter() {
        LocalDate now = LocalDate.now();
        return meeting.dateTimeInfo.endDate.eq(now.minusDays(3));
    }
}
