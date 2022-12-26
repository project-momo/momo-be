package com.example.momobe.answer.mapper;

import com.example.momobe.answer.domain.*;
import com.example.momobe.answer.dto.AnswerDto;
import com.example.momobe.common.resolver.UserInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnswerMapper {
    default Answer of(Long meetingId, Long questionId, AnswerDto answerDto, UserInfo userInfo) {
        return new Answer(new Content(answerDto.getContent()), new Meeting(meetingId), new Writer(userInfo.getId()), new Question(questionId));
    }
}
