package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.momobe.address.domain.QAddress.address;
import static com.example.momobe.meeting.domain.QDateTime.dateTime1;
import static com.example.momobe.meeting.domain.QMeeting.meeting;
import static com.example.momobe.reservation.domain.QReservation.reservation;
import static com.example.momobe.user.domain.QAvatar.avatar;
import static com.example.momobe.user.domain.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;
import static com.querydsl.core.types.Projections.list;

@SuppressWarnings("unchecked")
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
                .where(containsKeyword(keyword), eqCategory(category))
                .fetch();

        List<Long> meetingIds = dtos.stream()
                .map(MeetingResponseDto::getMeetingId).collect(Collectors.toList());

        Map<Long, Group> groupMap = queryFactory
                .from(meeting)
                .where(meeting.id.in(meetingIds))
                .leftJoin(address).on(address.id.in(meeting.address.addressIds))
                .leftJoin(meeting.dateTimeInfo.dateTimes, dateTime1)
                .transform(
                        groupBy(meeting.id).as(
                                set(address.si.append(" ").append(address.gu)),
                                list(dateTime1.dateTime)
                        )
                );

        Map<Long, List<String>> addressesMaps = new LinkedHashMap<>();
        Map<Long, List<Integer>> dayWeeksMaps = new LinkedHashMap<>();
        Map<Long, List<LocalDate>> datesMaps = new LinkedHashMap<>();
        Map<Long, MeetingResponseDto.DateTimeDto> dateInfoMap = new LinkedHashMap<>();

        dtos.forEach(dto -> {
            addressesMaps.put(dto.getMeetingId(), new ArrayList<>());
            dayWeeksMaps.put(dto.getMeetingId(), null);
            datesMaps.put(dto.getMeetingId(), null);
            dateInfoMap.put(dto.getMeetingId(), dto.getDateTime());
        });

        groupMap.forEach(
                (meetingId, group) -> {
                    Object[] array = group.toArray();

                    Set<String> addresses = (Set<String>) array[1];
                    addressesMaps.put(meetingId, new ArrayList<>(addresses));
                    List<LocalDateTime> dateTimes = (List<LocalDateTime>) array[2];
                    MeetingResponseDto.DateTimeDto dateTimeDto = dateInfoMap.get(meetingId);

                    if (dateTimeDto.getDatePolicy() == DatePolicy.FREE) {
                        LinkedHashSet<LocalDate> set = new LinkedHashSet<>();
                        dateTimes.forEach(dateTime -> {
                            if (dateTime != null)
                                set.add(dateTime.toLocalDate());
                        });
                        datesMaps.put(meetingId, new ArrayList<>(set));
                    } else if (dateTimeDto.getDatePolicy() == DatePolicy.PERIOD) {
                        TreeSet<Integer> set = new TreeSet<>();
                        dateTimes.forEach(dateTime -> {
                            if (dateTime != null)
                                set.add(dateTime.getDayOfWeek().getValue());
                        });
                        dayWeeksMaps.put(meetingId, new ArrayList<>(set));
                    }
                }
        );

        dtos.forEach(dto -> dto.init(
                addressesMaps.get(dto.getMeetingId()),
                dayWeeksMaps.get(dto.getMeetingId()),
                datesMaps.get(dto.getMeetingId())));

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
