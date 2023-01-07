package com.example.momobe.meeting.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingCloseService {
    private final MeetingCommonService meetingCommonService;

    public void closeMeeting(Long userId, Long meetingId) {
        meetingCommonService.getMeetingOrThrowException(meetingId)
                .closeWithHostId(userId);
    }
}
