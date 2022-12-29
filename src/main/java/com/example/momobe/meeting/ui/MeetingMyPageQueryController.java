package com.example.momobe.meeting.ui;

import com.example.momobe.common.dto.PageResponseDto;
import com.example.momobe.common.resolver.Token;
import com.example.momobe.common.resolver.UserInfo;
import com.example.momobe.meeting.dao.MeetingHostQueryRepository;
import com.example.momobe.meeting.dao.MeetingParticipantQueryRepository;
import com.example.momobe.meeting.dto.MeetingHostResponseDto;
import com.example.momobe.meeting.dto.MeetingParticipantResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/meetings")
public class MeetingMyPageQueryController {

    private final MeetingHostQueryRepository meetingHostQueryRepository;
    private final MeetingParticipantQueryRepository meetingParticipantQueryRepository;

    @GetMapping("/hosts")
    public PageResponseDto<MeetingHostResponseDto> meetingHostQuery(@Token UserInfo userInfo, Pageable pageable) {
        return PageResponseDto.of(meetingHostQueryRepository.findAll(userInfo.getId(), pageable));
    }

    @GetMapping("/participants")
    public PageResponseDto<MeetingParticipantResponseDto> meetingParticipantQuery(@Token UserInfo userInfo, Pageable pageable) {
        return PageResponseDto.of(meetingParticipantQueryRepository.findAll(userInfo.getId(), pageable));
    }

}
