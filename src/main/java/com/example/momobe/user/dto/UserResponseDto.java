package com.example.momobe.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UserResponseDto {
    private final String nickname;
    private final String email;
    private final Long point;
    private final String avatar;

}
