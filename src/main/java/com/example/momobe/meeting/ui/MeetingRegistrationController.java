package com.example.momobe.meeting.ui;

import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.meeting.dto.MeetingRequestDto;
import com.example.momobe.meeting.mapper.MeetingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@Validated
public class MeetingRegistrationController {

    private final MeetingMapper meetingMapper;
    private final MeetingRepository meetingRepository;

    @PostMapping("/meetings")
    @ResponseStatus(CREATED)
    public Long registerMeeting(@Token UserInfo userInfo,
                                @Valid @RequestBody MeetingRequestDto requestDto) {
        Meeting meeting = meetingMapper.toMeeting(requestDto, userInfo.getId());
        meetingRepository.save(meeting);
        return meeting.getId();
    }
}
