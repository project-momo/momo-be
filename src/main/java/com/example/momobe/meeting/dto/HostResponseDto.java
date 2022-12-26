package com.example.momobe.meeting.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HostResponseDto {
    private final Long hostId;
    private final String nickname;
    private final String imageUrl;
}
