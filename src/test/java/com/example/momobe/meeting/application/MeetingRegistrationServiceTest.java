package com.example.momobe.meeting.application;

import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.meeting.mapper.MeetingMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.enums.MeetingConstants.MEETING_REQUEST_DTO_WITH_ONE_DAY;
import static com.example.momobe.meeting.enums.MeetingConstants.generateMeeting;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MeetingRegistrationServiceTest {
    @InjectMocks
    private MeetingRegistrationService meetingRegistrationService;
    @Mock
    private MeetingMapper meetingMapper;
    @Mock
    private MeetingRepository meetingRepository;
    @Mock
    private MeetingCommonService meetingCommonService;

    @Test
    void saveMeeting() {
        // given
        List<Long> tagIds = List.of(ID1, ID2, ID3);
        given(meetingCommonService.verifyAddressesAndFindTagIds(
                MEETING_REQUEST_DTO_WITH_ONE_DAY.getAddress().getAddressIds(),
                MEETING_REQUEST_DTO_WITH_ONE_DAY.getTags()))
                .willReturn(tagIds);
        given(meetingMapper.toMeeting(MEETING_REQUEST_DTO_WITH_ONE_DAY, ID1, tagIds))
                .willReturn(generateMeeting(ID1));
        given(meetingRepository.save(any(Meeting.class)))
                .willAnswer(returnsFirstArg());

        // when
        Meeting meeting = meetingRegistrationService.saveMeeting(ID1, MEETING_REQUEST_DTO_WITH_ONE_DAY);

        // then
        assertThat(meeting.getHostId()).isEqualTo(ID1);
    }
}