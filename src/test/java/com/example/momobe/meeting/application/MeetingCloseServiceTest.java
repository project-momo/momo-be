package com.example.momobe.meeting.application;

import com.example.momobe.meeting.domain.CanNotUpdateMeetingException;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.enums.MeetingState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.momobe.common.enums.TestConstants.ID1;
import static com.example.momobe.common.enums.TestConstants.ID2;
import static com.example.momobe.meeting.enums.MeetingConstants.generateMeeting;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MeetingCloseServiceTest {
    @InjectMocks
    MeetingCloseService meetingCloseService;

    @Mock
    MeetingCommonService meetingCommonService;

    @Test
    @DisplayName("Meeting의 hostId와 요청한 userId가 다르면 예외가 발생한다.")
    void closeMeetingTest1() throws Exception {
        // given
        Long meetingId = ID1;
        given(meetingCommonService.getMeetingOrThrowException(meetingId))
                .willReturn(generateMeeting(ID1));

        // when
        // then
        assertThatThrownBy(() -> meetingCloseService.closeMeeting(ID2, meetingId))
                .isInstanceOf(CanNotUpdateMeetingException.class);
    }

    @Test
    @DisplayName("Meeting의 hostId와 요청한 userId가 같으면 MeetingState가 CLOSE로 바뀐다.")
    void closeMeetingTest2() throws Exception {
        // given
        Long meetingId = ID1;
        Meeting meeting = generateMeeting(ID1);
        given(meetingCommonService.getMeetingOrThrowException(meetingId))
                .willReturn(meeting);

        // when
        meetingCloseService.closeMeeting(ID1, meetingId);

        // then
        assertThat(meeting.getMeetingState()).isEqualTo(MeetingState.CLOSE);
    }
}
