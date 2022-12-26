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

import static com.example.momobe.meeting.domain.QLocation.location;
import static com.example.momobe.meeting.domain.QMeeting.meeting;
import static com.example.momobe.user.domain.QAvatar.avatar;
import static com.example.momobe.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<MeetingResponseDto> findAll(String keyword, Category category, Pageable pageable) {
        List<MeetingResponseDto> dtos = queryFactory
                .select(new QMeetingResponseDto(
                        meeting.id,
                        meeting.category,
                        meeting.hostId,
                        user.nickname.nickname,
                        avatar.remotePath,
                        meeting.title,
                        meeting.content,
                        location.address.address1,
                        meeting.meetingStatus,
                        meeting.priceInfo.pricePolicy,
                        meeting.priceInfo.price))
                .from(meeting)
                .innerJoin(user).on(meeting.hostId.eq(user.id))
                .innerJoin(user.avatar, avatar)
                .innerJoin(meeting.locations, location)
                .where(containsKeyword(keyword), eqCategory(category))
                .groupBy(meeting.id)
                .orderBy(meeting.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
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
