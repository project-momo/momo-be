package com.example.momobe.meeting.application;

import com.example.momobe.address.application.AddressCommonService;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.meeting.dto.in.MeetingRequestDto;
import com.example.momobe.meeting.mapper.MeetingMapper;
import com.example.momobe.tag.application.TagCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingRegistrationService {
    private final MeetingMapper meetingMapper;
    private final MeetingRepository meetingRepository;
    private final TagCommonService tagCommonService;
    private final AddressCommonService addressCommonService;

    public Meeting saveMeeting(Long hostId, MeetingRequestDto meetingRequestDto) {
        addressCommonService.verifyAddressIdsOrThrowException(meetingRequestDto.getAddress().getAddressIds());
        List<Long> tagIds = tagCommonService.findTagIdsByEngNamesOrThrowException(
                meetingRequestDto.getTags().stream().map(Enum::name).collect(Collectors.toList()));

        Meeting meeting = meetingMapper.toMeeting(meetingRequestDto, hostId, tagIds);
        return meetingRepository.save(meeting);
    }
}
