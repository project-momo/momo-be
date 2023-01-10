package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.dto.out.*;
import com.example.momobe.user.domain.QAvatar;
import com.example.momobe.user.domain.QUser;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.momobe.address.domain.QAddress.address;
import static com.example.momobe.meeting.domain.QDateTime.dateTime1;
import static com.example.momobe.meeting.domain.QMeeting.meeting;
import static com.example.momobe.payment.domain.QPayment.payment;
import static com.example.momobe.reservation.domain.QReservation.reservation;
import static com.example.momobe.user.domain.QAvatar.avatar;
import static com.example.momobe.user.domain.QUser.user;
import static com.querydsl.core.group.GroupBy.*;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingParticipantQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<MeetingParticipantResponseDto> findAll(Long participantId, Pageable pageable) {
        QUser host = new QUser("host");
        QUser participant = new QUser("participant");
        QAvatar hostAvatar = new QAvatar("hostAvatar");
        QAvatar participantAvatar = new QAvatar("participantAvatar");
        List<MeetingParticipantResponseDto> dtos = queryFactory
                .select(new QMeetingParticipantResponseDto(
                        meeting.id,
                        meeting.category,
                        meeting.hostId,
                        host.nickname.nickname,
                        hostAvatar.remotePath,
                        host.email.address,
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
                        new QMeetingParticipantResponseDto_ApplicationDto(
                                Expressions.constant(participantId),
                                participant.nickname.nickname,
                                participantAvatar.remotePath,
                                participant.email.address,
                                reservation.reservationState,
                                reservation.reservationMemo.content,
                                payment.paymentKey,
                                new QMeetingDateTimeDto(
                                        reservation.reservationDate.date,
                                        reservation.reservationDate.startTime,
                                        reservation.reservationDate.endTime
                                )
                        )))
                .from(reservation)
                .innerJoin(meeting).on(reservation.meetingId.eq(meeting.id))
                .innerJoin(host).on(meeting.hostId.eq(host.id))
                .leftJoin(host.avatar, hostAvatar)
                .innerJoin(participant).on(participant.id.eq(participantId))
                .leftJoin(participant.avatar, participantAvatar)
                .leftJoin(payment).on(payment.reservationId.eq(reservation.id))
                .where(reservation.reservedUser.userId.eq(participantId))
                .orderBy(reservation.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
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
                                list(dateTime1.dateTime))
                        )
                );

        MeetingInfoUtil meetingInfoUtil = new MeetingInfoUtil(dtos);
        meetingInfoUtil.updateAddressesAndDateTimes(meetingInfoDtoMap);
        meetingInfoUtil.initMeetingResponseDto(dtos);

        JPAQuery<Long> countQuery = queryFactory
                .select(reservation.count())
                .from(reservation)
                .where(reservation.reservedUser.userId.eq(participantId));

        return PageableExecutionUtils.getPage(dtos, pageable, countQuery::fetchOne);
    }

}
