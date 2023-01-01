package com.example.momobe.meeting.dao;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.meeting.domain.MeetingNotFoundException;
import com.example.momobe.meeting.dto.MeetingDetailResponseDto;
import com.example.momobe.meeting.dto.QMeetingDetailResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.momobe.address.domain.QAddress.address;
import static com.example.momobe.meeting.domain.QDateTime.dateTime1;
import static com.example.momobe.meeting.domain.QMeeting.meeting;
import static com.example.momobe.user.domain.QAvatar.avatar;
import static com.example.momobe.user.domain.QUser.user;
import static com.querydsl.core.group.GroupBy.*;
import static com.querydsl.core.types.dsl.Expressions.constant;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingDetailQueryRepository {
    private final JPAQueryFactory queryFactory;

    public MeetingDetailResponseDto findById(Long meetingId) {
        List<MeetingDetailResponseDto> dto = queryFactory
                .from(meeting)
                .where(meeting.id.eq(meetingId))
                .leftJoin(user).on(meeting.hostId.eq(user.id))
                .leftJoin(user.avatar, avatar)
                .leftJoin(address).on(meeting.address.addressIds.contains(address.id))
                .leftJoin(meeting.dateTimeInfo.dateTimes, dateTime1)
                .transform(groupBy(meeting.id).list(
                        new QMeetingDetailResponseDto(
                                constant(meetingId),
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
                                set(address.si.append(" ").append(address.gu)),
                                list(dateTime1.dateTime))
                ));

        if (dto.isEmpty()) throw new MeetingNotFoundException(ErrorCode.DATA_NOT_FOUND);
        return dto.get(0);
    }
}
