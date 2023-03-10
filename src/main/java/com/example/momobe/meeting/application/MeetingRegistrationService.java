package com.example.momobe.meeting.application;

import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.meeting.dto.in.MeetingRequestDto;
import com.example.momobe.meeting.mapper.MeetingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingRegistrationService {
    private final MeetingMapper meetingMapper;
    private final MeetingRepository meetingRepository;
    private final MeetingCommonService meetingCommonService;

    public Meeting saveMeeting(Long hostId, MeetingRequestDto meetingRequestDto) {
        List<Long> tagIds = meetingCommonService.verifyAddressesAndFindTagIds(
                meetingRequestDto.getAddress().getAddressIds(), meetingRequestDto.getTags());

        Meeting meeting = meetingMapper.toMeeting(meetingRequestDto, hostId, tagIds);
        return meetingRepository.save(meeting);
    }
}
