package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.dto.out.*;
import com.example.momobe.reservation.domain.enums.ReservationState;
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
import static com.querydsl.core.group.GroupBy.*;

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
                        meeting.price))
                .where(meeting.hostId.eq(hostId))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(meeting.count())
                .from(meeting)
                .where(meeting.hostId.eq(hostId));

        List<Long> meetingIds = dtos.stream()
                .map(MeetingResponseDto::getMeetingId).collect(Collectors.toList());

        Map<Long, MeetingInfoDto> meetingInfoMap = queryFactory
                .from(meeting)
                .where(meeting.id.in(meetingIds))
                .leftJoin(reservation).on(reservation.meetingId.in(meetingIds))
                .leftJoin(user).on(reservation.reservedUser.userId.eq(user.id))
                .leftJoin(user.avatar, avatar)
                .leftJoin(address).on(address.id.in(meeting.address.addressIds))
                .leftJoin(meeting.dateTimeInfo.dateTimes, dateTime1)
                .transform(
                        groupBy(meeting.id).as(
                                new QMeetingInfoDto(
                                        list(new QMeetingInfoDto_ReservationDto(
                                                user.id, user.nickname.nickname, avatar.remotePath, user.email.address,
                                                reservation.reservationState, reservation.reservationDate.date,
                                                reservation.reservationDate.startTime, reservation.reservationDate.endTime,
                                                reservation.reservationMemo.content)),
                                        set(address.si.append(" ").append(address.gu)),
                                        list(dateTime1.dateTime))

                        ));

        Map<Long, List<MeetingHostResponseDto.RequestDto>> requestMaps = new LinkedHashMap<>();
        Map<Long, List<MeetingHostResponseDto.RequestConfirmedDto>> confirmedMaps = new LinkedHashMap<>();
        Map<Long, List<String>> addressesMaps = new LinkedHashMap<>();
        Map<Long, List<Integer>> dayWeeksMaps = new LinkedHashMap<>();
        Map<Long, List<LocalDate>> datesMaps = new LinkedHashMap<>();
        Map<Long, MeetingResponseDto.DateTimeDto> dateInfoMap = new LinkedHashMap<>();

        dtos.forEach(dto -> {
            requestMaps.put(dto.getMeetingId(), new ArrayList<>());
            confirmedMaps.put(dto.getMeetingId(), new ArrayList<>());
            addressesMaps.put(dto.getMeetingId(), new ArrayList<>());
            dayWeeksMaps.put(dto.getMeetingId(), null);
            datesMaps.put(dto.getMeetingId(), null);
            dateInfoMap.put(dto.getMeetingId(), dto.getDateTime());
        });

        meetingInfoMap.forEach(
                (meetingId, meetingInfoDto) -> {
                    meetingInfoDto.getReservations().forEach(
                            reservationDto -> {
                                if (reservationDto.getReservationState() == ReservationState.ACCEPT) {
                                    confirmedMaps.get(meetingId).add(new MeetingHostResponseDto.RequestConfirmedDto(reservationDto));
                                } else {
                                    requestMaps.get(meetingId).add(new MeetingHostResponseDto.RequestDto(reservationDto));
                                }
                            }
                    );

                    addressesMaps.put(meetingId, new ArrayList<>(meetingInfoDto.getAddresses()));
                    MeetingResponseDto.DateTimeDto dateTimeDto = dateInfoMap.get(meetingId);

                    if (dateTimeDto.getDatePolicy() == DatePolicy.FREE) {
                        LinkedHashSet<LocalDate> set = new LinkedHashSet<>();
                        meetingInfoDto.getDateTimes().forEach(dateTime -> {
                            if (dateTime != null)
                                set.add(dateTime.toLocalDate());
                        });
                        datesMaps.put(meetingId, new ArrayList<>(set));
                    } else if (dateTimeDto.getDatePolicy() == DatePolicy.PERIOD) {
                        TreeSet<Integer> set = new TreeSet<>();
                        meetingInfoDto.getDateTimes().forEach(dateTime -> {
                            if (dateTime != null)
                                set.add(dateTime.getDayOfWeek().getValue());
                        });
                        dayWeeksMaps.put(meetingId, new ArrayList<>(set));
                    }
                }
        );

        dtos.forEach(dto ->
                dto.init(
                        addressesMaps.get(dto.getMeetingId()),
                        dayWeeksMaps.get(dto.getMeetingId()),
                        datesMaps.get(dto.getMeetingId()),
                        new MeetingHostResponseDto.ApplicationDto(
                                requestMaps.get(dto.getMeetingId()),
                                confirmedMaps.get(dto.getMeetingId())
                        ))
        );

        return PageableExecutionUtils.getPage(dtos, pageable, countQuery::fetchOne);
    }

}
