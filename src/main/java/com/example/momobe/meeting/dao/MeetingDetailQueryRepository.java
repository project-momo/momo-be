package com.example.momobe.meeting.dao;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.meeting.domain.MeetingException;
import com.example.momobe.meeting.dto.out.MeetingDetailResponseDto;
import com.example.momobe.meeting.dto.out.QMeetingDetailResponseDto;
import com.example.momobe.question.dto.out.ResponseQuestionDto;
import com.example.momobe.question.infrastructure.QuestionQueryRepository;
import com.example.momobe.user.domain.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.momobe.address.domain.QAddress.address;
import static com.example.momobe.answer.domain.QAnswer.answer;
import static com.example.momobe.meeting.domain.QDateTime.dateTime1;
import static com.example.momobe.meeting.domain.QMeeting.meeting;
import static com.example.momobe.question.domain.QQuestion.question;
import static com.example.momobe.tag.domain.QTag.tag;
import static com.example.momobe.user.domain.QAvatar.avatar;
import static com.querydsl.core.group.GroupBy.*;
import static com.querydsl.core.types.dsl.Expressions.constant;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingDetailQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QuestionQueryRepository questionQueryRepository;

    QUser host = new QUser("host");
    QUser questioner = new QUser("questioner");
    QUser answerer = new QUser("answerer");

    public MeetingDetailResponseDto findById(Long meetingId) {
        List<MeetingDetailResponseDto> dtos = queryFactory
                .from(meeting)
                .where(meeting.id.eq(meetingId))
                .leftJoin(host).on(meeting.hostId.eq(host.id))
                .leftJoin(host.avatar, avatar)
                .leftJoin(address).on(meeting.address.addressIds.contains(address.id))
                .leftJoin(tag).on(meeting.tagIds.contains(tag.id))
                .leftJoin(meeting.dateTimeInfo.dateTimes, dateTime1)
                .leftJoin(question).on(question.meeting.meetingId.eq(meetingId))
                .leftJoin(answer).on(answer.question.questionId.eq(question.id))
                .leftJoin(questioner).on(question.writer.writerId.eq(questioner.id))
                .leftJoin(answerer).on(answer.writer.writerId.eq(answerer.id))
                .leftJoin(questioner.avatar, avatar)
                .leftJoin(answerer.avatar, avatar)
                .transform(groupBy(meeting.id)
                        .list(new QMeetingDetailResponseDto(
                                constant(meetingId),
                                meeting.category,
                                meeting.hostId,
                                host.nickname.nickname,
                                host.avatar.remotePath,
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
                                set(address.si.append(" ").append(address.gu)),
                                set(tag.name),
                                list(dateTime1.dateTime))
                        ));

        if (dtos.isEmpty()) throw new MeetingException(ErrorCode.DATA_NOT_FOUND);

        List<ResponseQuestionDto> questionDtos = questionQueryRepository.getQuestions(meetingId);
        dtos.get(0).init(questionDtos);

        return dtos.get(0);
    }
}
