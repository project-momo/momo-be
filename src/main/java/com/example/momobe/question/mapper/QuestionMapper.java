package com.example.momobe.question.mapper;

import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.question.domain.Question;
import com.example.momobe.question.dto.in.QuestionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    default Question of(Long meetingId, UserInfo userInfo, QuestionDto questionDto) {
        return new Question(meetingId, questionDto.getContent(), userInfo.getId());
    }
}
