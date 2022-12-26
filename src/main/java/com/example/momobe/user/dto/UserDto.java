package com.example.momobe.user.dto;

import com.example.momobe.user.domain.Email;
import com.example.momobe.user.domain.Nickname;
import com.example.momobe.user.domain.Point;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UserDto {
    private final Nickname nickname;
    private final Email email;
    private final Point point;

}
