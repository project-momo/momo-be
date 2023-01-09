package com.example.momobe.meeting.application;

import com.example.momobe.common.util.ReflectionUtil;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.mapper.MeetingMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.momobe.common.enums.TestConstants.ID1;
import static com.example.momobe.meeting.enums.MeetingConstants.MEETING_UPDATE_DTO;
import static com.example.momobe.meeting.enums.MeetingConstants.generateMeeting;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MeetingUpdateServiceTest {
    @InjectMocks
    private MeetingUpdateService meetingUpdateService;

    @Mock
    private MeetingCommonService meetingCommonService;

    @BeforeEach
    void init() throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.setField(meetingUpdateService, "meetingMapper", new MeetingMapperImpl());
    }

    @Test
    void updateMeeting() {
        // given
        Meeting meeting = generateMeeting(ID1);
        given(meetingCommonService.getMeetingOrThrowException(ID1))
                .willReturn(meeting);
        given(meetingCommonService.verifyAddressesAndFindTagIds(
                MEETING_UPDATE_DTO.getAddress().getAddressIds(), MEETING_UPDATE_DTO.getTags()))
                .willReturn(List.of(1L, 3L, 4L, 6L));

        // when
        meetingUpdateService.updateMeeting(ID1, ID1, MEETING_UPDATE_DTO);

        // then
        assertThat(meeting.getTitle()).isEqualTo(MEETING_UPDATE_DTO.getTitle());
        assertThat(meeting.getContent()).isEqualTo(MEETING_UPDATE_DTO.getContent());
        assertThat(meeting.getCategory()).isEqualTo(MEETING_UPDATE_DTO.getCategory());
        assertThat(meeting.getTagIds()).hasSize(MEETING_UPDATE_DTO.getTags().size());
        assertThat(meeting.getAddress().getAddressIds()).isEqualTo(MEETING_UPDATE_DTO.getAddress().getAddressIds());
        assertThat(meeting.getAddress().getAddressInfo()).isEqualTo(MEETING_UPDATE_DTO.getAddress().getAddressInfo());
        assertThat(meeting.getPersonnel()).isEqualTo(MEETING_UPDATE_DTO.getPersonnel());
        assertThat(meeting.getPrice()).isEqualTo(MEETING_UPDATE_DTO.getPrice());
    }
}