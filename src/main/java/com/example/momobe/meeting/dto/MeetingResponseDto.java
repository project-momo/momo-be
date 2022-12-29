package com.example.momobe.meeting.dto;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.MeetingState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class MeetingResponseDto {
    private final Long meetingId;
    private final Category category;
    private final MeetingUserResponseDto host;
    private final String title;
    private final String content;
    private final String address;
    private final MeetingState meetingState;
    private final Boolean isOpen;
    private final DatePolicy datePolicy;
    private final Long price;
    private final String notice;

    @QueryProjection
    public MeetingResponseDto(Long meetingId, Category category, Long hostId,
                              String hostNickname, String hostImageUrl,
                              String title, String content, String address,
                              MeetingState meetingState, DatePolicy datePolicy, Long price, String notice) {
        this.meetingId = meetingId;
        this.category = category;
        this.host = new MeetingUserResponseDto(hostId, hostNickname, hostImageUrl);
        this.title = title;
        this.content = content;
        this.address = address;
        this.meetingState = meetingState;
        this.isOpen = meetingState == MeetingState.OPEN;
        this.datePolicy = datePolicy;
        this.price = price;
        this.notice = notice;
    }
}
