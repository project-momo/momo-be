package com.example.momobe.question.mapper;

import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.question.domain.Content;
import com.example.momobe.question.domain.Meeting;
import com.example.momobe.question.domain.Question;
import com.example.momobe.question.domain.Writer;
import com.example.momobe.question.dto.in.QuestionDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class QuestionMapperTest {
    QuestionMapper questionMapper;

    @BeforeEach
    void init() {
        questionMapper = new QuestionMapperImpl();
    }

    @Test
    @DisplayName("")
    void questionMapperTest() {
        // given
        Long meetingId = 1L;
        UserInfo userInfo = UserInfo.builder()
                .email(EMAIL1)
                .id(ID1)
                .roles(ROLE_USER_LIST)
                .build();
        QuestionDto question = QuestionDto.builder()
                .content(CONTENT1)
                .build();

        // when
        Question result = questionMapper.of(meetingId, userInfo, question);

        // then
        assertThat(result.getMeeting()).isEqualTo(new Meeting(1L));
        assertThat(result.getWriter()).isEqualTo(new Writer(ID1));
        assertThat(result.getContent()).isEqualTo(new Content(CONTENT1));
    }
}