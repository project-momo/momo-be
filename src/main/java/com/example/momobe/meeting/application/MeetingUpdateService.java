package com.example.momobe.meeting.application;

import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.dto.in.MeetingUpdateDto;
import com.example.momobe.meeting.mapper.MeetingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingUpdateService {
    private final MeetingMapper meetingMapper;
    private final MeetingCommonService meetingCommonService;

    public void updateMeeting(Long hostId, Long meetingId, MeetingUpdateDto updateDto) {
        List<Long> tagIds = meetingCommonService.verifyAddressesAndFindTagIds(
                updateDto.getAddress().getAddressIds(), updateDto.getTags());
        Meeting meeting = meetingCommonService.getMeetingOrThrowException(meetingId);
        Meeting newMeeting = meetingMapper.toMeeting(updateDto, hostId, tagIds);
        meeting.updateMeetingInfo(newMeeting);
    }
}
