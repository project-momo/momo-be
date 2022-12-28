package com.example.momobe.meeting.dto;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MeetingResponseDto {
    private final Long meetingId;
    private final Category category;
    private final HostResponseDto host;
    private final String title;
    private final String content;
    private final String address;
    private final Boolean isOpen;
    private final Long price;

    @QueryProjection
    public MeetingResponseDto(Long meetingId, Category category, Long hostId,
                              String hostNickname, String hostImageUrl,
                              String title, String content, String address,
                              MeetingState meetingState, Long price) {
        this.meetingId = meetingId;
        this.category = category;
        this.host = new HostResponseDto(hostId, hostNickname, hostImageUrl);
        this.title = title;
        this.content = content;
        this.address = address;
        this.isOpen = meetingState == MeetingState.OPEN;
        this.price = price;
    }
}
