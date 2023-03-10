package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.dto.out.*;
import com.example.momobe.reservation.domain.enums.ReservationState;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
import static com.querydsl.core.group.GroupBy.set;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingHostQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final MeetingQueryFactoryUtil meetingQueryFactoryUtil;

    public Page<MeetingHostResponseDto> findAll(Long hostId, Pageable pageable) {
        List<MeetingHostResponseDto> dtos = meetingQueryFactoryUtil
                .generateMeetingQuery(queryFactory, pageable)
                .select(new QMeetingHostResponseDto(
                        meeting.id,
                        meeting.category,
                        meeting.hostId,
                        user.nickname.nickname,
                        avatar.remotePath,
                        user.email.address,
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
                        meeting.price,
                        JPAExpressions.select(reservation.count())
                                .from(reservation)
                                .where(reservation.meetingId.eq(meeting.id)),
                        meeting.personnel.longValue()))
                .where(meeting.hostId.eq(hostId))
                .fetch();

        List<Long> meetingIds = dtos.stream()
                .map(MeetingResponseDto::getMeetingId).collect(Collectors.toList());

        Map<Long, MeetingInfoDto> meetingInfoDtoMap = queryFactory
                .from(meeting)
                .leftJoin(reservation).on(reservation.meetingId.eq(meeting.id))
                .leftJoin(user).on(reservation.reservedUser.userId.eq(user.id))
                .leftJoin(user.avatar, avatar)
                .leftJoin(address).on(address.id.in(meeting.address.addressIds))
                .leftJoin(meeting.dateTimeInfo.dateTimes, dateTime1)
                .where(meeting.id.in(meetingIds).and(
                        reservation.reservationState.eq(ReservationState.ACCEPT).or(eqPaymentSuccessAndFuture())))
                .transform(
                        groupBy(meeting.id).as(
                                new QMeetingInfoDto(
                                        set(new QMeetingInfoDto_ReservationDto(
                                                reservation.id, user.id,
                                                user.nickname.nickname, avatar.remotePath, user.email.address,
                                                reservation.reservationState, reservation.reservationDate.date,
                                                reservation.reservationDate.startTime, reservation.reservationDate.endTime,
                                                reservation.reservationMemo.content)),
                                        set(address.si.append(" ").append(address.gu)),
                                        set(dateTime1.dateTime))

                        ));

        MeetingInfoUtil meetingInfoUtil = new MeetingInfoUtil(dtos);
        meetingInfoUtil.updateReservations(meetingInfoDtoMap);
        meetingInfoUtil.updateAddressesAndDateTimes(meetingInfoDtoMap);
        meetingInfoUtil.initMeetingHostResponseDto(dtos);

        JPAQuery<Long> countQuery = queryFactory
                .select(meeting.count())
                .from(meeting)
                .where(meeting.hostId.eq(hostId));

        return PageableExecutionUtils.getPage(dtos, pageable, countQuery::fetchOne);
    }

    private BooleanExpression eqPaymentSuccessAndFuture() {
        return reservation.reservationState.eq(ReservationState.PAYMENT_SUCCESS)
                .and(reservation.reservationDate.startDateTime.after(LocalDateTime.now()));
    }

}
