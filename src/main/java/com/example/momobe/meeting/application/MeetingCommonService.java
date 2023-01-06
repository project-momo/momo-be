package com.example.momobe.meeting.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingNotFoundException;
import com.example.momobe.meeting.domain.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingCommonService {
    private final MeetingRepository meetingRepository;

    public Meeting getMeetingOrThrowException(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(ErrorCode.DATA_NOT_FOUND));
    }

    public void closeMeeting(Long userId, Long meetingId) {
        getMeetingOrThrowException(meetingId).closeWithHostId(userId);
    }
}
