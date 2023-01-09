package com.example.momobe.meeting.application;

import com.example.momobe.address.application.AddressCommonService;
import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.meeting.domain.Meeting;
import com.example.momobe.meeting.domain.MeetingNotFoundException;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.meeting.domain.enums.Tag;
import com.example.momobe.tag.application.TagCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingCommonService {
    private final MeetingRepository meetingRepository;
    private final TagCommonService tagCommonService;
    private final AddressCommonService addressCommonService;

    public Meeting getMeetingOrThrowException(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException(ErrorCode.DATA_NOT_FOUND));
    }

    public List<Long> verifyAddressesAndFindTagIds(List<Long> addressIds, List<Tag> tags) {
        addressCommonService.verifyAddressIdsOrThrowException(addressIds);
        return tagCommonService.findTagIdsByEngNamesOrThrowException(
                tags.stream().map(Enum::name).collect(Collectors.toList()));
    }
}
