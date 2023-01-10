package com.example.momobe.settlement.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.meeting.dao.MeetingQueryRepository;
import com.example.momobe.settlement.domain.NotFoundEndMeetingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CheckSettlementService {
    private final MeetingQueryRepository meetingQueryRepository;

    public List<Long> checkEndMeetingExist(){
        List<Long> meetingId = meetingQueryRepository.findMeetingClosedBefore3days();
        if(meetingId.isEmpty()) throw new NotFoundEndMeetingException(ErrorCode.CAN_NOT_FOUND_END_MEETING);
        return meetingId;
    }

}
