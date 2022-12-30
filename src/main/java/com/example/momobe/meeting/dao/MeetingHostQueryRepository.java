package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.dto.MeetingHostResponseDto;
import com.example.momobe.meeting.dto.MeetingResponseDto;
import com.example.momobe.meeting.dto.QMeetingHostResponseDto;
import com.querydsl.core.group.Group;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.momobe.address.domain.QAddress.address;
import static com.example.momobe.meeting.domain.QDateTime.dateTime1;
import static com.example.momobe.meeting.domain.QMeeting.meeting;
import static com.example.momobe.reservation.domain.QReservation.reservation;
import static com.example.momobe.user.domain.QAvatar.avatar;
import static com.example.momobe.user.domain.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;

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
                .where(meeting.hostId.eq(hostId))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(meeting.count())
                .from(meeting)
                .where(meeting.hostId.eq(hostId));

//        List<Long> meetingIds = dtos.stream()
//                .map(MeetingResponseDto::getMeetingId).collect(Collectors.toList());

//        Map<Long, Group> groupMap = queryFactory
//                .from(meeting)
//                .where(meeting.id.in(meetingIds))
//                .innerJoin(reservation).on(reservation.meetingId.in(meetingIds))
//                .innerJoin(user).on(reservation.reservedUser.userId.eq(user.id))
//                .innerJoin(user.avatar, avatar)
//                .innerJoin(address).on(address.id.in(meeting.address.addressIds))
//                .innerJoin(meeting.dateTimeInfo.dateTimes, dateTime1)
//                .transform(groupBy(meeting.id).as(
//                        list(user.id, user.nickname.nickname, avatar.remotePath, user.email.address,
//                                reservation.meetingId, reservation.reservationDate.date,
//                                reservation.reservationDate.startTime, reservation.reservationDate.endTime,
//                                reservation.reservationMemo.content, reservation.reservationState),
//                        list(address.gu, address.si),
//                        list(dateTime1.dateTime))
//                );

//        groupMap.forEach((k, v) -> System.out.println(Arrays.toString(v.toArray())));


//        List<Tuple> tuples = queryFactory.select(
//                        user.id, user.nickname.nickname, avatar.remotePath, user.email.address,
//                        reservation.meetingId, reservation.reservationDate.date,
//                        reservation.reservationDate.startTime, reservation.reservationDate.endTime,
//                        reservation.reservationMemo.content, reservation.reservationState
//                )
//                .from(reservation)
//                .where(reservation.meetingId.in(meetingIds))
//                .innerJoin(user).on(reservation.reservedUser.userId.eq(user.id))
//                .innerJoin(user.avatar, avatar)
//                .fetch();

        Map<Long, List<MeetingHostResponseDto.RequestDto>> requestMaps = new LinkedHashMap<>();
        Map<Long, List<MeetingHostResponseDto.RequestConfirmedDto>> confirmedMaps = new LinkedHashMap<>();

        dtos.forEach(dto -> {
            requestMaps.put(dto.getMeetingId(), new ArrayList<>());
            confirmedMaps.put(dto.getMeetingId(), new ArrayList<>());
        });


//        groupMap.forEach(
//                (meetingId, group) -> {
//                    List<List<?>> list = group.getList(list(user.id, user.nickname.nickname, avatar.remotePath, user.email.address,
//                            reservation.meetingId, reservation.reservationDate.date,
//                            reservation.reservationDate.startTime, reservation.reservationDate.endTime,
//                            reservation.reservationMemo.content, reservation.reservationState));
//                }
//        );

//        tuples.forEach(tuple -> {
//            ReservationState state = tuple.get(reservation.reservationState);
//
//            Long meetingId = tuple.get(reservation.meetingId);
//            if (state == ReservationState.ACCEPT) {
//                confirmedMaps.get(meetingId).add(new MeetingHostResponseDto.RequestConfirmedDto(
//                        tuple.get(user.id), tuple.get(user.nickname.nickname),
//                        tuple.get(avatar.remotePath), tuple.get(reservation.reservationState),
//                        tuple.get(user.email.address),
//                        tuple.get(reservation.reservationDate.date),
//                        tuple.get(reservation.reservationDate.startTime),
//                        tuple.get(reservation.reservationDate.endTime),
//                        tuple.get(reservation.reservationMemo.content)
//                ));
//            } else {
//                requestMaps.get(meetingId).add(new MeetingHostResponseDto.RequestDto(
//                        tuple.get(user.id), tuple.get(user.nickname.nickname),
//                        tuple.get(avatar.remotePath), tuple.get(reservation.reservationState),
//                        tuple.get(reservation.reservationDate.date),
//                        tuple.get(reservation.reservationDate.startTime),
//                        tuple.get(reservation.reservationDate.endTime),
//                        tuple.get(reservation.reservationMemo.content)
//                ));
//            }
//        });

        dtos.forEach(dto ->
                dto.init(
                        // 목데이터
                        List.of("서울시 강남구", "서울시 강북구"),
                        List.of(1, 3, 7), List.of(LocalDate.now(), LocalDate.now().plusDays(1)),
                        new MeetingHostResponseDto.ApplicationDto(
                                requestMaps.get(dto.getMeetingId()),
                                confirmedMaps.get(dto.getMeetingId())
                        ))
        );


        return PageableExecutionUtils.getPage(dtos, pageable, countQuery::fetchOne);
    }

}
