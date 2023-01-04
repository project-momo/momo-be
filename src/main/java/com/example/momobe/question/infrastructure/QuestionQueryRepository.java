package com.example.momobe.question.infrastructure;

import com.example.momobe.answer.domain.QAnswer;
import com.example.momobe.question.domain.QQuestion;
import com.example.momobe.question.dto.out.QResponseQuestionDto;
import com.example.momobe.question.dto.out.QResponseQuestionDto_Answer;
import com.example.momobe.question.dto.out.ResponseQuestionDto;
import com.example.momobe.user.domain.QAvatar;
import com.example.momobe.user.domain.QUser;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.momobe.answer.domain.QAnswer.*;
import static com.example.momobe.question.domain.QQuestion.*;
import static com.example.momobe.user.domain.QAvatar.*;
import static com.example.momobe.user.domain.QUser.*;
import static com.querydsl.core.group.GroupBy.*;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    QUser questioner = new QUser("questioner");
    QUser answerer = new QUser("answerer");
    public List<ResponseQuestionDto> getQuestions(Long meetingId) {
        return jpaQueryFactory.select(question)
                .from(question)
                .innerJoin(questioner).on(question.writer.writerId.eq(questioner.id))
                .leftJoin(answer).on(question.id.eq(answer.question.questionId))
                .leftJoin(answerer).on(answer.writer.writerId.eq(answerer.id))
                .leftJoin(questioner.avatar, avatar)
                .leftJoin(answerer.avatar, avatar)
                .where(question.meeting.meetingId.eq(meetingId))
                .orderBy(question.createdAt.asc())
                .transform(groupBy(question.id)
                        .list(
                                new QResponseQuestionDto(question.id, question.content.content, questioner.id, questioner.email.address,
                                        questioner.nickname.nickname, questioner.avatar.remotePath, question.createdAt,
                                        question.lastModifiedAt, list(
                                                new QResponseQuestionDto_Answer(answer.id, answer.content.content, answerer.id, answerer.email.address, answerer.nickname.nickname, answerer.avatar.remotePath,
                                                        answer.createdAt, answer.lastModifiedAt)
                                ))
                        ))
                .stream()
                .peek(e -> {
                    ResponseQuestionDto.Answer answer = e.getAnswers().get(0);
                    if (answer.getAnswerId() == null) {
                        e.getAnswers().remove(answer);
                    }
                }).collect(Collectors.toList());
    }
}
