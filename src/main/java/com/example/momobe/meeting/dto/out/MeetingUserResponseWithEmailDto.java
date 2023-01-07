package com.example.momobe.meeting.dto.out;

import lombok.Getter;

@Getter
public class MeetingUserResponseWithEmailDto extends MeetingUserResponseDto {
    private final String email;

    public MeetingUserResponseWithEmailDto(Long userId, String nickname, String imageUrl, String email) {
        super(userId, nickname, imageUrl);
        this.email = email;
    }
}
