package com.example.momobe.answer.mapper;

import com.example.momobe.answer.domain.*;
import com.example.momobe.answer.dto.AnswerDto;
import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.common.resolver.UserInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AnswerMapperTest {
    AnswerMapper answerMapper;

    @BeforeEach
    void init() {
        answerMapper = new AnswerMapperImpl();
    }

    @Test
    void answerMapperTest() {
        //given
        AnswerDto dto = AnswerDto.builder()
                .content(CONTENT1)
                .build();

        Long meetingId = 1L;

        Long questionId = 1L;

        UserInfo userInfo = UserInfo.builder()
                .id(ID1)
                .roles(ROLE_USER_LIST)
                .email(EMAIL1)
                .build();

        //when
        Answer result = answerMapper.of(meetingId, questionId, dto, userInfo);

        //then
        assertThat(result.getContent()).isEqualTo(new Content(dto.getContent()));
        assertThat(result.getMeeting()).isEqualTo(new Meeting(1L));
        assertThat(result.getQuestion()).isEqualTo(new Question(1L));
        assertThat(result.getWriter()).isEqualTo(new Writer(ID1));
    }
}