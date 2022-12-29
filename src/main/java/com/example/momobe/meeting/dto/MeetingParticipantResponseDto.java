package com.example.momobe.meeting.dto;

import com.example.momobe.meeting.domain.enums.Category;
import com.example.momobe.meeting.domain.enums.DatePolicy;
import com.example.momobe.meeting.domain.enums.MeetingState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingParticipantResponseDto {
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
}
