package com.example.momobe.meeting.application;

import com.example.momobe.address.application.AddressFindService;
import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingException;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.tag.application.TagCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingCommonService {
    private final MeetingRepository meetingRepository;
    private final TagCommonService tagCommonService;
    private final AddressFindService addressFindService;

    public Meeting getMeeting(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCode.DATA_NOT_FOUND));
    }

    public List<Long> verifyAddressesAndFindTagIds(List<Long> addressIds, List<String> tags) {
        addressFindService.verifyAddressId(addressIds);
        return tagCommonService.findTagIdsByNamesOrThrowException(tags);
    }
}
