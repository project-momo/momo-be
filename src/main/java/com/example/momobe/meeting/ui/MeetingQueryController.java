package com.example.momobe.meeting.ui;

import com.example.momobe.common.dto.PageResponseDto;
import com.example.momobe.meeting.dao.MeetingDetailQueryRepository;
import com.example.momobe.meeting.dao.MeetingQueryRepository;
import com.example.momobe.meeting.domain.MeetingRankingStore;
import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.dto.out.MeetingDetailResponseDto;
import com.example.momobe.meeting.dto.out.MeetingRankDto;
import com.example.momobe.meeting.dto.out.MeetingResponseDto;
import com.example.momobe.meeting.mapper.MeetingRankMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

import static com.example.momobe.meeting.constants.MeetingConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetings")
public class MeetingQueryController {
    private final MeetingQueryRepository meetingQueryRepository;
    private final MeetingDetailQueryRepository meetingDetailQueryRepository;
    private final MeetingRankingStore<MeetingRankDto> meetingRankingStore;
    private final MeetingRankMapper meetingRankMapper;

    @GetMapping
    public PageResponseDto<MeetingResponseDto> meetingQuery(@PathParam("keyword") String keyword,
                                                            @PathParam("category") Category category,
                                                            @PathParam("tag") String tag,
                                                            Pageable pageable) {
        return PageResponseDto.of(meetingQueryRepository.findAll(keyword, category, tag, pageable));
    }

    @GetMapping("/{meeting-id}")
    public MeetingResponseDto meetingDetailQuery(@PathVariable("meeting-id") Long meetingId) {
        MeetingDetailResponseDto meeting = meetingDetailQueryRepository.findById(meetingId);
        meetingRankingStore.updateRank(RANKING_CACHE_KEY, meetingRankMapper.of(meeting));

        return meeting;
    }
}
