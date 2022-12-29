package com.example.momobe.meeting.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MeetingUserResponseDto {
    private final Long userId;
    private final String nickname;
    private final String imageUrl;
}
