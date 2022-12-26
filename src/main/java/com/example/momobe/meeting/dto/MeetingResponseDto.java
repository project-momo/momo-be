package com.example.momobe.meeting.dto;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.MeetingStatus;
import com.example.momobe.meeting.domain.enums.PricePolicy;
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
    private final MeetingStatus meetingStatus;
    private final PricePolicy pricePolicy;
    private final Long price;

    @QueryProjection
    public MeetingResponseDto(Long meetingId, Category category, Long hostId,
                              String hostNickname, String hostImageUrl,
                              String title, String content, String address,
                              MeetingStatus meetingStatus, PricePolicy pricePolicy, Long price) {
        this.meetingId = meetingId;
        this.category = category;
        this.host = new HostResponseDto(hostId, hostNickname, hostImageUrl);
        this.title = title;
        this.content = content;
        this.address = address;
        this.meetingStatus = meetingStatus;
        this.pricePolicy = pricePolicy;
        this.price = price;
    }
}
