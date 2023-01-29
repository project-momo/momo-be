package com.example.momobe.meeting.dto.out;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MeetingUserResponseDto {
    private final Long userId;
    private final String nickname;
    private final String imageUrl;
}
