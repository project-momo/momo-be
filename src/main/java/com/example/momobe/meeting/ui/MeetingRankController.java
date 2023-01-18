package com.example.momobe.meeting.ui;

import com.example.momobe.meeting.constants.MeetingConstants;
import com.example.momobe.meeting.domain.MeetingRankingStore;
import com.example.momobe.meeting.dto.out.MeetingRankDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ranks")
@RequiredArgsConstructor
public class MeetingRankController {
    private final MeetingRankingStore<MeetingRankDto> meetingRankingStore;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MeetingRankDto> getRanks() {
        return meetingRankingStore.getRanks(MeetingConstants.RANKING_CACHE_KEY, 0, 9, MeetingRankDto.class);
    }
}
