package com.example.momobe.meeting.dao;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.dto.MeetingResponseDto;
import com.example.momobe.meeting.dto.QMeetingResponseDto;
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

import java.util.List;

import static com.example.momobe.address.domain.QAddress.address;
import static com.example.momobe.meeting.domain.QMeeting.meeting;
import static com.example.momobe.user.domain.QAvatar.avatar;
import static com.example.momobe.user.domain.QUser.user;

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
                        meeting.price,
                        meeting.notice))
                .where(containsKeyword(keyword), eqCategory(category))
                .fetch();

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
