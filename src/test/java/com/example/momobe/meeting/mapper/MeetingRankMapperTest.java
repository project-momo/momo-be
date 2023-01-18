package com.example.momobe.meeting.mapper;

import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.dto.out.MeetingRankDto;
import com.example.momobe.meeting.dto.out.MeetingResponseDto;
import com.example.momobe.meeting.dto.out.MeetingUserResponseWithEmailDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;

class MeetingRankMapperTest {
    MeetingRankMapper meetingRankMapper;

    @BeforeEach
    void init() {
        meetingRankMapper = new MeetingRankMapperImpl();
    }

    @Test
    @DisplayName("변환 테스트")
    void test() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //given
        MeetingResponseDto dto = MeetingResponseDto.builder()
                .meetingId(ID1)
                .category("카테고리")
                .meetingState("상태")
                .content(CONTENT1)
                .host(new MeetingUserResponseWithEmailDto(ID1, NICKNAME1, TISTORY_URL, EMAIL1))
                .price(10000L)
                .isOpen(Boolean.TRUE)
                .title(TITLE1)
                .build();

        // when
        MeetingRankDto result = meetingRankMapper.of(dto);

        // then
        assertThat(result.getMeetingId()).isEqualTo(dto.getMeetingId());
        assertThat(result.getContent()).isEqualTo(dto.getContent());
        assertThat(result.getTitle()).isEqualTo(dto.getTitle());
        assertThat(result.getImageUrl()).isEqualTo(dto.getHost().getImageUrl());
    }
}