package com.example.momobe.meeting.ui;

import com.example.momobe.common.dto.PageResponseDto;
import com.example.momobe.meeting.dao.MeetingDetailQueryRepository;
import com.example.momobe.meeting.dao.MeetingQueryRepository;
import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.dto.out.MeetingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetings")
public class MeetingQueryController {

    private final MeetingQueryRepository meetingQueryRepository;
    private final MeetingDetailQueryRepository meetingDetailQueryRepository;

    @GetMapping
    public PageResponseDto<MeetingResponseDto> meetingQuery(@PathParam("keyword") String keyword,
                                                            @PathParam("category") Category category,
                                                            Pageable pageable) {
        return PageResponseDto.of(meetingQueryRepository.findAll(keyword, category, pageable));
    }

    @GetMapping("/{meeting-id}")
    public MeetingResponseDto meetingDetailQuery(@PathVariable("meeting-id") Long meetingId) {
        return meetingDetailQueryRepository.findById(meetingId);
    }

}
