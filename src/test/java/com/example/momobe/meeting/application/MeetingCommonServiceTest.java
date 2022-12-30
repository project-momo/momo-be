package com.example.momobe.meeting.application;

import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.meeting.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingCommonServiceTest {
    @InjectMocks
    MeetingCommonService meetingCommonService;

    @Mock
    MeetingRepository meetingRepository;

    @Test
    @DisplayName("결과 조회 실패 시 MeetingNotFoundException 발생")
    void findMeetingOrThrowExceptionTest1() {
        //given
        given(meetingRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> meetingCommonService.findMeetingOrThrowException(1L))
                .isInstanceOf(MeetingNotFoundException.class);
    }

    @Test
    @DisplayName("정상 조회 시 찾은 데이터를 반환한다.")
    void findMeetingOrThrowExceptionTest2() {
        //given
        Meeting meeting = Meeting.builder()
                .hostId(ID1)
                .price(10000L)
                .personnel(10)
                .dateTimeInfo(DateTimeInfo.builder()
                        .dateTimes(List.of(new DateTime(LocalDateTime.now())))
                        .build())
                .build();

        given(meetingRepository.findById(ID1)).willReturn(Optional.of(meeting));

        //when
        Meeting result = meetingCommonService.findMeetingOrThrowException(ID1);

        //then
        assertThat(result.getPersonnel()).isEqualTo(meeting.getPersonnel());
        assertThat(result.getPrice()).isEqualTo(meeting.getPrice());
        assertThat(result.getHostId()).isEqualTo(meeting.getHostId());
    }
}