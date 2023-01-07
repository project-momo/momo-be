package com.example.momobe.meeting.ui;

import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.application.MeetingRegistrationService;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.dto.in.MeetingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Validated
@RestController
@RequiredArgsConstructor
public class MeetingRegistrationController {
    private final MeetingRegistrationService meetingRegistrationService;

    @PostMapping("/meetings")
    @ResponseStatus(CREATED)
    public Long registerMeeting(@Token UserInfo userInfo,
                                @Valid @RequestBody MeetingRequestDto requestDto) {
        Meeting meeting = meetingRegistrationService.saveMeeting(userInfo.getId(), requestDto);
        return meeting.getId();
    }
}
