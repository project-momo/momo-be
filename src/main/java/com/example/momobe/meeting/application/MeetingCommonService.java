package com.example.momobe.meeting.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingNotFoundException;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.reservation.domain.ReservationNotPossibleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeetingCommonService {
    private final MeetingRepository meetingRepository;

    public Meeting findMeetingOrThrowException(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(ErrorCode.DATA_NOT_FOUND));
    }
}
