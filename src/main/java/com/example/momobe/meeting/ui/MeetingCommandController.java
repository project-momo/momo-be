package com.example.momobe.meeting.ui;

import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.application.MeetingCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/meetings")
public class MeetingCommandController {
    private final MeetingCommonService meetingCommonService;

    @DeleteMapping("/{meeting-id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteMeeting(@Token UserInfo userInfo,
                              @Positive @PathVariable("meeting-id") Long meetingId) {
        meetingCommonService.closeMeeting(userInfo.getId(), meetingId);
    }
}
